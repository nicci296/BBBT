package com.example.bbbt

import android.app.Dialog
import android.content.Context
import android.widget.TextView
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsDialog(private val context: Context, private val settingsManager: SettingsManager) {
    private val dialog = Dialog(context)

    init {
        dialog.setContentView(R.layout.settings_layout)
        setupSwitches()
        setupTips()
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

    private fun setupTips() {
        val tips = context.resources.getStringArray(R.array.tips)
        val tipsText = StringBuilder("• ").append(tips.joinToString("\n• "))
        dialog.findViewById<TextView>(R.id.tipsText).text = tipsText
    }

    fun show() {
        dialog.show()
    }
}