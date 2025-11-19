# Yape Notification Reader

Aplicación Android que lee y narra automáticamente los pagos recibidos a través de Yape usando Text-to-Speech (TTS).

## 📱 Características

- ✅ **Lectura automática de notificaciones de Yape**: Detecta cuando recibes un pago
- 🔊 **Narración por voz**: Usa Text-to-Speech para narrar el monto recibido
- 👤 **Opción de narrar el nombre**: Configurable para incluir el nombre del remitente
- 🔄 **Servicio persistente en segundo plano**: Se mantiene activo incluso cuando cierras la app
- 🔋 **Optimización de batería configurable**: Opción para evitar que el sistema detenga el servicio
- 📲 **Notificación persistente**: Muestra que el servicio está activo (se puede minimizar)
- 🔁 **Reinicio automático**: Se reactiva después de reiniciar el dispositivo
- ⚙️ **Configuración simple**: Interfaz intuitiva para personalizar la experiencia
- 🔒 **Privacidad**: Solo procesa notificaciones de Yape, ninguna información se envía a servidores externos

## 🚀 Instalación

### Requisitos
- **Android**: 5.0 (API 21) o superior
- **Java**: JDK 17 o JDK 21
- **Gradle**: 8.9 (incluido en el proyecto)
- **Aplicación de Yape**: Debe estar instalada en el dispositivo

### Pasos de instalación

1. **Compilar el proyecto**:
   ```bash
   cd yape-notification-reader
   ./gradlew assembleDebug
   ```

2. **Instalar en dispositivo**:
   ```bash
   ./gradlew installDebug
   ```

   O copiar el APK generado en `app/build/outputs/apk/debug/app-debug.apk` a tu dispositivo Android.

**⚠️ Problemas de compilación?** Consulta la [Guía de Compilación](BUILD_GUIDE.md) para solucionar errores comunes.

## 📖 Cómo usar

### Primera configuración

1. **Abrir la aplicación** "Yape Notification Reader"

2. **Conceder permisos de notificaciones**:
   - La app te pedirá acceso a las notificaciones
   - Toca "Abrir Configuración de Notificaciones"
   - Busca "Yape Notification Reader" en la lista
   - Activa el permiso de acceso a notificaciones

3. **Desactivar optimización de batería** (Recomendado):
   - Toca "Desactivar Optimización de Batería" si aparece el botón
   - Esto permite que el servicio se mantenga activo en segundo plano
   - En la pantalla de configuración, selecciona "Todas las apps"
   - Busca "Yape Notification Reader" y selecciona "No optimizar"
   - Esto evita que Android detenga el servicio para ahorrar batería

4. **Configurar preferencias**:
   - **Activar servicio**: Habilita/deshabilita la narración de pagos
   - **Leer nombre del remitente**: Activa si deseas que se narre el nombre de quien te envió el pago

### Uso diario

Una vez configurada, la aplicación funcionará automáticamente:

- Cuando recibas un pago por Yape, escucharás una narración
- **Sin nombre**: "Recibiste 50 soles"
- **Con nombre**: "Recibiste 50 soles de Juan Pérez"

**Servicio en segundo plano:**
- Verás una notificación persistente que dice "Yape Reader activo"
- Esta notificación indica que el servicio está funcionando
- Puedes minimizar la notificación deslizándola hacia un lado
- El servicio continuará activo incluso si cierras la app
- Se reiniciará automáticamente si reinicias el dispositivo

## ⚙️ Configuración

### Opciones disponibles

| Opción | Descripción | Valor por defecto |
|--------|-------------|-------------------|
| **Activar servicio** | Habilita o deshabilita completamente la narración | ✅ Habilitado |
| **Leer nombre** | Include el nombre del remitente en la narración | ❌ Deshabilitado |

### Cambiar idioma de narración

La aplicación usa el idioma del sistema. Para español:
- Ve a **Configuración** > **Idioma y entrada** > **Text-to-Speech**
- Selecciona un motor de TTS que soporte español
- Si no tienes uno instalado, descarga "Google Text-to-Speech" desde Play Store

## 🔧 Estructura del proyecto

```
yape-notification-reader/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/yape/notificationreader/
│   │       │   ├── MainActivity.java              # Actividad principal
│   │       │   ├── YapeNotificationListener.java  # Servicio de notificaciones (foreground)
│   │       │   └── BootReceiver.java              # Reinicio automático después de reboot
│   │       ├── res/
│   │       │   ├── layout/
│   │       │   │   └── activity_main.xml          # Layout principal
│   │       │   ├── values/
│   │       │   │   ├── strings.xml                # Textos de la app
│   │       │   │   ├── colors.xml                 # Colores
│   │       │   │   └── styles.xml                 # Estilos
│   │       │   └── drawable/                      # Iconos
│   │       └── AndroidManifest.xml                # Manifiesto
│   ├── build.gradle                               # Configuración del módulo
│   └── proguard-rules.pro                         # Reglas de ofuscación
├── build.gradle                                   # Configuración del proyecto
├── settings.gradle                                # Configuración de Gradle
└── gradle.properties                              # Propiedades de Gradle
```

