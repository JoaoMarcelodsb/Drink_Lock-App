package com.example.drink_lock.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drink_lock.ui.theme.*

enum class DestinoNav(val rotulo: String, val icone: String) {
    INICIO("Início", "⌂"),
    TESTES("Testes", "✓"),
    PROTECAO("Proteção", "🛡"),
    FOCO("Foco", "◷"),
    PERFIL("Perfil", "●"),
}

@Composable
fun DrinkBottomBar(
    selecionado: DestinoNav,
    onSelect: (DestinoNav) -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(22.dp)

    Row(
        modifier = modifier
            .navigationBarsPadding()
            .padding(horizontal = 14.dp, vertical = 10.dp)
            .fillMaxWidth()
            .height(64.dp)
            .clip(shape)
            .background(Color(0xEB090F0C))
            .border(1.dp, Color(0x0FFFFFFF), shape),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DestinoNav.entries.forEach { destino ->
            val cor = if (destino == selecionado) Green else Color(0xFF68766E)

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { onSelect(destino) }
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(destino.icone, color = cor, fontSize = 20.sp)
                Text(destino.rotulo, color = cor, fontSize = 10.sp)
            }
        }
    }
}
