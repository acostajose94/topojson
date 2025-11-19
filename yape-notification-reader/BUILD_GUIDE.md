# Guía de Compilación - Yape Notification Reader

Esta guía te ayudará a compilar correctamente la aplicación Android en tu entorno de desarrollo.

## 📋 Requisitos previos

### Versiones necesarias

- **Java Development Kit (JDK)**: Java 17 o Java 21
- **Android Studio**: Arctic Fox (2020.3.1) o superior
- **Android SDK**: API 34 o superior
- **Gradle**: 8.9 (se descarga automáticamente con el wrapper)

### Verificar versión de Java

Para verificar qué versión de Java tienes instalada:

**Windows:**
```cmd
java -version
```

**Linux/Mac:**
```bash
java -version
```

Deberías ver algo como:
```
openjdk version "17.0.x" o "21.0.x"
```

## 🔧 Configuración del proyecto

El proyecto está configurado para:
- **compileSdk**: 34
- **minSdk**: 21 (Android 5.0 Lollipop)
- **targetSdk**: 34 (Android 14)
- **Java**: 17 (compatible con Java 21)
- **Gradle**: 8.9
- **Android Gradle Plugin**: 8.5.2

## 🚀 Compilar el proyecto

### Método 1: Desde Android Studio

1. Abre Android Studio
2. Selecciona **File** > **Open**
3. Navega a la carpeta `yape-notification-reader` y ábrela
4. Espera a que Gradle sincronice (puede tardar unos minutos la primera vez)
5. Una vez sincronizado, selecciona **Build** > **Build Bundle(s) / APK(s)** > **Build APK(s)**
6. El APK se generará en: `app/build/outputs/apk/debug/app-debug.apk`

### Método 2: Desde línea de comandos

#### Windows (PowerShell o CMD)

```cmd
cd yape-notification-reader
.\gradlew assembleDebug
```

#### Linux/Mac

```bash
cd yape-notification-reader
chmod +x gradlew
./gradlew assembleDebug
```

El APK compilado estará en: `app/build/outputs/apk/debug/app-debug.apk`

## 🐛 Solución de problemas

### Error: "Unsupported class file major version 69"

**Causa:** Estás usando Java 21 pero el proyecto necesita Java 17, o viceversa.

**Solución 1 - Usar Java 17 (Recomendado):**

