package com.nova.launcher

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue

class NovaSettings(private val prefs: SharedPreferences) {

    var accentIndex by mutableIntStateOf(
        prefs.getInt(KEY_ACCENT, 0).coerceIn(0, NovaTheme.accents.lastIndex)
    )
        private set

    var backgroundIndex by mutableIntStateOf(
        prefs.getInt(KEY_BACKGROUND, 0).coerceIn(0, NovaTheme.backgrounds.lastIndex)
    )
        private set

    fun selectAccent(index: Int) {
        accentIndex = index.coerceIn(0, NovaTheme.accents.lastIndex)
        prefs.edit().putInt(KEY_ACCENT, accentIndex).apply()
    }

    fun selectBackground(index: Int) {
        backgroundIndex = index.coerceIn(0, NovaTheme.backgrounds.lastIndex)
        prefs.edit().putInt(KEY_BACKGROUND, backgroundIndex).apply()
    }

    private companion object {
        const val KEY_ACCENT = "accent"
        const val KEY_BACKGROUND = "background"
    }
}
