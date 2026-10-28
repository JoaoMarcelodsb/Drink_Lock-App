package com.example.drink_lock.ui.login

data class LoginUiState(
    val email: String = "",
    val senha: String = "",
    val senhaVisivel: Boolean = false,
    val erro: String? = null,
)