1. Descarga e instala JDK 17 desde:
   - [Oracle JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
   - [OpenJDK 17](https://adoptium.net/temurin/releases/?version=17)

2. Configura la variable de entorno JAVA_HOME:

   **Windows:**
   ```cmd
   setx JAVA_HOME "C:\Program Files\Java\jdk-17"
   ```

   **Linux/Mac:**
   ```bash
   export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
   ```

3. Verifica la instalación:
   ```cmd
   java -version
   ```

**Solución 2 - Configurar Android Studio:**

1. Abre Android Studio
2. Ve a **File** > **Settings** (o **Android Studio** > **Preferences** en Mac)
3. Navega a **Build, Execution, Deployment** > **Build Tools** > **Gradle**
4. En "Gradle JDK", selecciona Java 17
5. Haz clic en **OK** y sincroniza el proyecto

### Error: "SDK location not found"

**Causa:** Android SDK no está configurado.

**Solución:**

Crea un archivo `local.properties` en la raíz del proyecto:

**Windows:**
```properties
sdk.dir=C\:\\Users\\TuUsuario\\AppData\\Local\\Android\\Sdk
```

**Linux:**
```properties
sdk.dir=/home/tuusuario/Android/Sdk
```

**Mac:**
```properties
sdk.dir=/Users/tuusuario/Library/Android/sdk
```

### Error: "Could not download gradle-X.X-bin.zip"

**Causa:** Problemas de conexión o proxy.

**Solución:**

1. Verifica tu conexión a Internet
2. Si estás detrás de un proxy, configúralo en `gradle.properties`:

```properties
systemProp.http.proxyHost=proxy.company.com
systemProp.http.proxyPort=8080
systemProp.https.proxyHost=proxy.company.com
systemProp.https.proxyPort=8080
```

### Error: "Execution failed for task ':app:mergeDebugResources'"

**Causa:** Recursos duplicados o conflictos.

**Solución:**

1. Limpia el proyecto:
   ```bash
   ./gradlew clean
   ```

2. Recompila:
   ```bash
   ./gradlew assembleDebug
   ```

### Error de memoria: "Out of memory"

**Causa:** Gradle no tiene suficiente memoria asignada.

**Solución:**

Edita o crea el archivo `gradle.properties` en la raíz del proyecto:

```properties
org.gradle.jvmargs=-Xmx4096m -XX:MaxMetaspaceSize=512m
```

### Warnings sobre "restricted methods"

Estos warnings son normales en Windows con Java 21 y se pueden ignorar. No afectan la compilación.

## 🔄 Limpiar y reconstruir

Si tienes problemas persistentes, intenta limpiar y reconstruir:

```bash
# Limpiar
./gradlew clean

# Reconstruir
./gradlew build

# O en un solo comando
./gradlew clean build
```

## 📦 Generar APK de release (firmado)

Para generar un APK optimizado para distribución:

1. Crea un keystore:
   ```bash
   keytool -genkey -v -keystore yape-reader.keystore -alias yape-reader -keyalg RSA -keysize 2048 -validity 10000
   ```

2. Configura el signing en `app/build.gradle`:
   ```gradle
   android {
       signingConfigs {
           release {
               storeFile file("../yape-reader.keystore")
               storePassword "tu-password"
               keyAlias "yape-reader"
               keyPassword "tu-password"
           }
       }

       buildTypes {
           release {
               signingConfig signingConfigs.release
               minifyEnabled true
               shrinkResources true
               proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
           }
       }
   }
   ```

3. Compila:
   ```bash
   ./gradlew assembleRelease
   ```

## 📊 Verificar el APK generado

Después de compilar, puedes verificar la información del APK:

```bash
# Ver información del APK
./gradlew app:assembleDebug --info

# Analizar el tamaño del APK
./gradlew app:assembleDebug --scan
```

## 🔍 Logs de compilación

Para ver logs más detallados si hay errores:

```bash
# Logs detallados
./gradlew assembleDebug --info

# Logs de debug
./gradlew assembleDebug --debug

# Stack trace completo
./gradlew assembleDebug --stacktrace
```

## 💡 Tips adicionales

1. **Primera compilación lenta**: La primera compilación puede tardar varios minutos mientras Gradle descarga todas las dependencias.

2. **Modo offline**: Si ya descargaste las dependencias, puedes compilar offline:
   ```bash
   ./gradlew assembleDebug --offline
   ```

3. **Caché de Gradle**: Si tienes problemas con el caché:
   ```bash
   # Limpiar caché de Gradle
   ./gradlew cleanBuildCache
   ```

4. **Compilación paralela**: Para compilar más rápido en máquinas con múltiples núcleos, agrega a `gradle.properties`:
   ```properties
   org.gradle.parallel=true
   org.gradle.configureondemand=true
   ```

## 🆘 Soporte adicional

Si sigues teniendo problemas:

1. Verifica que tienes las últimas versiones de:
   - Android Studio
   - Android SDK Platform-Tools
   - Android SDK Build-Tools

2. Revisa los logs de Gradle con `--stacktrace`

3. Consulta la documentación oficial de Android:
   - [Android Developer - Build your app](https://developer.android.com/studio/build)
   - [Gradle User Guide](https://docs.gradle.org/current/userguide/userguide.html)

---

**Versiones utilizadas en este proyecto:**
- Gradle: 8.9
- Android Gradle Plugin: 8.5.2
- Java: 17 (compatible con 21)
- Compile SDK: 34
- Min SDK: 21
- Target SDK: 34
