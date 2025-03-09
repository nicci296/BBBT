package com.example.bbbt

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class SettingsManager(context: Context) {
    private val prefs = context.getSharedPreferences("timer_settings", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val KEY_THEME = "selected_theme"
        private const val KEY_CUSTOM_THEMES = "custom_themes"
        private const val KEY_VIBRATION = "vibration_enabled"
        private const val KEY_PULSE = "pulse_enabled"
    }

    var isVibrationEnabled: Boolean
        get() = prefs.getBoolean(KEY_VIBRATION, true)
        set(value) = prefs.edit() { putBoolean(KEY_VIBRATION, value) }

    var isPulseEnabled: Boolean
        get() = prefs.getBoolean(KEY_PULSE, true)
        set(value) = prefs.edit() { putBoolean(KEY_PULSE, value) }

    var selectedTheme: TeamTheme
        get() = TeamTheme.valueOf(prefs.getString(KEY_THEME, TeamTheme.LAKERS.name) ?: TeamTheme.LAKERS.name)
        set(value) = prefs.edit() { putString(KEY_THEME, value.name) }

    var customThemes: List<CustomTheme>
        get() {
            val json = prefs.getString(KEY_CUSTOM_THEMES, "[]")
            val type = object : TypeToken<List<CustomTheme>>() {}.type
            return gson.fromJson(json, type)
        }
        set(value) {
            val json = gson.toJson(value)
            prefs.edit { putString(KEY_CUSTOM_THEMES, json) }
        }

    fun addCustomTheme(theme: CustomTheme) {
        val currentThemes = customThemes.toMutableList()
        currentThemes.add(theme)
        customThemes = currentThemes
    }
}