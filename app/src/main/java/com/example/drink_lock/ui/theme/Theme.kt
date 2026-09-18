package com.example.drink_lock.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CoresEscuras = darkColorScheme(
    primary        = VerdeVibrante,
    onPrimary      = FundoPreto,
    secondary      = VerdeClaro,
    background     = FundoPreto,
    surface        = SuperficieCard,
    onBackground   = TextoBranco,
    onSurface      = TextoBranco,
    error          = VermelhoAlerta,
)

private val CoresClaras = lightColorScheme(
    primary        = VerdeVibrante,
    onPrimary      = Color.White,
    secondary      = VerdeSucesso,
    background     = Color(0xFFF5F5FF),
    surface        = Color.White,
    onBackground   = FundoPreto,
    onSurface      = FundoPreto,
    error          = VermelhoAlerta,
)

@Composable
fun DrinkLockTheme(
    temaEscuro: Boolean = isSystemInDarkTheme(),
    conteudo: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (temaEscuro) CoresEscuras else CoresClaras,
        content = conteudo,
    )
}
