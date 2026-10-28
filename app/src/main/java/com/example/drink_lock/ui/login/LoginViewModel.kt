package com.example.drink_lock.ui.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(novo: String) {
        _uiState.update { it.copy(email = novo, erro = null) }
    }

    fun onSenhaChange(nova: String) {
        _uiState.update { it.copy(senha = nova, erro = null) }
    }

    fun onToggleSenhaVisivel() {
        _uiState.update { it.copy(senhaVisivel = !it.senhaVisivel) }
    }

    fun onEntrarClick(onSucesso: () -> Unit) {
        val s = _uiState.value
        when {
            s.email.isBlank() || s.senha.isBlank() ->
                _uiState.update { it.copy(erro = "Preencha e-mail e senha.") }

            !Patterns.EMAIL_ADDRESS.matcher(s.email).matches() ->
                _uiState.update { it.copy(erro = "Digite um e-mail válido.") }

            else -> {
                _uiState.value = LoginUiState()
                onSucesso()
            }
        }
    }
}
