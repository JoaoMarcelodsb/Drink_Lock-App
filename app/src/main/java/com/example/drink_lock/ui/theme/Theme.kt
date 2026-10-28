package com.example.drink_lock.ui.theme

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Green = Color(0xFF00E676)
val GreenDark = Color(0xFF00A85A)
val GreenSoft = Color(0xFF8FFFC0)
val OnGreen = Color(0xFF001B0D)
val Black = Color(0xFF050807)
val SurfaceCard = Color(0xFF0D1411)
val Surface2 = Color(0xFF111B16)
val BorderGreen = Color(0x2900E676)
val TextMain = Color(0xFFF4F8F5)
val TextMuted = Color(0xFF9AA9A0)
val Danger = Color(0xFFFF5D5D)

val FieldBackground = Color(0xFF0B120E)
val IconBoxBg = Color(0xFF122219)

val DeepNight = Black
val SurfaceNight = SurfaceCard
val IndigoVibrant = Green
val IndigoLight = GreenSoft
val AlertRed = Danger
val SuccessGreen = Green
val TextPrimary = TextMain
val TextSecondary = TextMuted
val Stroke = Color(0xFF1A241E)

val ScreenBackground: Brush = Brush.verticalGradient(
    0.00f to Color(0xFF07100B),
    0.58f to Color(0xFF050807),
    1.00f to Color(0xFF07110B),
)

fun Modifier.screenBackground(): Modifier = this.background(ScreenBackground)

private val DrinkColorScheme = darkColorScheme(
    primary = Green,
    onPrimary = OnGreen,
    secondary = GreenSoft,
    background = Black,
    surface = SurfaceCard,
    onBackground = TextMain,
    onSurface = TextMain,
    error = Danger,
)

@Composable
fun DrinkLockTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = DrinkColorScheme, content = content)
}
