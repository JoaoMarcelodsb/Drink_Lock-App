package com.example.drink_lock

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.drink_lock.data.ProtectionMonitor
import com.example.drink_lock.ui.navigation.AppNavigation
import com.example.drink_lock.ui.theme.DrinkLockTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var protectionMonitor: ProtectionMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
        )

        setContent {
            DrinkLockTheme {
                AppNavigation()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        protectionMonitor.refresh()
    }
}
