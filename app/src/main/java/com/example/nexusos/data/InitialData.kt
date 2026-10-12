package com.example.nexusos.data

import com.example.nexusos.model.*

object InitialData {
    val apks: List<ApkProject> = listOf(
        ApkProject(
            id = "apk-shield-security",
            appName = "Shield Security",
            packageName = "com.shield.security",
            version = "2.26.8",
            description = "Android 15 Shield Security System with user auth, email captcha verification, and V2/V3 APK signature validation.",
            category = "Security & Tools",
            kotlinCode = """
package com.shield.security

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        val etUser = findViewById<EditText>(R.id.etCuenta)
        val etPass = findViewById<EditText>(R.id.etContrasena)
        val etBuzon = findViewById<EditText>(R.id.etBuzon)
        val etCaptcha = findViewById<EditText>(R.id.etCaptcha)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {
            val user = etUser.text.toString()
            val captcha = etCaptcha.text.toString()

            if (user.isNotEmpty() && captcha == "663225") {
                Toast.makeText(this, "Regístrese para obtener una cuenta... ¡Registro exitoso!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Por favor verifique los datos o el captcha", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
            """.trimIndent(),
            xmlLayout = """
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:background="#1a1d24"
    android:padding="20dp">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Inscribirse"
        android:textSize="20sp"
        android:textColor="#FFFFFF"
        android:textStyle="bold" />

    <EditText
        android:id="@+id/etCuenta"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Cuenta (ej: wexicos)"
        android:textColor="#FFFFFF" />

    <EditText
        android:id="@+id/etCaptcha"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="Captcha (663225)"
        android:textColor="#FFFFFF" />

    <Button
        android:id="@+id/btnRegister"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="20dp"
        android:text="Regístrese para obtener una cuenta..."
        android:backgroundTint="#333333" />
</LinearLayout>
            """.trimIndent(),
            manifest = """
<manifest xmlns:android="http://schemas.android.com/apk/res/android" package="com.shield.security">
    <uses-permission android:name="android.permission.INTERNET" />
    <application android:label="Shield Security">
        <activity android:name=".MainActivity" android:exported="true"/>
    </application>
</manifest>
            """.trimIndent(),
            buildStatus = "compiled"
        ),
        ApkProject(
            id = "apk-1",
            appName = "FitPulse AI",
            packageName = "com.nexus.fitpulse",
            version = "1.0.0",
            description = "AI-powered personal trainer and workout generator for Android 15.",
            category = "Health & Fitness",
            kotlinCode = """
package com.nexus.fitpulse

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        val titleText = findViewById<TextView>(R.id.titleText)
        titleText.text = "FitPulse AI: Ready for Today's Workout!"
    }
}
            """.trimIndent(),
            xmlLayout = """
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:gravity="center"
    android:padding="24dp"
    android:background="#121212">

    <TextView
        android:id="@+id/titleText"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="FitPulse AI"
        android:textSize="24sp"
        android:textColor="#10B981" />
</LinearLayout>
            """.trimIndent(),
            manifest = """
<manifest xmlns:android="http://schemas.android.com/apk/res/android" package="com.nexus.fitpulse">
    <application android:label="FitPulse AI">
        <activity android:name=".MainActivity" android:exported="true"/>
    </application>
</manifest>
            """.trimIndent(),
            buildStatus = "compiled"
        )
    )

    val webs: List<WebProject> = listOf(
        WebProject(
            id = "web-1",
            title = "CyberCafe & Roastery",
            prompt = "A futuristic cyber-punk coffee shop landing page with glowing neon accents and menu ordering.",
            htmlCode = """
<div class="cyber-container">
  <h1>NEXUS BREW ☕</h1>
  <p>Coffee from the year 2077</p>
  <button onclick="alert('Ordered Neural Espresso!')">Order Neural Espresso ($4.50)</button>
</div>
            """.trimIndent(),
            cssCode = "body { background: #090d16; color: #00ffcc; font-family: sans-serif; text-align: center; padding: 2rem; } button { background: #00ffcc; color: #000; padding: 12px 24px; border-radius: 8px; font-weight: bold; }",
            jsCode = "console.log('Nexus Brew loaded in Android 15 WebForge Engine.');"
        )
    )

