package com.example.drink_lock.domain.usecase

import com.example.drink_lock.domain.model.ResultadoDesafio
import com.example.drink_lock.domain.model.DesafioSobriedade
import javax.inject.Inject
import kotlin.math.abs

class EvaluateSobrietyUseCase @Inject constructor() {

    fun evaluate(
        challenge: DesafioSobriedade,
        timeRemainingMs: Long = Long.MAX_VALUE,
    ): ResultadoDesafio {
        return when (challenge) {
            is DesafioSobriedade.SequenciaToque -> {
                when {
                    timeRemainingMs <= 0L -> ResultadoDesafio.TEMPO_ESGOTADO
                    challenge.progressoUsuario == challenge.sequencia -> ResultadoDesafio.APROVADO
                    else -> ResultadoDesafio.REPROVADO
                }
            }
            is DesafioSobriedade.ToqueContinuo -> {
                ResultadoDesafio.REPROVADO
            }
        }
    }

    fun evaluateHold(heldMs: Long, challenge: DesafioSobriedade.ToqueContinuo): ResultadoDesafio {
        val diff = abs(heldMs - challenge.tempoAlvoMs)
        return if (diff <= challenge.toleranciaMs) ResultadoDesafio.APROVADO else ResultadoDesafio.REPROVADO
    }
}
