package com.example.drink_lock.domain.model

sealed class EstadoBloqueio {
    data class Bloqueado(val nomeApp: String) : EstadoBloqueio()

    data class Testando(
        val nomeApp: String,
        val desafio: DesafioSobriedade,
        val tentativasRestantes: Int,
    ) : EstadoBloqueio()

    data class Desbloqueado(val nomeApp: String) : EstadoBloqueio()

    data class Bloqueado_Espera(
        val nomeApp: String,
        val minutosBloqueio: Int,
        val segundosRestantes: Int,
    ) : EstadoBloqueio()
}

sealed class DesafioSobriedade {
    data class SequenciaToque(
        val sequencia: List<Int>,
        val progressoUsuario: List<Int> = emptyList(),
        val tempoRestanteMs: Long = 15_000L,
    ) : DesafioSobriedade()

    data class ToqueContinuo(
        val tempoAlvoMs: Long = 3_000L,
        val toleranciaMs: Long = 500L,
    ) : DesafioSobriedade()
}

enum class ResultadoDesafio { APROVADO, REPROVADO, TEMPO_ESGOTADO }
