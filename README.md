# NexusOS 15 AI Studio (Android Application)

Rewritten from the original React application into a modern Android application using **Kotlin**, **Jetpack Compose**, and **Material Design 3**.

## Architecture & Features Ported

1. **Android 15 Kernel & Launcher (`HomeScreen.kt`)**:
   - Quick glance widget with system diagnostics, SDK 35 specifications, and real-time alerts.
   - App grid featuring all 9 core system modules:
     - APK Studio AI 15
     - Crash Inspector (Moto G54 Diagnostic)
     - WebForge AI
     - Nexus Canvas (Imagen 3 Studio)
     - CineGen AI (Veo 3.1 Studio)
     - DocMaster AI
     - Gemini Copilot
     - Storage & APKs Manager
     - Android 15 Settings
   - Bottom dock and Material navigation.

2. **APK Studio AI 15 (`ApkStudioScreen.kt`)**:
   - Prompt-driven Android APK generation.
   - Live interactive device sandbox.
   - Play Console export inspector (RSA-2048 signing, Scheme v2/v3/v4 validation, 4-byte zipalign check, AAB bundles).
   - Code inspector tabs for Kotlin source (`MainActivity.kt`), XML layout (`activity_main.xml`), and `AndroidManifest.xml`.

3. **Crash Inspector & 1-Click APK Repair (`CrashAnalyzerScreen.kt`)**:
   - Android 15 stack trace analyzer parsing `java.io.IOException: Archive is not a ZIP archive` on target devices.
   - Root cause diagnostic (ZIP magic bytes `PK\x03\x04` validation).
   - 1-click "Repair APK (Darkia Fixed)" action that compiles a fixed, signed APK and launches APK Studio.

4. **WebForge AI (`WebForgeScreen.kt`)**:
   - Prompt-to-web generator with responsive viewport simulator (Desktop & Mobile).
   - Code viewer for HTML/CSS.

5. **Nexus Canvas AI (`ImageStudioScreen.kt`)**:
   - Imagen 3 generation with style picker (Photorealistic, Cyberpunk, 3D Render, Anime, Oil Painting) and aspect ratios.
   - Image editor with AI filters (Normal, B&W, Sepia, HDR+ AI) and retouching.

6. **CineGen AI 15 (`VideoSuiteScreen.kt`)**:
   - Veo 3.1 cinematic video studio with 1080p/720p resolutions and aspect ratios.
   - Video player with play/pause simulation and MP4 export.

7. **DocMaster AI (`DocEditorScreen.kt`)**:
   - Multi-document code & markdown editor.
   - Gemini AI refactoring bar for translating, formatting, and summarizing content.

8. **Gemini Assistant Copilot (`GeminiAssistantScreen.kt`)**:
   - Context-aware conversation assistant with interactive action cards that navigate to specific system tools.

9. **Files & Storage Manager (`FileExplorerScreen.kt`)**:
   - Storage manager organizing Play Store AAB bundles, release APKs, web projects, and document assets.

10. **Android 15 Settings (`SettingsScreen.kt`)**:
    - Material You dynamic theme accents (Emerald, Lavender, Ocean Blue, Sunset Amber, Obsidian Dark).
    - AI Neural Engine telemetry and system specifications.

## Technology Stack
- **Language**: Kotlin 2.0
- **UI Framework**: Jetpack Compose (Material Design 3)
- **Architecture**: MVVM with `NexusOsViewModel` and unidirectional state flow (`StateFlow`)
- **Build System**: Gradle Kotlin DSL (`build.gradle.kts`, `settings.gradle.kts`, `gradle/libs.versions.toml`)
- **Compatibility**: Android 8.0+ (minSdk 26, targetSdk 35)
