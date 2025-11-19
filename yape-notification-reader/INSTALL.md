# Guía de Instalación - Yape Notification Reader

Esta guía te ayudará a instalar y configurar Yape Notification Reader en tu dispositivo Android.

## 📋 Requisitos previos

Antes de comenzar, asegúrate de tener:

- ✅ **Android 5.0 (Lollipop) o superior**
- ✅ **Aplicación de Yape instalada** en tu dispositivo
- ✅ **Android Studio** (si vas a compilar desde el código fuente)
- ✅ **Conexión a internet** (para descargar dependencias durante la compilación)

## 🔧 Opción 1: Instalar desde Android Studio

### Paso 1: Abrir el proyecto

1. Abre **Android Studio**
2. Selecciona **File** > **Open**
3. Navega a la carpeta `yape-notification-reader`
4. Haz clic en **OK**

### Paso 2: Sincronizar Gradle

1. Android Studio automáticamente comenzará a sincronizar el proyecto
2. Espera a que se descarguen todas las dependencias
3. Si aparecen errores, haz clic en **File** > **Sync Project with Gradle Files**

### Paso 3: Conectar tu dispositivo

**Opción A: Dispositivo físico**
1. Habilita las **Opciones de desarrollador** en tu dispositivo:
   - Ve a **Configuración** > **Acerca del teléfono**
   - Toca **Número de compilación** 7 veces
2. Habilita la **Depuración USB**:
   - Ve a **Configuración** > **Opciones de desarrollador**
   - Activa **Depuración USB**
3. Conecta tu dispositivo a la computadora con un cable USB
4. Acepta el mensaje de autorización en tu dispositivo

**Opción B: Emulador**
1. En Android Studio, haz clic en **Tools** > **AVD Manager**
2. Crea un nuevo dispositivo virtual con Android 5.0 o superior
3. Inicia el emulador

### Paso 4: Instalar la aplicación

1. En Android Studio, selecciona tu dispositivo en la barra superior
2. Haz clic en el botón **Run** (▶️) o presiona **Shift + F10**
3. La aplicación se instalará automáticamente en tu dispositivo

## 🛠️ Opción 2: Compilar APK manualmente

### Usando Gradle (Linux/Mac)

```bash
cd yape-notification-reader
chmod +x gradlew
./gradlew assembleDebug
```

### Usando Gradle (Windows)

```cmd
cd yape-notification-reader
gradlew.bat assembleDebug
```

El APK se generará en: `app/build/outputs/apk/debug/app-debug.apk`

### Instalar el APK

**Método 1: Usando ADB**
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

**Método 2: Transferencia manual**
1. Copia el archivo `app-debug.apk` a tu dispositivo
2. Abre el archivo en tu dispositivo
3. Permite la instalación de aplicaciones de fuentes desconocidas si se solicita
4. Toca **Instalar**

## ⚙️ Configuración inicial

### Paso 1: Abrir la aplicación

1. Busca el ícono de **Yape Notification Reader** en tu dispositivo
2. Toca para abrir la aplicación

### Paso 2: Conceder permisos

1. La aplicación mostrará un mensaje sobre permisos necesarios
2. Toca **"Abrir Configuración de Notificaciones"**
3. En la pantalla de configuración del sistema:
   - Busca **"Yape Notification Reader"** en la lista
   - Toca para seleccionarla
   - Activa el interruptor de **"Permitir acceso a notificaciones"**
4. Confirma el permiso si se solicita
5. Presiona el botón **Atrás** para volver a la aplicación

### Paso 3: Verificar el estado

1. En la pantalla principal de la aplicación, deberías ver:
   - ✅ **"Permiso de notificaciones concedido"** en verde
2. Si aparece en rojo, repite el Paso 2

### Paso 4: Configurar preferencias

**Activar/Desactivar el servicio:**
- Usa el interruptor **"Activar servicio"**
- Cuando está activado, la app narrará los pagos recibidos
- Cuando está desactivado, no habrá narración

