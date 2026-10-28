package com.example.drink_lock.ui.lock

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.drink_lock.domain.model.EstadoBloqueio
import com.example.drink_lock.domain.model.DesafioSobriedade
import com.example.drink_lock.ui.components.*
import com.example.drink_lock.ui.theme.*

@Composable
fun LockScreen(
    appName: String,
    onUnlocked: () -> Unit,
    viewModel: LockViewModel = hiltViewModel(),
) {
    LaunchedEffect(appName) { viewModel.init(appName) }

    val state by viewModel.state.collectAsState()

    LaunchedEffect(state) {
        if (state is EstadoBloqueio.Desbloqueado) {
            onUnlocked()
            viewModel.init(appName)
        }
    }

    DrinkLockTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(FundoPreto, SuperficieCard)
                    )
                ),
        ) {
            AnimatedContent(
                targetState    = state,
                contentKey     = { it::class },
                transitionSpec = {
                    fadeIn() + slideInVertically { it / 4 } togetherWith
                            fadeOut() + slideOutVertically { -it / 4 }
                },
                label          = "lock_content",
            ) { currentState ->
                when (currentState) {
                    is EstadoBloqueio.Bloqueado        -> LockedPanel(
                        state   = currentState,
                        onStart = viewModel::startChallenge,
                    )
                    is EstadoBloqueio.Testando         -> TestingPanel(
                        state = currentState,
                        onTap = viewModel::onTargetTapped,
                    )
                    is EstadoBloqueio.Desbloqueado     -> UnlockedPanel(currentState)
                    is EstadoBloqueio.Bloqueado_Espera -> CooldownPanel(currentState)
                }
            }
        }
    }
}

@Composable
private fun LockedPanel(
    state: EstadoBloqueio.Bloqueado,
    onStart: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
    ) {
        IconeCadeadoAnimado(estaBloqueado = true)

        Spacer(Modifier.height(32.dp))

        Text(
            text       = state.nomeApp,
            color      = TextoCinza,
            fontSize   = 14.sp,
            fontWeight = FontWeight.Medium,
            textAlign  = TextAlign.Center,
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text       = "Acesso bloqueado",
            color      = TextoBranco,
            fontSize   = 26.sp,
            fontWeight = FontWeight.Bold,
            textAlign  = TextAlign.Center,
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text      = "Complete o teste de sobriedade\npara continuar.",
            color     = TextoCinza,
            fontSize  = 15.sp,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
        )

        Spacer(Modifier.height(48.dp))

        PrimaryButton(
            text     = "Iniciar teste",
            onClick  = onStart,
            modifier  = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun TestingPanel(
    state: EstadoBloqueio.Testando,
    onTap: (Int) -> Unit,
) {
    val challenge = state.desafio as? DesafioSobriedade.SequenciaToque ?: return
    val totalMs   = 15_000f
    val progress  = (challenge.tempoRestanteMs / totalMs).coerceIn(0f, 1f)
    val secondsLeft = (challenge.tempoRestanteMs / 1_000f).toInt()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
    ) {
        Text(
            text       = "Toque na ordem certa",
            color      = TextoBranco,
            fontSize   = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign  = TextAlign.Center,
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text      = "Toque os círculos de 1 a ${challenge.sequencia.size}",
            color     = TextoCinza,
            fontSize  = 14.sp,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(28.dp))

        Row(
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier              = Modifier.fillMaxWidth(),
        ) {
            BarraTimer(
                fracaoProgresso = progress,
                modifier         = Modifier.weight(1f),
            )
            Spacer(Modifier.width(12.dp))
            Text(
                text      = "${secondsLeft}s",
                color     = TextoCinza,
                fontSize  = 13.sp,
                fontWeight = FontWeight.Medium,
            )
        }

        Spacer(Modifier.height(36.dp))

        GradeAlvosToque(
            totalAlvos       = challenge.sequencia.size,
            sequenciaCorreta  = challenge.sequencia,
            progressoUsuario     = challenge.progressoUsuario,
            aoTocar            = onTap,
        )

        Spacer(Modifier.height(36.dp))

        IndicadorTentativas(
            tentativasRestantes = state.tentativasRestantes,
            maxTentativas  = 3,
        )
    }
}

@Composable
private fun UnlockedPanel(state: EstadoBloqueio.Desbloqueado) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
    ) {
        IconeCadeadoAnimado(estaBloqueado = false)

        Spacer(Modifier.height(32.dp))

        Text(
            text       = "Acesso liberado",
            color      = VerdeSucesso,
            fontSize   = 26.sp,
            fontWeight = FontWeight.Bold,
            textAlign  = TextAlign.Center,
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text      = "Abrindo ${state.nomeApp}…",
            color     = TextoCinza,
            fontSize  = 15.sp,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun CooldownPanel(state: EstadoBloqueio.Bloqueado_Espera) {
    val minutes = state.segundosRestantes / 60
    val seconds = state.segundosRestantes % 60

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
    ) {
        Text(text = "🚫", fontSize = 56.sp)

        Spacer(Modifier.height(24.dp))

        Text(
            text       = "Acesso negado",
            color      = VermelhoAlerta,
            fontSize   = 26.sp,
            fontWeight = FontWeight.Bold,
            textAlign  = TextAlign.Center,
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text      = "Você esgotou as tentativas.\nTente novamente em:",
            color     = TextoCinza,
            fontSize  = 15.sp,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp,
        )

        Spacer(Modifier.height(24.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(SuperficieCard)
                .padding(horizontal = 36.dp, vertical = 20.dp),
        ) {
            Text(
                text       = "%02d:%02d".format(minutes, seconds),
                color      = VermelhoAlerta,
                fontSize   = 40.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D0D1A)
@Composable
private fun LockedPreview() {
    DrinkLockTheme {
        LockedPanel(
            state   = EstadoBloqueio.Bloqueado("Instagram"),
            onStart = { },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0D0D1A)
@Composable
private fun CooldownPreview() {
    DrinkLockTheme {
        CooldownPanel(
            EstadoBloqueio.Bloqueado_Espera("Instagram", 5, 237)
        )
    }
}
