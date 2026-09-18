package com.example.drink_lock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.drink_lock.ui.lock.LockScreen
import com.example.drink_lock.ui.theme.DrinkLockTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DrinkLockTheme {
                LockScreen(
                    appName = "Instagram",
                    onUnlocked = {
                    },
                )
            }
        }
    }
}