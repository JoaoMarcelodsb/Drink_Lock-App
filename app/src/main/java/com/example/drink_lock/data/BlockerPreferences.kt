package com.example.drink_lock.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

object BlockerConfig {
    const val MAX_ATTEMPTS = 3
    const val COUNTDOWN_MS = 15_000L
    const val COOLDOWN_MS = 5 * 60_000L
    const val UNLOCK_DURATION_MS = 15 * 60_000L
}

@Singleton
class BlockerPreferences @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val sp = context.getSharedPreferences("drink_lock_blocker", Context.MODE_PRIVATE)

    fun blockedPackages(): Set<String> =
        sp.getStringSet(KEY_BLOCKED, null)?.toSet() ?: emptySet()

    fun isBlocked(pacote: String): Boolean =
        sp.getStringSet(KEY_BLOCKED, null)?.contains(pacote) == true

    fun setBlocked(pacote: String, bloquear: Boolean) {
        val novo = blockedPackages().toMutableSet()
        if (bloquear) novo.add(pacote) else novo.remove(pacote)
        sp.edit().putStringSet(KEY_BLOCKED, novo).apply()
    }

    fun unlockFor(pacote: String, duracaoMs: Long) {
        sp.edit().putLong(KEY_UNLOCK + pacote, System.currentTimeMillis() + duracaoMs).apply()
    }

    fun isTemporarilyUnlocked(pacote: String): Boolean =
        System.currentTimeMillis() < sp.getLong(KEY_UNLOCK + pacote, 0L)

    fun attemptsLeft(): Int = sp.getInt(KEY_ATTEMPTS, BlockerConfig.MAX_ATTEMPTS)

    fun setAttemptsLeft(n: Int) {
        sp.edit().putInt(KEY_ATTEMPTS, n).apply()
    }

    fun resetAttempts() = setAttemptsLeft(BlockerConfig.MAX_ATTEMPTS)

    fun startCooldown(ateMs: Long) {
        sp.edit().putLong(KEY_COOLDOWN, ateMs).apply()
    }

    fun cooldownRemainingMs(): Long =
        (sp.getLong(KEY_COOLDOWN, 0L) - System.currentTimeMillis()).coerceAtLeast(0L)

    fun clearCooldownIfExpired() {
        val ate = sp.getLong(KEY_COOLDOWN, 0L)
        if (ate != 0L && System.currentTimeMillis() >= ate) {
            sp.edit().putLong(KEY_COOLDOWN, 0L).putInt(KEY_ATTEMPTS, BlockerConfig.MAX_ATTEMPTS).apply()
        }
    }

    private companion object {
        const val KEY_BLOCKED = "blocked_packages"
        const val KEY_UNLOCK = "unlock_until_"
        const val KEY_ATTEMPTS = "attempts_left"
        const val KEY_COOLDOWN = "cooldown_until"
    }
}
