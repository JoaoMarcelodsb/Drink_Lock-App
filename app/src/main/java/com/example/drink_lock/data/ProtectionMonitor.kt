package com.example.drink_lock.data

import android.content.ComponentName
import android.content.Context
import android.provider.Settings
import com.example.drink_lock.service.AppBlockerService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProtectionMonitor @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {
    private val _servicoAtivo = MutableStateFlow(calcular())
    val servicoAtivo: StateFlow<Boolean> = _servicoAtivo.asStateFlow()

    fun refresh() {
        _servicoAtivo.value = calcular()
    }

    private fun calcular(): Boolean {
        val lista = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES,
        ) ?: return false

        val componente = ComponentName(context, AppBlockerService::class.java)
        val formasAceitas = listOf(componente.flattenToString(), componente.flattenToShortString())

        return lista.split(':').any { item ->
            formasAceitas.any { it.equals(item, ignoreCase = true) }
        }
    }
}
