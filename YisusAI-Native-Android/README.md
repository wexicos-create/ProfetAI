# Yisus AI — Android nativo (Kotlin)

Interfaz de chat Android nativa. No genera respuestas simuladas: utiliza el endpoint HTTPS `/api/chat` configurado por el usuario. Las claves del proveedor deben permanecer en el backend.

## Requisitos
- JDK 17
- Android SDK Platform 35
- Gradle 8.9
- Android Gradle Plugin 8.7.3

## Compilar
```bash
gradle assembleDebug
```
APK de prueba: `app/build/outputs/apk/debug/app-debug.apk`. Gradle firma el APK debug con la clave de depuración del entorno de compilación. Para distribución pública se requiere una clave privada de publicación.

## IA real
Configura una URL HTTPS que termine en `/api/chat`. El endpoint debe aceptar JSON con `message` y `history`, y devolver JSON con `answer`. Si el servicio falla, la aplicación muestra el error real en vez de inventar una respuesta.

## Plataforma
- Android 10+ (minSdk 29), target SDK 35.
- Interfaz nativa Kotlin, gris oscuro y verde neón.
- No incluye claves de proveedor ni almacena keystores.