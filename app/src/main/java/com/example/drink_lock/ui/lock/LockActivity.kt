package com.example.drink_lock.ui.lock

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.drink_lock.ui.theme.DrinkLockTheme
import dagger.hilt.android.AndroidEntryPoint

private data class Alvo(val pacote: String?, val label: String)

@AndroidEntryPoint
class LockActivity : ComponentActivity() {

    private var alvo by mutableStateOf(Alvo(null, ""))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
        )

        alvo = lerAlvo(intent)
        if (alvo.pacote == null) {
            finish()
            return
        }

        setContent {
            DrinkLockTheme {
                BackHandler { irParaTelaInicial() }

                LockScreen(
                    appName = alvo.label,
                    packageName = alvo.pacote,
                    onUnlocked = { finish() },
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        alvo = lerAlvo(intent)
    }

    private fun lerAlvo(intent: Intent): Alvo {
        val pacote = intent.getStringExtra(EXTRA_PACOTE)
        val label = intent.getStringExtra(EXTRA_LABEL) ?: pacote ?: "Aplicativo"
        return Alvo(pacote, label)
    }

    private fun irParaTelaInicial() {
        startActivity(
            Intent(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_HOME)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
        finish()
    }

    companion object {
        private const val EXTRA_PACOTE = "pacote"
        private const val EXTRA_LABEL = "label"

        fun intent(context: Context, pacote: String, label: String): Intent =
            Intent(context, LockActivity::class.java)
                .putExtra(EXTRA_PACOTE, pacote)
                .putExtra(EXTRA_LABEL, label)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
}
