package com.example.drink_lock.ui.protection

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drink_lock.data.BlockerPreferences
import com.example.drink_lock.data.InstalledAppsRepository
import com.example.drink_lock.data.ProtectionMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class AppItem(
    val packageName: String,
    val label: String,
    val icon: Bitmap?,
    val bloqueado: Boolean,
)

data class ProtectionUiState(
    val servicoAtivo: Boolean = false,
    val carregando: Boolean = false,
    val apps: List<AppItem> = emptyList(),
    val totalBloqueados: Int = 0,
)

@HiltViewModel
class ProtectionViewModel @Inject constructor(
    monitor: ProtectionMonitor,
    private val appsRepository: InstalledAppsRepository,
    private val prefs: BlockerPreferences,
) : ViewModel() {

    private val _apps = MutableStateFlow<List<AppItem>>(emptyList())
    private val _carregando = MutableStateFlow(false)
    private val _total = MutableStateFlow(prefs.blockedPackages().size)

    val uiState: StateFlow<ProtectionUiState> = combine(
        monitor.servicoAtivo, _apps, _carregando, _total,
    ) { ativo, apps, carregando, total ->
        ProtectionUiState(ativo, carregando, apps, total)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ProtectionUiState(
            servicoAtivo = monitor.servicoAtivo.value,
            totalBloqueados = _total.value,
        ),
    )

    fun carregarApps() {
        if (_apps.value.isNotEmpty() || _carregando.value) return

        viewModelScope.launch {
            _carregando.value = true
            val bloqueados = prefs.blockedPackages()

            val lista = withContext(Dispatchers.IO) {
                appsRepository.listarApps()
                    .map { AppItem(it.packageName, it.label, it.icon, it.packageName in bloqueados) }
                    .sortedWith(
                        compareByDescending<AppItem> { it.bloqueado }
                            .thenBy { it.label.lowercase() }
                    )
            }

            _apps.value = lista
            _carregando.value = false
        }
    }

    fun onToggle(pacote: String, bloquear: Boolean) {
        prefs.setBlocked(pacote, bloquear)
        _apps.update { lista ->
            lista.map { if (it.packageName == pacote) it.copy(bloqueado = bloquear) else it }
        }
        _total.value = prefs.blockedPackages().size
    }
}
