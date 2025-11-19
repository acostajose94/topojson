package com.yape.notificationreader;

import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Servicio que escucha las notificaciones de Yape y narra los pagos recibidos
 */
public class YapeNotificationListener extends NotificationListenerService {

    private static final String TAG = "YapeNotificationListener";
    private static final String YAPE_PACKAGE = "com.yape.app"; // Package de la app de Yape
    private static final String PREFS_NAME = "YapeReaderPrefs";
    private static final String PREF_READ_NAME = "read_name";
    private static final String PREF_ENABLED = "service_enabled";

    private TextToSpeech tts;
    private SharedPreferences prefs;

    @Override
    public void onCreate() {
        super.onCreate();
        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Inicializar Text-to-Speech
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = tts.setLanguage(new Locale("es", "ES"));
                if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e(TAG, "Idioma español no soportado");
                    // Intentar con español de América Latina
                    tts.setLanguage(new Locale("es", "MX"));
                }
                tts.setPitch(1.0f);
                tts.setSpeechRate(1.0f);
            } else {
                Log.e(TAG, "Error al inicializar TTS");
            }
        });

        Log.d(TAG, "Servicio de notificaciones iniciado");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        // Verificar si el servicio está habilitado
        if (!prefs.getBoolean(PREF_ENABLED, true)) {
            return;
        }

        // Verificar si la notificación es de Yape
        String packageName = sbn.getPackageName();
        if (!packageName.equals(YAPE_PACKAGE)) {
            return;
        }

        Notification notification = sbn.getNotification();
        Bundle extras = notification.extras;

        // Obtener el título y el texto de la notificación
        String title = extras.getString(Notification.EXTRA_TITLE, "");
        String text = extras.getString(Notification.EXTRA_TEXT, "");
        String bigText = extras.getString(Notification.EXTRA_BIG_TEXT, "");

        Log.d(TAG, "Notificación de Yape recibida");
        Log.d(TAG, "Título: " + title);
        Log.d(TAG, "Texto: " + text);
        Log.d(TAG, "BigText: " + bigText);

        // Procesar la notificación
        processYapeNotification(title, text, bigText);
    }

    /**
     * Procesa la notificación de Yape y extrae la información del pago
     */
    private void processYapeNotification(String title, String text, String bigText) {
        // Usar bigText si está disponible, sino usar text
        String content = (bigText != null && !bigText.isEmpty()) ? bigText : text;

        // Verificar si es una notificación de pago recibido
        if (isPaymentReceived(title, content)) {
            String name = extractSenderName(title, content);
            String amount = extractAmount(content);

            if (amount != null) {
                speakPayment(name, amount);
            }
        }
    }

    /**
     * Verifica si la notificación es de un pago recibido
     */
    private boolean isPaymentReceived(String title, String content) {
        // Patrones comunes en notificaciones de pagos recibidos
        String lowerTitle = title.toLowerCase();
        String lowerContent = content.toLowerCase();

        return lowerTitle.contains("recibiste") ||
               lowerTitle.contains("te yapeo") ||
               lowerTitle.contains("te yapeó") ||
               lowerContent.contains("recibiste") ||
               lowerContent.contains("te yapeo") ||
               lowerContent.contains("te yapeó");
    }

    /**
     * Extrae el nombre del remitente de la notificación
     */
    private String extractSenderName(String title, String content) {
        // Patrón para extraer el nombre: "Te yapeó [Nombre]" o "Recibiste de [Nombre]"
        Pattern pattern1 = Pattern.compile("(?:te yape[oó]|recibiste de)\\s+([^\\d]+?)(?:\\s*S/|\\s*\\d|$)",
                                          Pattern.CASE_INSENSITIVE);
        Matcher matcher1 = pattern1.matcher(title);
        if (matcher1.find()) {
            return matcher1.group(1).trim();
        }

        Matcher matcher2 = pattern1.matcher(content);
        if (matcher2.find()) {
            return matcher2.group(1).trim();
        }

        // Patrón alternativo: buscar nombre antes del monto
        Pattern pattern3 = Pattern.compile("([A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+)\\s+S/\\s*\\d",
                                          Pattern.CASE_INSENSITIVE);
        Matcher matcher3 = pattern3.matcher(content);
        if (matcher3.find()) {
            String possibleName = matcher3.group(1).trim();
            // Filtrar palabras comunes que no son nombres
            if (!possibleName.toLowerCase().matches(".*(recibiste|yapeo|yapeó|de).*")) {
                return possibleName;
            }
        }

        return null;
    }

    /**
     * Extrae el monto del pago de la notificación
     */
    private String extractAmount(String content) {
        // Patrón para extraer montos en soles: S/ 123.45 o S/123.45
        Pattern pattern = Pattern.compile("S/\\s*([\\d,]+\\.?\\d{0,2})");
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            String amount = matcher.group(1);
            // Limpiar el formato (remover comas)
            amount = amount.replace(",", "");
            return amount;
        }

        return null;
    }

    /**
     * Narra el pago recibido usando Text-to-Speech
     */
    private void speakPayment(String name, String amount) {
        if (tts == null) {
            Log.e(TAG, "TTS no inicializado");
            return;
        }

        boolean readName = prefs.getBoolean(PREF_READ_NAME, false);

        // Formatear el monto para lectura
        String formattedAmount = formatAmountForSpeech(amount);

        String message;
        if (readName && name != null && !name.isEmpty()) {
            message = String.format("Recibiste %s soles de %s", formattedAmount, name);
        } else {
            message = String.format("Recibiste %s soles", formattedAmount);
        }

        Log.d(TAG, "Narrando: " + message);
        tts.speak(message, TextToSpeech.QUEUE_ADD, null, null);
    }

    /**
     * Formatea el monto para que sea más natural al narrar
     */
    private String formatAmountForSpeech(String amount) {
        try {
            double value = Double.parseDouble(amount);

            // Si es un número entero, no leer los decimales
            if (value == Math.floor(value)) {
                return String.valueOf((int) value);
            }

            // Si tiene decimales, formatear de manera natural
            String[] parts = amount.split("\\.");
            if (parts.length == 2) {
                int integerPart = Integer.parseInt(parts[0]);
                int decimalPart = Integer.parseInt(parts[1]);

                if (decimalPart == 0) {
                    return String.valueOf(integerPart);
                } else if (decimalPart < 10) {
                    return integerPart + " punto cero " + decimalPart;
                } else {
                    return integerPart + " punto " + decimalPart;
                }
            }

            return amount;
        } catch (NumberFormatException e) {
            return amount;
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        // No hacer nada cuando se remueve una notificación
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
        Log.d(TAG, "Servicio de notificaciones detenido");
    }
}