**Configurar lectura del nombre:**
- Usa el interruptor **"Leer nombre del remitente"**
- **Activado**: Escucharás "Recibiste 50 soles de Juan Pérez"
- **Desactivado**: Escucharás "Recibiste 50 soles"

## 🎤 Configurar Text-to-Speech (Opcional)

Para una mejor experiencia de narración en español:

### Paso 1: Instalar Google Text-to-Speech

1. Abre **Google Play Store**
2. Busca **"Google Text-to-Speech"**
3. Instala la aplicación

### Paso 2: Configurar el motor de voz

1. Ve a **Configuración** del dispositivo
2. Busca **Idioma y entrada** (o **Sistema** > **Idiomas y entrada**)
3. Toca **Text-to-Speech** (o **Salida de texto a voz**)
4. Selecciona **Motor preferido**: **Google Text-to-Speech Engine**
5. Toca el ícono de **Configuración** ⚙️ junto al motor
6. Selecciona **Instalar datos de voz**
7. Descarga **Español (España)** o **Español (Estados Unidos)**

### Paso 3: Probar la voz

1. En la configuración de Text-to-Speech
2. Toca **"Escuchar un ejemplo"**
3. Deberías escuchar una narración en español

## ✅ Verificar funcionamiento

### Prueba de funcionamiento

1. Asegúrate de tener **Yape instalado**
2. Configura **Yape Notification Reader** según las preferencias
3. Pídele a un amigo que te haga un pago de prueba por Yape
4. Cuando recibas la notificación, deberías escuchar la narración

### Si no funciona

**Verifica lo siguiente:**

1. **Permisos de notificación:**
   - Configuración > Aplicaciones > Yape Notification Reader
   - Permisos > Notificaciones > Permitido

2. **Servicio activo:**
   - Abre Yape Notification Reader
   - Verifica que "Activar servicio" esté encendido

3. **Volumen del dispositivo:**
   - Asegúrate de que el volumen multimedia esté alto

4. **TTS configurado:**
   - Configuración > Idioma y entrada > Text-to-Speech
   - Verifica que haya un motor instalado

## 🔄 Actualización

Para actualizar a una versión más reciente:

1. Descarga o compila el nuevo APK
2. Instálalo sobre la versión anterior
3. Tus configuraciones se mantendrán

## 🗑️ Desinstalación

Para desinstalar la aplicación:

1. Ve a **Configuración** > **Aplicaciones**
2. Busca **Yape Notification Reader**
3. Toca **Desinstalar**
4. Confirma la desinstalación

**Nota:** El permiso de acceso a notificaciones se revocará automáticamente.

## 🆘 Solución de problemas

### Error: "No se puede instalar la aplicación"

**Solución:**
1. Ve a **Configuración** > **Seguridad**
2. Activa **"Fuentes desconocidas"** o **"Instalar aplicaciones desconocidas"**
3. Intenta instalar nuevamente

### Error durante la compilación

**Solución:**
1. Verifica que tengas **JDK 8 o superior** instalado
2. En Android Studio: **File** > **Invalidate Caches / Restart**
3. Limpia el proyecto: **Build** > **Clean Project**
4. Reconstruye: **Build** > **Rebuild Project**

### La aplicación se cierra inesperadamente

**Solución:**
1. Verifica los logs de Android:
   ```bash
   adb logcat | grep "YapeNotificationListener"
   ```
2. Reinstala la aplicación
3. Verifica que tu versión de Android sea compatible (5.0+)

## 📞 Soporte

Si tienes problemas durante la instalación:

1. Revisa la sección de **Solución de problemas** arriba
2. Consulta el archivo **README.md** para más información
3. Verifica que cumples con todos los requisitos previos

---

**¡Listo!** Ahora deberías tener Yape Notification Reader instalado y funcionando correctamente en tu dispositivo.
