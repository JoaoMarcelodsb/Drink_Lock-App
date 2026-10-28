package com.example.drink_lock.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.drink_lock.R
import com.example.drink_lock.ui.components.DrinkTextField
import com.example.drink_lock.ui.components.PrimaryButton
import com.example.drink_lock.ui.components.SecondaryButton
import com.example.drink_lock.ui.theme.*

@Composable
fun LoginScreen(
    onLoginSucesso: () -> Unit,
    onCriarConta: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    LoginContent(
        state = state,
        onEmailChange = viewModel::onEmailChange,
        onSenhaChange = viewModel::onSenhaChange,
        onToggleSenha = viewModel::onToggleSenhaVisivel,
        onEntrar = { viewModel.onEntrarClick(onLoginSucesso) },
        onEsqueciSenha = {},
        onCriarConta = onCriarConta,
    )
}

@Composable
fun LoginContent(
    state: LoginUiState,
    onEmailChange: (String) -> Unit,
    onSenhaChange: (String) -> Unit,
    onToggleSenha: () -> Unit,
    onEntrar: () -> Unit,
    onEsqueciSenha: () -> Unit,
    onCriarConta: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .screenBackground()
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 28.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(24.dp))

        Image(
            painter = painterResource(R.drawable.drink),
            contentDescription = "Logo Drink Lock",
            modifier = Modifier
                .size(72.dp)
                .clip(RoundedCornerShape(18.dp)),
        )
        Spacer(Modifier.height(10.dp))

        Text(
            text = buildAnnotatedString {
                append("Drink")
                withStyle(SpanStyle(color = Green)) { append("Lock") }
            },
            color = TextMain,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = (-0.7).sp,
        )

        Spacer(Modifier.height(22.dp))

        Text(
            text = "Bem-vindo de volta",
            color = TextMain,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "Entre na sua conta para continuar protegido.",
            color = TextMuted,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(28.dp))

        DrinkTextField(
            label = "E-mail",
            value = state.email,
            onValueChange = onEmailChange,
            placeholder = "seuemail@exemplo.com",
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
            onImeAction = { focusManager.moveFocus(FocusDirection.Down) },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(14.dp))

        DrinkTextField(
            label = "Senha",
            value = state.senha,
            onValueChange = onSenhaChange,
            placeholder = "Digite sua senha",
            isPassword = true,
            passwordVisible = state.senhaVisivel,
            onTogglePassword = onToggleSenha,
            imeAction = ImeAction.Done,
            onImeAction = {
                focusManager.clearFocus()
                onEntrar()
            },
            modifier = Modifier.fillMaxWidth(),
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            TextButton(onClick = onEsqueciSenha) {
                Text("Esqueci minha senha", color = GreenSoft, fontSize = 12.sp)
            }
        }

        state.erro?.let { erro ->
            Text(
                text = erro,
                color = Danger,
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(8.dp))
        }

        Spacer(Modifier.height(8.dp))

        PrimaryButton(
            text = "Entrar",
            onClick = onEntrar,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = Stroke)
            Text(
                text = "ou",
                color = Color(0xFF58645D),
                fontSize = 11.sp,
                modifier = Modifier.padding(horizontal = 12.dp),
            )
            HorizontalDivider(modifier = Modifier.weight(1f), color = Stroke)
        }

        Spacer(Modifier.height(20.dp))

        SecondaryButton(
            text = "Criar uma conta",
            onClick = onCriarConta,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(40.dp))
        Text(
            text = "Proteção inteligente para momentos que exigem atenção.",
            color = Color(0xFF6F7C74),
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(8.dp))
        Text(text = "DrinkLock", color = Color(0xFF4E5B54), fontSize = 10.sp)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF050807, heightDp = 800)
@Composable
private fun LoginPreview() {
    DrinkLockTheme {
        LoginContent(
            state = LoginUiState(),
            onEmailChange = {},
            onSenhaChange = {},
            onToggleSenha = {},
            onEntrar = {},
            onEsqueciSenha = {},
            onCriarConta = {},
        )
    }
}
