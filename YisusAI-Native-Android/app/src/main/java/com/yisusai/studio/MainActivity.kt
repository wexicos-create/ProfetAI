package com.yisusai.studio

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.WindowManager
import android.view.View
import android.view.inputmethod.EditorInfo
import android.content.Context
import android.content.SharedPreferences
import android.widget.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.concurrent.Executors

class MainActivity : Activity() {
    private val bg = Color.rgb(13, 17, 16)
    private val panel = Color.rgb(23, 29, 27)
    private val neon = Color.rgb(57, 255, 136)
    private val white = Color.rgb(238, 244, 240)
    private val muted = Color.rgb(157, 173, 164)
    private val executor = Executors.newSingleThreadExecutor()
    private lateinit var prefs: SharedPreferences
    private lateinit var endpointInput: EditText
    private lateinit var messageInput: EditText
    private lateinit var messages: LinearLayout
    private lateinit var scroll: ScrollView
    private lateinit var sendButton: Button
    private var history = JSONArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg
        window.decorView.systemUiVisibility = 0
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        prefs = getSharedPreferences("yisus", Context.MODE_PRIVATE)
        buildUi()
        addMessage("Yisus AI", "Soy Yisus AI. Para responder con un modelo real, configura la URL HTTPS de tu endpoint /api/chat. No inventaré respuestas si el servicio falla.", false)
    }

    private fun buildUi() {
        val root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setBackgroundColor(bg); setPadding(dp(18), dp(10), dp(18), dp(12)) }
        setContentView(root)
        val header = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL; setPadding(0, dp(8), 0, dp(16)) }
        val brand = TextView(this).apply { text = "✳  YISUS AI"; textSize = 23f; setTextColor(neon); typeface = Typeface.DEFAULT_BOLD }
        header.addView(brand, LinearLayout.LayoutParams(0, -2, 1f))
        val state = TextView(this).apply { text = "● STUDIO"; textSize = 11f; setTextColor(neon); setPadding(dp(10), dp(7), dp(10), dp(7)); background = rounded(panel, 16) }
        header.addView(state)
        root.addView(header)

        root.addView(label("ENDPOINT DE IA · HTTPS"))
        endpointInput = EditText(this).apply {
            setSingleLine(true); textSize = 13f; setTextColor(white); setHintTextColor(muted); hint = "https://tu-dominio.vercel.app/api/chat"
            setPadding(dp(12), dp(10), dp(12), dp(10)); background = rounded(panel, 12)
            setText(prefs.getString("endpoint", "") ?: "")
            imeOptions = EditorInfo.IME_ACTION_DONE
        }
        root.addView(endpointInput, LinearLayout.LayoutParams(-1, dp(48)).apply { bottomMargin = dp(12) })

        scroll = ScrollView(this).apply { isFillViewport = false; clipToPadding = false; setPadding(0, 0, 0, dp(8)) }
        messages = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        scroll.addView(messages)
        root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))

        val divider = View(this).apply { setBackgroundColor(Color.rgb(38, 50, 44)) }
        root.addView(divider, LinearLayout.LayoutParams(-1, dp(1)).apply { topMargin = dp(8); bottomMargin = dp(10) })
        val row = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL }
        messageInput = EditText(this).apply {
            hint = "Escribe a Yisus…"; setTextColor(white); setHintTextColor(muted); textSize = 15f
            setPadding(dp(14), dp(10), dp(14), dp(10)); background = rounded(panel, 16)
            minLines = 1; maxLines = 4; imeOptions = EditorInfo.IME_ACTION_SEND
            setOnEditorActionListener { _, actionId, _ -> if (actionId == EditorInfo.IME_ACTION_SEND) { send(); true } else false }
        }
        row.addView(messageInput, LinearLayout.LayoutParams(0, -2, 1f).apply { rightMargin = dp(8) })
        sendButton = Button(this).apply { text = "Enviar"; setTextColor(Color.rgb(7, 24, 14)); textSize = 13f; typeface = Typeface.DEFAULT_BOLD; background = rounded(neon, 14); isAllCaps = false; setPadding(dp(8), 0, dp(8), 0); setOnClickListener { send() } }
        row.addView(sendButton, LinearLayout.LayoutParams(dp(84), dp(48)))
        root.addView(row)
        val footer = TextView(this).apply { text = "Las respuestas dependen de tu proveedor y conexión. Las claves deben permanecer en el servidor."; textSize = 10f; setTextColor(muted); gravity = Gravity.CENTER; setPadding(0, dp(10), 0, 0) }
        root.addView(footer)
    }

    private fun send() {
        val text = messageInput.text.toString().trim()
        val endpoint = endpointInput.text.toString().trim()
        if (text.isEmpty()) return
        if (!endpoint.startsWith("https://") || !endpoint.endsWith("/api/chat")) {
            addMessage("Sistema", "Configura una URL HTTPS válida que termine en /api/chat. No se enviaron datos.", false); return
        }
        prefs.edit().putString("endpoint", endpoint).apply()
        messageInput.setText("")
        addMessage("Tú", text, true)
        sendButton.isEnabled = false; sendButton.text = "…"
        val historySnapshot = JSONArray(history.toString())
        executor.execute {
            try {
                val conn = (URL(endpoint).openConnection() as HttpURLConnection).apply {
                    requestMethod = "POST"; connectTimeout = 15000; readTimeout = 60000; doOutput = true
                    setRequestProperty("Content-Type", "application/json; charset=utf-8")
                    setRequestProperty("Accept", "application/json")
                }
                val body = JSONObject().put("message", text).put("history", historySnapshot).toString()
                conn.outputStream.use { it.write(body.toByteArray(StandardCharsets.UTF_8)) }
                val code = conn.responseCode
                val stream = if (code in 200..299) conn.inputStream else conn.errorStream
                val response = stream?.bufferedReader()?.use { it.readText() } ?: "{}"
                conn.disconnect()
                val json = JSONObject(response)
                if (code !in 200..299) throw IllegalStateException(json.optString("error", "El servidor respondió HTTP $code"))
                val answer = json.optString("answer", "").trim()
                if (answer.isEmpty()) throw IllegalStateException("El proveedor de IA respondió sin texto.")
                history.put(JSONObject().put("role", "user").put("content", text))
                history.put(JSONObject().put("role", "assistant").put("content", answer))
                runOnUiThread { addMessage("Yisus AI", answer, false); setBusy(false) }
            } catch (e: Exception) {
                runOnUiThread { addMessage("Error real de conexión", e.message ?: "No se pudo conectar con el endpoint.", false); setBusy(false) }
            }
        }
    }

    private fun setBusy(busy: Boolean) { sendButton.isEnabled = !busy; sendButton.text = if (busy) "…" else "Enviar" }

    private fun addMessage(who: String, body: String, user: Boolean) {
        val card = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(dp(14), dp(11), dp(14), dp(11)); background = rounded(if (user) Color.rgb(24, 52, 38) else panel, 16) }
        val title = TextView(this).apply { text = who; textSize = 12f; typeface = Typeface.DEFAULT_BOLD; setTextColor(if (user) neon else muted) }
        val text = TextView(this).apply { this.text = body; textSize = 15f; setTextColor(white); setPadding(0, dp(5), 0, 0); setTextIsSelectable(true) }
        card.addView(title); card.addView(text)
        messages.addView(card, LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(10) })
        scroll.post { scroll.fullScroll(View.FOCUS_DOWN) }
    }

    private fun label(value: String) = TextView(this).apply { text = value; textSize = 10f; typeface = Typeface.DEFAULT_BOLD; setTextColor(muted); setPadding(dp(2), 0, 0, dp(6)) }
    private fun rounded(color: Int, radius: Int) = GradientDrawable().apply { setColor(color); cornerRadius = dp(radius).toFloat() }
    private fun dp(value: Int) = (value * resources.displayMetrics.density).toInt()

    override fun onDestroy() { super.onDestroy(); executor.shutdown() }
}