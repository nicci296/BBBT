package com.example.bbbt

import android.app.Dialog
import android.content.Context
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsDialog(context: Context, private val settingsManager: SettingsManager) {
    private val dialog = Dialog(context)

    init {
        dialog.setContentView(R.layout.settings_layout)
        setupSwitches()
    }

    private fun setupSwitches() {
        dialog.findViewById<SwitchMaterial>(R.id.vibrationSwitch).apply {
            isChecked = settingsManager.isVibrationEnabled
            setOnCheckedChangeListener { _, isChecked ->
                settingsManager.isVibrationEnabled = isChecked
            }
        }

        dialog.findViewById<SwitchMaterial>(R.id.pulseAnimationSwitch).apply {
            isChecked = settingsManager.isPulseEnabled
            setOnCheckedChangeListener { _, isChecked ->
                settingsManager.isPulseEnabled = isChecked
            }
        }
    }

    fun show() {
        dialog.show()
    }
}