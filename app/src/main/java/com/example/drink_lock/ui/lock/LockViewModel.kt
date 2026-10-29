package com.example.drink_lock.ui.lock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drink_lock.data.BlockerConfig
import com.example.drink_lock.data.BlockerPreferences
import com.example.drink_lock.domain.model.DesafioSobriedade
import com.example.drink_lock.domain.model.EstadoBloqueio
import com.example.drink_lock.domain.model.ResultadoDesafio
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val COUNTDOWN_TICK_MS = 250L
private const val TAP_TARGET_COUNT = 4

@HiltViewModel
class LockViewModel @Inject constructor(
    private val prefs: BlockerPreferences,
) : ViewModel() {

    private val _state = MutableStateFlow<EstadoBloqueio>(EstadoBloqueio.Bloqueado(nomeApp = ""))
    val state: StateFlow<EstadoBloqueio> = _state.asStateFlow()

    private var appName = ""
    private var pacote: String? = null
    private var attemptsLeft = BlockerConfig.MAX_ATTEMPTS
    private var timerJob: Job? = null
    private var cooldownJob: Job? = null

    private val persistir: Boolean get() = pacote != null

    fun init(appName: String, packageName: String? = null) {
        timerJob?.cancel()
        cooldownJob?.cancel()
        this.appName = appName
        this.pacote = packageName

        if (!persistir) {
            attemptsLeft = BlockerConfig.MAX_ATTEMPTS
            _state.value = EstadoBloqueio.Bloqueado(appName)
            return
        }

        val restante = prefs.cooldownRemainingMs()
        if (restante > 0L) {
            showCooldown(restante)
        } else {
            prefs.clearCooldownIfExpired()
            attemptsLeft = prefs.attemptsLeft()
            _state.value = EstadoBloqueio.Bloqueado(appName)
        }
    }

    fun startChallenge() {
        if (_state.value !is EstadoBloqueio.Bloqueado) return
        val challenge = buildTapSequenceChallenge()
        _state.value = EstadoBloqueio.Testando(appName, challenge, attemptsLeft)
        startCountdown(challenge)
    }

    fun onTargetTapped(targetIndex: Int) {
        val current = _state.value as? EstadoBloqueio.Testando ?: return
        val challenge = current.desafio as? DesafioSobriedade.SequenciaToque ?: return

        val esperado = challenge.sequencia.getOrNull(challenge.progressoUsuario.size)
        if (targetIndex != esperado) {
            handleResult(ResultadoDesafio.REPROVADO)
            return
        }

        val novoProgresso = challenge.progressoUsuario + targetIndex
        if (novoProgresso == challenge.sequencia) {
            handleResult(ResultadoDesafio.APROVADO)
        } else {
            _state.value = current.copy(desafio = challenge.copy(progressoUsuario = novoProgresso))
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
                _state.value = current.copy(desafio = ch.copy(tempoRestanteMs = remaining.coerceAtLeast(0L)))
            }
            if (_state.value is EstadoBloqueio.Testando) handleResult(ResultadoDesafio.TEMPO_ESGOTADO)
        }
    }

    private fun handleResult(result: ResultadoDesafio) {
        timerJob?.cancel()

        when (result) {
            ResultadoDesafio.APROVADO -> {
                pacote?.let {
                    prefs.resetAttempts()
                    prefs.unlockFor(it, BlockerConfig.UNLOCK_DURATION_MS)
                }
                _state.value = EstadoBloqueio.Desbloqueado(appName)
            }

            ResultadoDesafio.REPROVADO, ResultadoDesafio.TEMPO_ESGOTADO -> {
                attemptsLeft--
                if (persistir) prefs.setAttemptsLeft(attemptsLeft.coerceAtLeast(0))

                if (attemptsLeft <= 0) {
                    if (persistir) {
                        prefs.startCooldown(System.currentTimeMillis() + BlockerConfig.COOLDOWN_MS)
                    }
                    showCooldown(BlockerConfig.COOLDOWN_MS)
                } else {
                    _state.value = EstadoBloqueio.Bloqueado(appName)
                }
            }
        }
    }

    private fun showCooldown(totalMs: Long) {
        cooldownJob?.cancel()
        val fim = System.currentTimeMillis() + totalMs
        val minutos = (BlockerConfig.COOLDOWN_MS / 60_000L).toInt()

        cooldownJob = viewModelScope.launch {
            while (true) {
                val restanteS = ((fim - System.currentTimeMillis() + 999L) / 1_000L).toInt()
                if (restanteS <= 0) break
                _state.value = EstadoBloqueio.Bloqueado_Espera(appName, minutos, restanteS)
                delay(1_000L)
            }
            if (persistir) prefs.clearCooldownIfExpired()
            attemptsLeft = BlockerConfig.MAX_ATTEMPTS
            _state.value = EstadoBloqueio.Bloqueado(appName)
        }
    }

    private fun buildTapSequenceChallenge(): DesafioSobriedade.SequenciaToque {
        val sequence = (0 until TAP_TARGET_COUNT).shuffled()
        return DesafioSobriedade.SequenciaToque(
            sequencia = sequence,
            tempoRestanteMs = BlockerConfig.COUNTDOWN_MS,
        )
    }
}