## 🔍 Funcionamiento técnico

### Detección de notificaciones

La aplicación usa `NotificationListenerService` para interceptar notificaciones de Yape:
- Filtra solo notificaciones del paquete `com.yape.app`
- Extrae el contenido de la notificación
- Analiza el texto para identificar pagos recibidos

### Extracción de datos

Usando expresiones regulares, extrae:
- **Monto**: Busca patrones como "S/ 123.45"
- **Nombre**: Busca el nombre del remitente en el título o cuerpo de la notificación

### Narración

Utiliza `TextToSpeech` de Android:
- Inicializa el motor TTS en español
- Formatea el monto para lectura natural
- Genera el mensaje según la configuración
- Narra usando la cola de TTS

### Servicio en segundo plano

Para mantener el servicio activo permanentemente:
- **Foreground Service**: El servicio se ejecuta en primer plano con una notificación persistente
- **Canal de notificación**: Usa importancia LOW para no molestar al usuario
- **Boot Receiver**: Se reinicia automáticamente cuando el dispositivo se enciende
- **Exención de batería**: Opción para evitar que el sistema detenga el servicio
- **Reconexión automática**: Si el sistema desconecta el listener, intenta reconectar automáticamente

## 🛡️ Privacidad y seguridad

- ✅ La aplicación **NO** envía datos a internet
- ✅ Solo procesa notificaciones de Yape
- ✅ No almacena información de pagos
- ✅ Las configuraciones se guardan localmente con `SharedPreferences`
- ✅ Código abierto para auditoría

## 🐛 Solución de problemas

### La aplicación no narra los pagos

1. **Verificar permisos**:
   - Configuración > Aplicaciones > Acceso especial > Acceso a notificaciones
   - Asegúrate de que "Yape Notification Reader" esté habilitado

2. **Verificar configuración**:
   - Abre la app y verifica que "Activar servicio" esté encendido

3. **Verificar TTS**:
   - Configuración > Idioma y entrada > Text-to-Speech
   - Prueba la síntesis de voz

### La narración está en inglés

1. Descarga e instala "Google Text-to-Speech" desde Play Store
2. Ve a Configuración > Idioma y entrada > Text-to-Speech
3. Selecciona "Google Text-to-Speech Engine"
4. Descarga el paquete de idioma español

### No se lee el nombre del remitente

1. Verifica que la opción "Leer nombre del remitente" esté activada
2. Algunas notificaciones de Yape pueden no incluir el nombre
3. El formato de las notificaciones puede variar según la versión de Yape

### El servicio se detiene después de un tiempo

1. **Desactivar optimización de batería**:
   - Abre la app y toca "Desactivar Optimización de Batería"
   - O ve a Configuración > Batería > Optimización de batería
   - Busca "Yape Notification Reader" y selecciona "No optimizar"

2. **Verificar modo de ahorro de energía**:
   - Algunos fabricantes (Xiaomi, Huawei, etc.) tienen modos agresivos de ahorro
   - Busca "App startup" o "Inicio automático" en configuración
   - Permite que Yape Notification Reader se inicie automáticamente

3. **Verificar la notificación persistente**:
   - Debe aparecer una notificación "Yape Reader activo"
   - Si no aparece, el servicio no está en foreground
   - Intenta desinstalar y reinstalar la app

## 📝 Notas importantes

- La aplicación depende del formato de las notificaciones de Yape
- Si Yape actualiza el formato de sus notificaciones, puede ser necesario actualizar la app
- La precisión de la detección del nombre depende del formato de la notificación

## 🤝 Contribuciones

Si encuentras algún problema o tienes sugerencias:
1. Verifica que el formato de las notificaciones de Yape no haya cambiado
2. Revisa los logs de Android para más información
3. Reporta el problema con capturas de pantalla de las notificaciones

## 📄 Licencia

Este proyecto es de código abierto y está disponible para uso personal y educativo.

## ⚠️ Disclaimer

Esta aplicación no está afiliada ni respaldada por Yape o BCP. Es un proyecto independiente creado para mejorar la accesibilidad de las notificaciones de pago.

---

**Versión**: 1.0
**Última actualización**: 2025
**Requisitos mínimos**: Android 5.0 (API 21)
