package com.example.bbbt

import android.content.Context
import androidx.core.content.edit

class SettingsManager(context: Context) {
    private val prefs = context.getSharedPreferences("timer_settings", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_VIBRATION = "vibration_enabled"
        private const val KEY_PULSE = "pulse_enabled"
    }

    var isVibrationEnabled: Boolean
        get() = prefs.getBoolean(KEY_VIBRATION, true)
        set(value) = prefs.edit() { putBoolean(KEY_VIBRATION, value) }

    var isPulseEnabled: Boolean
        get() = prefs.getBoolean(KEY_PULSE, true)
        set(value) = prefs.edit() { putBoolean(KEY_PULSE, value) }
}