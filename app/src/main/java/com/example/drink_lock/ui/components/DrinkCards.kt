package com.example.drink_lock.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drink_lock.ui.theme.*

@Composable
fun Eyebrow(texto: String, modifier: Modifier = Modifier) {
    Text(
        text = texto.uppercase(),
        color = Green,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.5.sp,
        modifier = modifier,
    )
}

@Composable
fun SectionTitle(texto: String) {
    Text(
        text = texto,
        color = TextMain,
        fontSize = 14.sp,
        fontWeight = FontWeight.ExtraBold,
        modifier = Modifier.padding(start = 2.dp, top = 22.dp, bottom = 10.dp),
    )
}

@Composable
fun IconBox(
    texto: String,
    cor: Color = Green,
    tamanho: Dp = 40.dp,
) {
    Box(
        modifier = Modifier
            .size(tamanho)
            .clip(RoundedCornerShape(12.dp))
            .background(IconBoxBg),
        contentAlignment = Alignment.Center,
    ) {
        Text(texto, color = cor, fontSize = 17.sp)
    }
}

@Composable
fun Pill(texto: String, onClick: (() -> Unit)? = null) {
    Text(
        text = texto,
        color = Green,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .clip(CircleShape)
            .background(Color(0x1400E676))
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(horizontal = 12.dp, vertical = 6.dp),
    )
}

@Composable
fun InfoCard(
    icone: String,
    titulo: String,
    subtitulo: String,
    modifier: Modifier = Modifier,
    destaque: Boolean = false,
    onClick: (() -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
) {
    val shape = RoundedCornerShape(16.dp)
    val fundo: Brush =
        if (destaque) Brush.linearGradient(listOf(Color(0xFF0C1510), Color(0xFF0A100D)))
        else SolidColor(SurfaceCard)
    val borda = if (destaque) Color(0x1A00E676) else Color(0x0DFFFFFF)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(fundo)
            .border(1.dp, borda, shape)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        IconBox(icone)
        Column(modifier = Modifier.weight(1f)) {
            Text(titulo, color = TextMain, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(3.dp))
            Text(subtitulo, color = TextMuted, fontSize = 11.sp, lineHeight = 15.sp)
        }
        trailing?.invoke()
    }
}
