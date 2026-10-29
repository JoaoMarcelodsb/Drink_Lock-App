package com.example.drink_lock.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.drink_lock.data.SampleData
import com.example.drink_lock.ui.components.DestinoNav
import com.example.drink_lock.ui.home.HomeScreen
import com.example.drink_lock.ui.lock.LockScreen
import com.example.drink_lock.ui.login.LoginScreen
import com.example.drink_lock.ui.profile.ProfileScreen
import com.example.drink_lock.ui.protection.ProtectionScreen
import com.example.drink_lock.ui.protection.ProtectionViewModel

enum class Tela { LOGIN, INICIO, PERFIL, TESTE, PROTECAO }

@Composable
fun AppNavigation() {
    var tela by rememberSaveable { mutableStateOf(Tela.LOGIN) }

    val protecaoViewModel: ProtectionViewModel = hiltViewModel()
    val protecao by protecaoViewModel.uiState.collectAsState()

    BackHandler(enabled = tela != Tela.LOGIN && tela != Tela.INICIO) {
        tela = Tela.INICIO
    }

    val navegar: (DestinoNav) -> Unit = { destino ->
        when (destino) {
            DestinoNav.INICIO -> tela = Tela.INICIO
            DestinoNav.PERFIL -> tela = Tela.PERFIL
            DestinoNav.TESTES -> tela = Tela.TESTE
            DestinoNav.PROTECAO -> tela = Tela.PROTECAO
            DestinoNav.FOCO -> Unit
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
            appsProtegidos = protecao.totalBloqueados,
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

        Tela.PROTECAO -> ProtectionScreen(onNavegar = navegar)

        Tela.TESTE -> LockScreen(
            appName = "Teste de sobriedade",
            onUnlocked = { tela = Tela.INICIO },
        )
    }
}
