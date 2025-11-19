package com.yape.notificationreader;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Actividad principal con configuración de la aplicación
 */
public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "YapeReaderPrefs";
    private static final String PREF_READ_NAME = "read_name";
    private static final String PREF_ENABLED = "service_enabled";

    private Switch switchReadName;
    private Switch switchServiceEnabled;
    private TextView tvStatus;
    private TextView tvBatteryStatus;
    private Button btnOpenSettings;
    private Button btnBatteryOptimization;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        initViews();
        setupListeners();
        updateStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatus();
    }

    /**
     * Inicializa las vistas
     */
    private void initViews() {
        switchReadName = findViewById(R.id.switch_read_name);
        switchServiceEnabled = findViewById(R.id.switch_service_enabled);
        tvStatus = findViewById(R.id.tv_status);
        tvBatteryStatus = findViewById(R.id.tv_battery_status);
        btnOpenSettings = findViewById(R.id.btn_open_settings);
        btnBatteryOptimization = findViewById(R.id.btn_battery_optimization);

        // Cargar preferencias guardadas
        boolean readName = prefs.getBoolean(PREF_READ_NAME, false);
        boolean serviceEnabled = prefs.getBoolean(PREF_ENABLED, true);

        switchReadName.setChecked(readName);
        switchServiceEnabled.setChecked(serviceEnabled);
    }

    /**
     * Configura los listeners de los controles
     */
    private void setupListeners() {
        // Switch para leer el nombre del remitente
        switchReadName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefs.edit().putBoolean(PREF_READ_NAME, isChecked).apply();
                String message = isChecked ?
                        "Se narrará el nombre y el monto" :
                        "Solo se narrará el monto";
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        // Switch para habilitar/deshabilitar el servicio
        switchServiceEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefs.edit().putBoolean(PREF_ENABLED, isChecked).apply();
                String message = isChecked ?
                        "Servicio habilitado" :
                        "Servicio deshabilitado";
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        // Botón para abrir configuración de acceso a notificaciones
        btnOpenSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNotificationListenerSettings();
            }
        });

        // Botón para desactivar optimización de batería
        btnBatteryOptimization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestBatteryOptimizationExemption();
            }
        });
    }

    /**
     * Actualiza el estado del servicio en la UI
     */
    private void updateStatus() {
        boolean isEnabled = isNotificationListenerEnabled();

        if (isEnabled) {
            tvStatus.setText("✓ Permiso de notificaciones concedido");
            tvStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            btnOpenSettings.setVisibility(View.GONE);
        } else {
            tvStatus.setText("✗ Permiso de notificaciones no concedido");
            tvStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            btnOpenSettings.setVisibility(View.VISIBLE);
            showPermissionDialog();
        }

        // Verificar optimización de batería
        updateBatteryOptimizationStatus();
    }

    /**
     * Actualiza el estado de la optimización de batería en la UI
     */
    private void updateBatteryOptimizationStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            boolean isIgnoringBatteryOptimizations = pm.isIgnoringBatteryOptimizations(getPackageName());

            if (tvBatteryStatus != null) {
                if (isIgnoringBatteryOptimizations) {
                    tvBatteryStatus.setText("✓ Optimización de batería desactivada");
                    tvBatteryStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                    btnBatteryOptimization.setVisibility(View.GONE);
                } else {
                    tvBatteryStatus.setText("⚠ Optimización de batería activa (recomendado desactivar)");
                    tvBatteryStatus.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
                    btnBatteryOptimization.setVisibility(View.VISIBLE);
                }
            }
        } else {
            // Android < 6.0 no tiene optimización de batería
            if (tvBatteryStatus != null) {
                tvBatteryStatus.setVisibility(View.GONE);
            }
            if (btnBatteryOptimization != null) {
                btnBatteryOptimization.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Solicita exención de optimización de batería
     */
    private void requestBatteryOptimizationExemption() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            new AlertDialog.Builder(this)
                    .setTitle("Optimización de batería")
                    .setMessage("Para mantener el servicio activo en segundo plano, se recomienda " +
                            "desactivar la optimización de batería.\n\n" +
                            "En la siguiente pantalla, selecciona 'Todas las apps' y busca " +
                            "'Yape Notification Reader', luego selecciona 'No optimizar'.")
                    .setPositiveButton("Continuar", (dialog, which) -> {
                        Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        try {
                            startActivity(intent);
                        } catch (Exception e) {
                            // Si falla, abrir configuración general de batería
                            Intent settingsIntent = new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                            startActivity(settingsIntent);
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        }
    }

    /**
     * Verifica si el servicio de escucha de notificaciones está habilitado
     */
    private boolean isNotificationListenerEnabled() {
        ComponentName cn = new ComponentName(this, YapeNotificationListener.class);
        String flat = Settings.Secure.getString(getContentResolver(),
                "enabled_notification_listeners");
        return flat != null && flat.contains(cn.flattenToString());
    }

    /**
     * Abre la configuración de acceso a notificaciones
     */
    private void openNotificationListenerSettings() {
        Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
        startActivity(intent);
    }

    /**
     * Muestra un diálogo explicando el permiso necesario
     */
    private void showPermissionDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Permiso necesario")
                .setMessage("Esta aplicación necesita permiso para leer las notificaciones de Yape.\n\n" +
                        "Por favor, habilita el acceso a notificaciones para 'Yape Notification Reader' " +
                        "en la siguiente pantalla.")
                .setPositiveButton("Abrir configuración", (dialog, which) -> {
                    openNotificationListenerSettings();
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