    val images: List<ImageItem> = listOf(
        ImageItem(
            id = "img-1",
            title = "Neon Cyber Cityscape",
            prompt = "A breathtaking cyber-punk Tokyo street at night in rain with glowing holographic neon signs, 8k resolution, cinematic lighting.",
            url = "https://picsum.photos/seed/cybercity/800/600",
            style = "Cyberpunk",
            aspectRatio = "16:9"
        ),
        ImageItem(
            id = "img-2",
            title = "Zen Alpine Retreat",
            prompt = "A minimalist wooden cabin nestled in snowy mountains during golden hour, serene and photorealistic.",
            url = "https://picsum.photos/seed/mountainzen/800/600",
            style = "Photorealistic",
            aspectRatio = "16:9"
        )
    )

    val videos: List<VideoItem> = listOf(
        VideoItem(
            id = "vid-1",
            title = "Quantum Drive Cinematic",
            prompt = "A sleek spacecraft traveling through a glowing hyper-space wormhole with particle effects.",
            duration = "0:07",
            resolution = "1080p",
            thumbnailUrl = "https://picsum.photos/seed/spaceship/800/450",
            status = "completed"
        )
    )

    val docs: List<DocumentItem> = listOf(
        DocumentItem(
            id = "doc-1",
            name = "project_specifications.md",
            type = "markdown",
            content = """
# NexusOS 15 AI Studio - System Specification

## Overview
NexusOS 15 is an advanced Android 15 simulator with fully integrated AI generation for APKs, websites, images, videos, and multi-format document editing.

## Features
- **AI APK Builder**: Generates functional Android project source files and previews them in a virtual device.
- **WebForge Engine**: Generates responsive web applications from natural language prompts.
- **Nexus Canvas & CineGen**: Generates and edits high-fidelity images and cinematic videos.
- **DocMaster**: Modifies and analyzes any document or code file.
            """.trimIndent(),
            size = "1.4 KB"
        ),
        DocumentItem(
            id = "doc-2",
            name = "config_manifest.json",
            type = "json",
            content = """
{
  "os": "Android 15 (Nexus Edition)",
  "aiEngine": "Gemini 3.6 Flash & Imagen 3",
  "securityLevel": "Enhanced Sandbox",
  "developerMode": true
}
            """.trimIndent(),
            size = "320 B"
        )
    )

    val messages: List<ChatMessage> = listOf(
        ChatMessage(
            id = "msg-1",
            sender = "gemini",
            text = "Hello! I am your Gemini AI OS Assistant built into Android 15. I detected an incoming crash log (java.io.IOException: Archive is not a ZIP archive on moto g54 5G). You can analyze it in the Crash Analyzer or ask me for fix options!",
            actionCard = ActionCard(
                type = "apk",
                title = "Analyze Moto G54 Crash Log",
                targetScreen = ScreenId.CRASH_ANALYZER
            )
        )
    )

    val crashLogs: List<CrashLog> = listOf(
        CrashLog(
            id = "crash-1",
            ver = "2.26.8(26080495)",
            permission = "APP",
            abi = "arm64-v8a",
            model = "moto g54 5G",
            sdk = "35",
            release = "15",
            targetSdk = "30",
            exceptionType = "java.io.IOException",
            exceptionMessage = "Archive is not a ZIP archive",
            stackTrace = """
VER: 2.26.8(26080495)
PERMISSION: APP
ABI: arm64-v8a
MODEL: moto g54 5G
SDK: 35
RELEASE: 15
TARGET_SDK: 30
---StackTrace---
java.io.IOException: Archive is not a ZIP archive
	at l.ۘۧ᩶.۟(75XQ:1093)
	at l.ۘۧ᩶.<init>(75XQ:401)
	at l.ۘۧ᩶.<init>(75XQ:189)
	at l.ۘۧ᩶.<init>(75XQ:178)
	at l.ۘۧ᩶.<init>(75XQ:154)
	at l.᩺᩹ۧ.۟(EAPS:45)
	at l.۬ۨܳ.ܿ(P5ZJ:172)
	at l.ܺۙܳ.۟(15ZR:305)
	at l.ۛۨܳ.۟(P5ZJ:0)
	at l.ܽܰ֡.run(U6B5:797)
	at l.ܺᩳ۬.ۧ(S1ZX:546)
	at l.ܰۢܰ.run(E4WB:140)
	at android.os.Handler.handleCallback(Handler.java:1001)
	at android.os.Handler.dispatchMessage(Handler.java:105)
	at android.os.Looper.loopOnce(Looper.java:268)
	at android.os.Looper.loop(Looper.java:384)
	at android.app.ActivityThread.main(ActivityThread.java:8936)
	at java.lang.reflect.Method.invoke(Native Method)
	at com.android.internal.os.RuntimeInit${'$'}MethodAndArgsCaller.run(RuntimeInit.java:580)
	at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:907)
            """.trimIndent()
        )
    )
}
