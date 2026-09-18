package com.example.drink_lock.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drink_lock.ui.theme.*

@Composable
fun IconeCadeadoAnimado(
    estaBloqueado: Boolean,
    modifier: Modifier = Modifier,
) {
    val scale by animateFloatAsState(
        targetValue  = if (estaBloqueado) 1f else 1.15f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label        = "lock_scale",
    )
    val color = if (estaBloqueado) VermelhoAlerta else VerdeSucesso

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .scale(scale)
            .size(96.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.12f))
            .border(2.dp, color.copy(alpha = 0.4f), CircleShape),
    ) {
        Text(
            text     = if (estaBloqueado) "🔒" else "🔓",
            fontSize = 40.sp,
        )
    }
}

@Composable
fun BarraTimer(
    fracaoProgresso: Float,
    modifier: Modifier = Modifier,
) {
    val color = when {
        fracaoProgresso > 0.5f -> VerdeSucesso
        fracaoProgresso > 0.25f -> Color(0xFFFFC107)
        else -> VermelhoAlerta
    }

    Column(modifier = modifier) {
        LinearProgressIndicator(
            progress            = { fracaoProgresso.coerceIn(0f, 1f) },
            modifier            = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color               = color,
            trackColor          = BordaLinha,
        )
    }
}

@Composable
fun GradeAlvosToque(
    totalAlvos: Int,
    sequenciaCorreta: List<Int>,
    progressoUsuario: List<Int>,
    aoTocar: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val colunas = 2
    val linhas  = (totalAlvos + 1) / colunas

    Column(
        verticalArrangement   = Arrangement.spacedBy(16.dp),
        horizontalAlignment   = Alignment.CenterHorizontally,
        modifier              = modifier,
    ) {
        for (linha in 0 until linhas) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                for (col in 0 until colunas) {
                    val indice = linha * colunas + col
                    if (indice < totalAlvos) {
                        val numeroPasso = sequenciaCorreta.indexOf(indice)
                        val tocado      = progressoUsuario.contains(indice)

                        AlvoToque(
                            rotulo   = (numeroPasso + 1).toString(),
                            tocado   = tocado,
                            aoTocar  = { aoTocar(indice) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AlvoToque(
    rotulo: String,
    tocado: Boolean,
    aoTocar: () -> Unit,
    tamanho: Dp = 72.dp,
) {
    val scale by animateFloatAsState(
        targetValue  = if (tocado) 0.88f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label        = "target_scale_$rotulo",
    )
    val corFundo = if (tocado) VerdeVibrante else SuperficieCard
    val corBorda = if (tocado) VerdeClaro else BordaLinha

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .scale(scale)
            .size(tamanho)
            .clip(CircleShape)
            .background(corFundo)
            .border(2.dp, corBorda, CircleShape)
            .clickable(enabled = !tocado, onClick = aoTocar),
    ) {
        Text(
            text       = rotulo,
            color      = if (tocado) FundoPreto else TextoCinza,
            fontSize   = 22.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun IndicadorTentativas(
    tentativasRestantes: Int,
    maxTentativas: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment     = Alignment.CenterVertically,
        modifier              = modifier,
    ) {
        Text(
            text     = "Tentativas:",
            color    = TextoCinza,
            fontSize = 13.sp,
        )
        repeat(maxTentativas) { i ->
            val preenchido = i < tentativasRestantes
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(if (preenchido) VerdeVibrante else BordaLinha),
            )
        }
    }
}

@Composable
fun BotaoGradiente(
    texto: String,
    aoClicar: () -> Unit,
    modifier: Modifier = Modifier,
    habilitado: Boolean = true,
) {
    val gradient = Brush.horizontalGradient(
        colors = if (habilitado)
            listOf(VerdeVibrante, VerdeClaro)
        else
            listOf(BordaLinha, BordaLinha),
    )
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(52.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(gradient)
            .clickable(enabled = habilitado, onClick = aoClicar)
            .padding(horizontal = 32.dp),
    ) {
        Text(
            text       = texto,
            color      = if (habilitado) FundoPreto else TextoCinza,
            fontSize   = 16.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign  = TextAlign.Center,
        )
    }
}
