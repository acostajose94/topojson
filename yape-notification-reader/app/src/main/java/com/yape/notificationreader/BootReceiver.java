package com.yape.notificationreader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * BroadcastReceiver que se ejecuta cuando el dispositivo se reinicia
 * o cuando la aplicación se actualiza, para asegurar que el servicio
 * de notificaciones esté activo
 */
public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "Received action: " + action);

        if (Intent.ACTION_BOOT_COMPLETED.equals(action) ||
            Intent.ACTION_MY_PACKAGE_REPLACED.equals(action) ||
            "android.intent.action.QUICKBOOT_POWERON".equals(action)) {

            Log.d(TAG, "Sistema iniciado o app actualizada - verificando servicio de notificaciones");

            // El NotificationListenerService se reinicia automáticamente por el sistema
            // si tiene los permisos adecuados. Este receiver solo registra el evento.

            // Opcionalmente, podemos abrir la MainActivity para mostrar el estado
            // (comentado por defecto para no interrumpir al usuario)
            /*
            Intent mainIntent = new Intent(context, MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainIntent);
            */

            Log.d(TAG, "Servicio de notificaciones debería estar activo");
        }
    }
}
