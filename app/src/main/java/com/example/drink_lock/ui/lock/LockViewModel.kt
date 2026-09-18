package com.example.drink_lock.ui.lock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drink_lock.domain.model.ResultadoDesafio
import com.example.drink_lock.domain.model.EstadoBloqueio
import com.example.drink_lock.domain.model.DesafioSobriedade
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val MAX_ATTEMPTS        = 3
private const val COUNTDOWN_MS        = 15_000L
private const val COUNTDOWN_TICK_MS   = 100L
private const val COOLDOWN_MINUTES    = 5
private const val TAP_TARGET_COUNT    = 4

@HiltViewModel
class LockViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow<EstadoBloqueio>(EstadoBloqueio.Bloqueado(nomeApp = ""))
    val state: StateFlow<EstadoBloqueio> = _state.asStateFlow()

    private var attemptsLeft = MAX_ATTEMPTS
    private var timerJob: Job? = null

    fun init(appName: String) {
        attemptsLeft = MAX_ATTEMPTS
        _state.value = EstadoBloqueio.Bloqueado(appName)
    }

    fun startChallenge() {
        val appName = currentAppName()
        val challenge = buildTapSequenceChallenge()
        _state.value = EstadoBloqueio.Testando(
            nomeApp             = appName,
            desafio             = challenge,
            tentativasRestantes = attemptsLeft,
        )
        startCountdown(challenge)
    }

    fun onTargetTapped(targetIndex: Int) {
        val current = (_state.value as? EstadoBloqueio.Testando) ?: return
        val challenge = current.desafio as? DesafioSobriedade.SequenciaToque ?: return

        val expectedNext = challenge.sequencia.getOrNull(challenge.progressoUsuario.size)
        if (targetIndex != expectedNext) {
            handleResult(ResultadoDesafio.REPROVADO, current.nomeApp)
            return
        }

        val newProgress = challenge.progressoUsuario + targetIndex
        val updatedChallenge = challenge.copy(progressoUsuario = newProgress)

        if (newProgress == challenge.sequencia) {
            timerJob?.cancel()
            handleResult(ResultadoDesafio.APROVADO, current.nomeApp)
        } else {
            _state.value = current.copy(desafio = updatedChallenge)
        }
    }

    private fun startCountdown(initial: DesafioSobriedade.SequenciaToque) {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            var remaining = initial.tempoRestanteMs
            while (remaining > 0) {
                delay(COUNTDOWN_TICK_MS)
                remaining -= COUNTDOWN_TICK_MS
                val current = _state.value as? EstadoBloqueio.Testando ?: return@launch
                val ch = current.desafio as? DesafioSobriedade.SequenciaToque ?: return@launch
                val updated = ch.copy(tempoRestanteMs = remaining.coerceAtLeast(0L))
                _state.value = current.copy(desafio = updated)
            }
            val current = _state.value as? EstadoBloqueio.Testando ?: return@launch
            handleResult(ResultadoDesafio.TEMPO_ESGOTADO, current.nomeApp)
        }
    }

    private fun handleResult(result: ResultadoDesafio, appName: String) {
        timerJob?.cancel()
        when (result) {
            ResultadoDesafio.APROVADO -> {
                _state.value = EstadoBloqueio.Desbloqueado(appName)
            }
            ResultadoDesafio.REPROVADO, ResultadoDesafio.TEMPO_ESGOTADO -> {
                attemptsLeft--
                if (attemptsLeft <= 0) {
                    startCooldown(appName)
                } else {
                    _state.value = EstadoBloqueio.Bloqueado(appName)
                }
            }
        }
    }

    fun retryChallenge() {
        val appName = currentAppName()
        _state.value = EstadoBloqueio.Bloqueado(appName)
    }

    private fun startCooldown(appName: String) {
        var secondsLeft = COOLDOWN_MINUTES * 60
        _state.value = EstadoBloqueio.Bloqueado_Espera(
            nomeApp           = appName,
            minutosBloqueio   = COOLDOWN_MINUTES,
            segundosRestantes = secondsLeft,
        )
        viewModelScope.launch {
            while (secondsLeft > 0) {
                delay(1_000L)
                secondsLeft--
                _state.value = EstadoBloqueio.Bloqueado_Espera(
                    nomeApp           = appName,
                    minutosBloqueio   = COOLDOWN_MINUTES,
                    segundosRestantes = secondsLeft,
                )
            }
            attemptsLeft = MAX_ATTEMPTS
            _state.value = EstadoBloqueio.Bloqueado(appName)
        }
    }

    private fun currentAppName(): String = when (val s = _state.value) {
        is EstadoBloqueio.Bloqueado        -> s.nomeApp
        is EstadoBloqueio.Testando         -> s.nomeApp
        is EstadoBloqueio.Desbloqueado     -> s.nomeApp
        is EstadoBloqueio.Bloqueado_Espera -> s.nomeApp
    }

    private fun buildTapSequenceChallenge(): DesafioSobriedade.SequenciaToque {
        val sequence = (0 until TAP_TARGET_COUNT).shuffled().take(TAP_TARGET_COUNT)
        return DesafioSobriedade.SequenciaToque(
            sequencia       = sequence,
            tempoRestanteMs = COUNTDOWN_MS,
        )
    }
}
