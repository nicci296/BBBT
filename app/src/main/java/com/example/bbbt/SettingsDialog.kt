package com.example.bbbt

import android.app.Dialog
import android.content.Context
import android.widget.RadioGroup
import android.widget.TextView
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsDialog(
    private val context: Context,
    private val settingsManager: SettingsManager,
    private val themeManager: ThemeManager
) {
    private val dialog = Dialog(context)

    init {
        dialog.setContentView(R.layout.settings_layout)
        setupSwitches()
        setupThemeSelection()
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

    private fun setupThemeSelection() {
        val radioGroup = dialog.findViewById<RadioGroup>(R.id.themeRadioGroup)

        // Set initial selection
        when (settingsManager.selectedTheme) {
            TeamTheme.LAKERS -> radioGroup.check(R.id.lakersTheme)
            TeamTheme.MAVERICKS -> radioGroup.check(R.id.mavsTheme)
            TeamTheme.CELTICS -> radioGroup.check(R.id.celticsTheme)
            TeamTheme.WARRIORS -> radioGroup.check(R.id.warriorsTheme)
        }

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedTheme = when (checkedId) {
                R.id.lakersTheme -> TeamTheme.LAKERS
                R.id.mavsTheme -> TeamTheme.MAVERICKS
                R.id.celticsTheme -> TeamTheme.CELTICS
                R.id.warriorsTheme -> TeamTheme.WARRIORS
                else -> TeamTheme.LAKERS
            }
            settingsManager.selectedTheme = selectedTheme
            themeManager.applyTheme(selectedTheme)
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