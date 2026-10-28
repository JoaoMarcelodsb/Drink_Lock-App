package com.example.drink_lock.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.drink_lock.R
import com.example.drink_lock.data.SampleData
import com.example.drink_lock.domain.model.Usuario
import com.example.drink_lock.ui.components.*
import com.example.drink_lock.ui.theme.*
import java.time.LocalTime

@Composable
fun HomeScreen(
    usuario: Usuario,
    diasEmFoco: Int,
    appsProtegidos: Int,
    onPerfil: () -> Unit,
    onIniciarTeste: () -> Unit,
    onNavegar: (DestinoNav) -> Unit,
) {
    val saudacao = remember { saudacaoDoDia() }
    val primeiroNome = usuario.nome.substringBefore(" ")

    Box(modifier = Modifier.fillMaxSize().screenBackground()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 120.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Eyebrow("DrinkLock")
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "$saudacao, $primeiroNome 👋",
                        color = TextMain,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.5).sp,
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Seu dispositivo está sendo acompanhado.",
                        color = TextMuted,
                        fontSize = 12.sp,
                    )
                }
                Spacer(Modifier.width(12.dp))
                Image(
                    painter = painterResource(R.drawable.pessoa),
                    contentDescription = "Perfil",
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.TopCenter,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color(0x4D00E676), CircleShape)
                        .clickable(onClick = onPerfil),
                )
            }

            Spacer(Modifier.height(20.dp))

            ProtectionCard()

            SectionTitle("O que você pretende fazer hoje?")
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                InfoCard(
                    icone = "🍹",
                    titulo = "Talvez eu beba hoje",
                    subtitulo = "Vou a um evento ou encontro e quero me prevenir.",
                    destaque = true,
                    onClick = {},
                    trailing = { Seta() },
                )
                InfoCard(
                    icone = "🛡",
                    titulo = "Vou beber, quero proteção",
                    subtitulo = "Ative uma proteção extra para esta noite.",
                    destaque = true,
                    onClick = {},
                    trailing = { Seta() },
                )
                InfoCard(
                    icone = "✓",
                    titulo = "Não vou beber hoje",
                    subtitulo = "Continuar normalmente com a proteção ativa.",
                    destaque = true,
                    onClick = {},
                    trailing = { Seta() },
                )
            }

            SectionTitle("Acesso rápido")
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                InfoCard(
                    icone = "✓",
                    titulo = "Teste de sobriedade",
                    subtitulo = "Faça uma nova verificação",
                    trailing = { Pill("Iniciar", onClick = onIniciarTeste) },
                )
                InfoCard(
                    icone = "⌁",
                    titulo = "Aplicativos protegidos",
                    subtitulo = "Redes sociais e apps selecionados",
                    trailing = { Pill("$appsProtegidos ativos") },
                )
            }

            SectionTitle("Seu progresso")
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                InfoCard(
                    icone = "◷",
                    titulo = "Tempo em foco",
                    subtitulo = "Dias desde sua última bebida registrada",
                    destaque = true,
                    trailing = {
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = diasEmFoco.toString(),
                                color = Green,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                            )
                            Text("dias", color = Color(0xFF75837A), fontSize = 11.sp)
                        }
                    },
                )
                InfoCard(
                    icone = "●",
                    titulo = "Última verificação",
                    subtitulo = "Nenhuma verificação pendente",
                )
            }

            SectionTitle("Dica do DrinkLock")
            DicaCard("Se você pretende beber, ative sua proteção antes de começar.")
        }

        DrinkBottomBar(
            selecionado = DestinoNav.INICIO,
            onSelect = onNavegar,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

@Composable
private fun ProtectionCard() {
    val shape = RoundedCornerShape(24.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 170.dp)
            .clip(shape)
            .background(Brush.linearGradient(listOf(Color(0xFF0C1911), Color(0xFF0A100D))))
            .border(1.dp, Color(0x3300E676), shape)
            .padding(20.dp),
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(9.dp).clip(CircleShape).background(Green))
                Spacer(Modifier.width(8.dp))
                Text("Proteção ativa", color = Color(0xFFCDD7D1), fontSize = 12.sp)
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = "Você está protegido.",
                color = TextMain,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "O DrinkLock está pronto para verificar sua condição antes de liberar ações protegidas.",
                color = TextMuted,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                modifier = Modifier.widthIn(max = 250.dp),
            )
            Spacer(Modifier.height(24.dp))
        }
        Text(
            text = "Ativo ✓",
            color = Green,
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.align(Alignment.BottomEnd),
        )
    }
}

@Composable
private fun Seta() {
    Text("›", color = Color(0xFF617069), fontSize = 22.sp)
}

@Composable
private fun DicaCard(texto: String) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(FieldBackground)
            .border(1.dp, Color(0x0DFFFFFF), shape)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("💡", fontSize = 20.sp)
        Text(texto, color = Color(0xFF89958E), fontSize = 11.sp, lineHeight = 16.sp)
    }
}

private fun saudacaoDoDia(): String {
    return when (LocalTime.now().hour) {
        in 5..11 -> "Bom dia"
        in 12..17 -> "Boa tarde"
        else -> "Boa noite"
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF050807, heightDp = 1000)
@Composable
private fun HomePreview() {
    DrinkLockTheme {
        HomeScreen(
            usuario = SampleData.usuario,
            diasEmFoco = 12,
            appsProtegidos = 3,
            onPerfil = {},
            onIniciarTeste = {},
            onNavegar = {},
        )
    }
}
