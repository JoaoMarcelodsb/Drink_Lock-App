package com.example.drink_lock.ui.profile

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
import com.example.drink_lock.domain.model.InfoPessoal
import com.example.drink_lock.domain.model.Usuario
import com.example.drink_lock.ui.components.*
import com.example.drink_lock.ui.theme.*

@Composable
fun ProfileScreen(
    usuario: Usuario,
    infos: List<InfoPessoal>,
    onVoltar: () -> Unit,
    onSair: () -> Unit,
    onNavegar: (DestinoNav) -> Unit,
) {
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
                BotaoVoltar(onClick = onVoltar)
                Column {
                    Eyebrow("Minha conta")
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "Perfil",
                        color = TextMain,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                    )
                }
                Spacer(Modifier.size(38.dp))
            }

            Spacer(Modifier.height(18.dp))

            CartaoPerfil(usuario)

            Spacer(Modifier.height(18.dp))

            SecaoSobreVoce(infos)

            Spacer(Modifier.height(14.dp))

            SecondaryButton(
                text = "Sair da conta",
                onClick = onSair,
                modifier = Modifier.fillMaxWidth(),
                contentColor = Color(0xFFB8C2BC),
                borderColor = Color(0x17FFFFFF),
            )
        }

        DrinkBottomBar(
            selecionado = DestinoNav.PERFIL,
            onSelect = onNavegar,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

@Composable
private fun BotaoVoltar(onClick: () -> Unit) {
    val shape = RoundedCornerShape(12.dp)
    Box(
        modifier = Modifier
            .size(38.dp)
            .clip(shape)
            .background(Color(0xFF0D1812))
            .border(1.dp, Color(0x12FFFFFF), shape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text("‹", color = Color(0xFFDFE8E2), fontSize = 26.sp)
    }
}

@Composable
private fun CartaoPerfil(usuario: Usuario) {
    val shape = RoundedCornerShape(24.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(Brush.linearGradient(listOf(Color(0xFF0D1812), Color(0xFF09100C))))
            .border(1.dp, Color(0x2100E676), shape)
            .padding(horizontal = 16.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(R.drawable.pessoa),
            contentDescription = "Foto de perfil de ${usuario.nome}",
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopCenter,
            modifier = Modifier
                .size(104.dp)
                .clip(CircleShape)
                .border(2.dp, Green, CircleShape),
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = usuario.nome,
            color = TextMain,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
        )
        Spacer(Modifier.height(4.dp))
        Text(text = usuario.email, color = Color(0xFF8C9B92), fontSize = 11.sp)
        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .clip(CircleShape)
                .background(Color(0x0D00E676))
                .border(1.dp, Color(0x2E00E676), CircleShape)
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(Modifier.size(6.dp).clip(CircleShape).background(Green))
            Spacer(Modifier.width(6.dp))
            Text(
                text = "Conta ativa",
                color = Green,
                fontSize = 10.sp,
                fontWeight = FontWeight.ExtraBold,
            )
        }
    }
}

@Composable
private fun SecaoSobreVoce(infos: List<InfoPessoal>) {
    val shape = RoundedCornerShape(22.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(FieldBackground)
            .border(1.dp, Color(0x0FFFFFFF), shape)
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Eyebrow("Sobre você")
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Coisas que fazem parte da sua vida",
                    color = TextMain,
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                )
            }
            Spacer(Modifier.width(8.dp))
            IconBox("⌾", tamanho = 34.dp)
        }

        Spacer(Modifier.height(8.dp))
        Text(
            text = "O DrinkLock usa essas informações para criar lembretes mais pessoais e ajudar você a lembrar do que realmente importa.",
            color = Color(0xFF78867D),
            fontSize = 11.sp,
            lineHeight = 16.sp,
        )

        Spacer(Modifier.height(12.dp))
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            infos.forEach { CartaoInfoPessoal(it) }
        }

        Spacer(Modifier.height(12.dp))
        NotaPrivacidade()
    }
}

@Composable
private fun CartaoInfoPessoal(info: InfoPessoal) {
    val shape = RoundedCornerShape(14.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(Color(0xFF080E0B))
            .border(1.dp, Color(0x0EFFFFFF), shape)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        IconBox(info.icone, cor = Color.Unspecified, tamanho = 38.dp)
        Column(modifier = Modifier.weight(1f)) {
            Text(info.rotulo, color = Color(0xFF748179), fontSize = 10.sp)
            Spacer(Modifier.height(3.dp))
            Text(
                text = info.valor,
                color = Color(0xFFD6DED9),
                fontSize = 12.sp,
                lineHeight = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun NotaPrivacidade() {
    val shape = RoundedCornerShape(12.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(Color(0x0900E676))
            .border(1.dp, Color(0x1400E676), shape)
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("🔒", fontSize = 13.sp)
        Text(
            text = "Essas informações fazem parte do seu perfil e podem ser usadas pelo DrinkLock em lembretes e mensagens personalizadas.",
            color = Color(0xFF69776F),
            fontSize = 10.sp,
            lineHeight = 14.sp,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF050807, heightDp = 1000)
@Composable
private fun ProfilePreview() {
    DrinkLockTheme {
        ProfileScreen(
            usuario = SampleData.usuario,
            infos = SampleData.infos,
            onVoltar = {},
            onSair = {},
            onNavegar = {},
        )
    }
}
