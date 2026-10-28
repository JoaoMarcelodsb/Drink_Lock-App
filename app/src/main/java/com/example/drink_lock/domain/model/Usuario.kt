package com.example.drink_lock.domain.model

data class Usuario(
    val nome: String,
    val email: String,
)

data class InfoPessoal(
    val icone: String,
    val rotulo: String,
    val valor: String,
)
