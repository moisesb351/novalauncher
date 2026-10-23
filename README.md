# Nova Launcher (Android TV)

Launcher minimalista y futurista para Android TV. Kotlin + Jetpack Compose.

## Qué incluye (fase 1)

- **Inicio**: reloj, saludo, fila de apps con tarjeta enfocada que crece y brilla, accesos rápidos (Ajustes, Red, Personalizar).
- **Todas las apps**: cuadrícula con todas las apps instaladas (se actualiza al volver al launcher).
- **Personalizar**: 3 fondos (Aurora, Carbón, Horizonte) y 4 colores de acento. Se guardan solos.
- **Gemini**: el botón de arriba a la derecha abre el asistente de voz que tenga tu TV (Gemini o Google Assistant).
- Botón Atrás y botón Inicio vuelven a la pantalla principal.

## Requisitos

- Android Studio Koala (2024.1.1) o más nuevo, con JDK 17 (viene incluido).
- TV con Android TV 8.0 o superior.

## Compilar

1. Android Studio > File > Open > carpeta `NovaLauncher`. Espera a que Gradle sincronice.
2. Build > Build App Bundle(s) / APK(s) > Build APK(s).
3. El APK queda en `app/build/outputs/apk/debug/app-debug.apk`.

## Instalar en la TV

1. En la TV: Ajustes > Acerca de > pulsa 7 veces en "Compilación". Luego, en Opciones de desarrollador, activa Depuración USB y Depuración ADB/por red.
2. Con TV y PC en la misma red:

```
adb connect IP_DE_LA_TV:5555
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## Probar sin riesgo

```
adb shell am start -n com.nova.launcher/.MainActivity
```

## Dejarlo como inicio (solo después de probarlo)

```
adb shell pm list packages | grep -i launcher
adb shell pm disable-user --user 0 NOMBRE_DEL_PAQUETE
```

Paquetes habituales: `com.google.android.tvlauncher` y `com.google.android.apps.tv.launcherx`.

Para volver atrás:

```
adb shell pm enable NOMBRE_DEL_PAQUETE
```

## Notas

- Incluye el wrapper de Gradle (8.7): Android Studio lo usa solo, o desde terminal `./gradlew assembleDebug`.
- La fuente es la del sistema. Se puede añadir Sora (u otra) en `res/font` en una fase siguiente.
- Pendiente para la siguiente fase: fijar apps favoritas en Inicio, estilos de iconos, disposición en cuadrícula y asistente propio con la API de Gemini.
