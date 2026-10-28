package com.example.drink_lock.data

import com.example.drink_lock.domain.model.InfoPessoal
import com.example.drink_lock.domain.model.Usuario

object SampleData {
    val usuario = Usuario(
        nome = "João Peixeiro",
        email = "peixeirofodao667@gmail.com",
    )

    val infos = listOf(
        InfoPessoal("❤", "Quem é importante para mim", "Minha família e minha namorada"),
        InfoPessoal("🐶", "Meus animais", "Meu cachorro Thor e minha gata Luna"),
        InfoPessoal("🎮", "O que gosto de jogar", "Monster hunter, Minecraft e Roblox principalmente"),
        InfoPessoal("🏋", "O que gosto de fazer", "Treinar, jogar, ouvir música e sair com quem gosto"),
        InfoPessoal("🎯", "O que quero preservar", "Minha saúde, meus relacionamentos e meu tempo livre"),
    )
}
