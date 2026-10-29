package com.example.drink_lock.service

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.os.PowerManager
import android.os.SystemClock
import android.view.accessibility.AccessibilityEvent
import android.view.inputmethod.InputMethodManager
import com.example.drink_lock.data.BlockerPreferences
import com.example.drink_lock.ui.lock.LockActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AppBlockerService : AccessibilityService() {

    @Inject lateinit var prefs: BlockerPreferences

    private val handler = Handler(Looper.getMainLooper())
    private var pacoteAtual: String? = null
    private var ultimoBloqueio = 0L

    private val pacotesIgnorados: Set<String> by lazy {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.enabledInputMethodList.map { it.packageName }.toSet() + "com.android.systemui"
    }

    private val verificador = object : Runnable {
        override fun run() {
            pacoteAtual?.let { verificar(it) }
            handler.postDelayed(this, 3_000L)
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        handler.removeCallbacks(verificador)
        handler.postDelayed(verificador, 3_000L)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) return
        val pacote = event.packageName?.toString() ?: return
        if (pacote in pacotesIgnorados) return

        pacoteAtual = pacote
        verificar(pacote)
    }

    private fun verificar(pacote: String) {
        if (pacote == packageName) return
        if (!prefs.isBlocked(pacote)) return
        if (prefs.isTemporarilyUnlocked(pacote)) return

        val power = getSystemService(POWER_SERVICE) as PowerManager
        if (!power.isInteractive) return

        val agora = SystemClock.elapsedRealtime()
        if (agora - ultimoBloqueio < 1_000L) return
        ultimoBloqueio = agora

        startActivity(LockActivity.intent(this, pacote, nomeDoApp(pacote)))
    }

    private fun nomeDoApp(pacote: String): String = try {
        @Suppress("DEPRECATION")
        packageManager.getApplicationInfo(pacote, 0).loadLabel(packageManager).toString()
    } catch (e: PackageManager.NameNotFoundException) {
        pacote
    }

    override fun onInterrupt() {}

    override fun onUnbind(intent: Intent?): Boolean {
        handler.removeCallbacks(verificador)
        return super.onUnbind(intent)
    }
}
