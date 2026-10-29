package com.example.drink_lock.ui.protection

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.drink_lock.ui.components.*
import com.example.drink_lock.ui.theme.*

@Composable
fun ProtectionScreen(
    onNavegar: (DestinoNav) -> Unit,
    viewModel: ProtectionViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) { viewModel.carregarApps() }

    ProtectionContent(
        state = state,
        onAbrirAcessibilidade = {
            context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        },
        onToggleApp = viewModel::onToggle,
        onNavegar = onNavegar,
    )
}

@Composable
fun ProtectionContent(
    state: ProtectionUiState,
    onAbrirAcessibilidade: () -> Unit,
    onToggleApp: (String, Boolean) -> Unit,
    onNavegar: (DestinoNav) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize().screenBackground()) {

        LazyColumn(
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item {
                Column {
                    Eyebrow("Proteção")
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Aplicativos protegidos",
                        color = TextMain,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }

            item { StatusCard(state, onAbrirAcessibilidade) }

            item { SectionTitle("Escolha o que proteger") }

            if (state.carregando) {
                item { Text("Carregando apps…", color = TextMuted, fontSize = 12.sp) }
            }

            items(state.apps, key = { it.packageName }) { app ->
                AppRow(app, onToggleApp)
            }
        }

        DrinkBottomBar(
            selecionado = DestinoNav.PROTECAO,
            onSelect = onNavegar,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

@Composable
private fun StatusCard(state: ProtectionUiState, onAbrir: () -> Unit) {
    val ativo = state.servicoAtivo
    val cor = if (ativo) Green else Danger
    val shape = RoundedCornerShape(24.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(Brush.linearGradient(listOf(Color(0xFF0C1911), Color(0xFF0A100D))))
            .border(1.dp, cor.copy(alpha = 0.35f), shape)
            .padding(20.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(9.dp).clip(CircleShape).background(cor))
            Spacer(Modifier.width(8.dp))
            Text(
                text = if (ativo) "Proteção ativa" else "Proteção desativada",
                color = Color(0xFFCDD7D1),
                fontSize = 12.sp,
            )
        }

        Spacer(Modifier.height(12.dp))
        Text(
            text = if (ativo) "O bloqueio está funcionando." else "Falta um passo para proteger você.",
            color = TextMain,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
        )

        Spacer(Modifier.height(6.dp))
        Text(
            text = when {
                !ativo -> "O Drink Lock usa o serviço de acessibilidade do Android para perceber quando um app protegido é aberto. Ele não lê o conteúdo da sua tela."
                state.totalBloqueados == 0 -> "Escolha abaixo quais apps devem pedir o teste antes de abrir."
                else -> "${state.totalBloqueados} app(s) protegido(s). Ao abrir um deles, o teste de sobriedade aparece primeiro."
            },
            color = TextMuted,
            fontSize = 12.sp,
            lineHeight = 18.sp,
        )

        if (!ativo) {
            Spacer(Modifier.height(16.dp))
            PrimaryButton(
                text = "Ativar nas configurações",
                onClick = onAbrir,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = "Na tela que abrir, procure “Drink Lock” (em Apps instalados ou Serviços baixados) e ligue a chave. " +
                    "Se a opção estiver bloqueada: Informações do app → ⋮ → Permitir configurações restritas.",
                color = Color(0xFF78867D),
                fontSize = 11.sp,
                lineHeight = 16.sp,
            )
        }
    }
}

@Composable
private fun AppRow(app: AppItem, onToggle: (String, Boolean) -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    val bitmap = remember(app.packageName) { app.icon?.asImageBitmap() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(SurfaceCard)
            .border(1.dp, Color(0x0DFFFFFF), shape)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap,
                contentDescription = null,
                modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)),
            )
        } else {
            IconBox("●")
        }

        Text(
            text = app.label,
            color = TextMain,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )

        Switch(
            checked = app.bloqueado,
            onCheckedChange = { onToggle(app.packageName, it) },
            colors = SwitchDefaults.colors(
                checkedThumbColor = OnGreen,
                checkedTrackColor = Green,
                uncheckedThumbColor = TextMuted,
                uncheckedTrackColor = Surface2,
                uncheckedBorderColor = BorderGreen,
            ),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF050807, heightDp = 900)
@Composable
private fun ProtectionPreview() {
    DrinkLockTheme {
        ProtectionContent(
            state = ProtectionUiState(
                servicoAtivo = false,
                totalBloqueados = 1,
                apps = listOf(
                    AppItem("com.instagram.android", "Instagram", null, true),
                    AppItem("com.whatsapp", "WhatsApp", null, false),
                    AppItem("com.google.android.youtube", "YouTube", null, false),
                ),
            ),
            onAbrirAcessibilidade = {},
            onToggleApp = { _, _ -> },
            onNavegar = {},
        )
    }
}
