package com.example.drink_lock.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.drink_lock.data.SampleData
import com.example.drink_lock.ui.components.DestinoNav
import com.example.drink_lock.ui.home.HomeScreen
import com.example.drink_lock.ui.lock.LockScreen
import com.example.drink_lock.ui.login.LoginScreen
import com.example.drink_lock.ui.profile.ProfileScreen

enum class Tela { LOGIN, INICIO, PERFIL, TESTE }

@Composable
fun AppNavigation() {
    var tela by rememberSaveable { mutableStateOf(Tela.LOGIN) }

    BackHandler(enabled = tela == Tela.PERFIL || tela == Tela.TESTE) {
        tela = Tela.INICIO
    }

    val navegar: (DestinoNav) -> Unit = { destino ->
        when (destino) {
            DestinoNav.INICIO -> tela = Tela.INICIO
            DestinoNav.PERFIL -> tela = Tela.PERFIL
            DestinoNav.TESTES -> tela = Tela.TESTE
            DestinoNav.PROTECAO, DestinoNav.FOCO -> Unit
        }
    }

    when (tela) {
        Tela.LOGIN -> LoginScreen(
            onLoginSucesso = { tela = Tela.INICIO },
            onCriarConta = {},
        )

        Tela.INICIO -> HomeScreen(
            usuario = SampleData.usuario,
            diasEmFoco = 12,
            appsProtegidos = 3,
            onPerfil = { tela = Tela.PERFIL },
            onIniciarTeste = { tela = Tela.TESTE },
            onNavegar = navegar,
        )

        Tela.PERFIL -> ProfileScreen(
            usuario = SampleData.usuario,
            infos = SampleData.infos,
            onVoltar = { tela = Tela.INICIO },
            onSair = { tela = Tela.LOGIN },
            onNavegar = navegar,
        )

        Tela.TESTE -> LockScreen(
            appName = "Teste de sobriedade",
            onUnlocked = { tela = Tela.INICIO },
        )
    }
}
