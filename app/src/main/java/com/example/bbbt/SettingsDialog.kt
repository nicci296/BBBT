package com.example.bbbt

import android.app.Dialog
import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RadioButton
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
        setupCreateThemeButton()
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

        // Set initial selection based on current theme
        when (settingsManager.selectedTheme) {
            TeamTheme.LAKERS -> radioGroup.check(R.id.lakersTheme)
            TeamTheme.MAVERICKS -> radioGroup.check(R.id.mavsTheme)
            TeamTheme.CELTICS -> radioGroup.check(R.id.celticsTheme)
            TeamTheme.WARRIORS -> radioGroup.check(R.id.warriorsTheme)
            TeamTheme.BULLS -> radioGroup.check(R.id.bullsTheme)
            TeamTheme.HEAT -> radioGroup.check(R.id.heatTheme)
            TeamTheme.NETS -> radioGroup.check(R.id.netsTheme)
            TeamTheme.SUNS -> radioGroup.check(R.id.sunsTheme)
            TeamTheme.BUCKS -> radioGroup.check(R.id.bucksTheme)
            TeamTheme.JAZZ -> radioGroup.check(R.id.jazzTheme)
        }

        // Clear existing custom themes before adding them again
        val childCount = radioGroup.childCount
        for (i in childCount - 1 downTo 0) {
            val view = radioGroup.getChildAt(i)
            if (view.id !in listOf(
                    R.id.lakersTheme,
                    R.id.mavsTheme,
                    R.id.celticsTheme,
                    R.id.warriorsTheme,
                    R.id.bullsTheme,
                    R.id.heatTheme,
                    R.id.netsTheme,
                    R.id.sunsTheme,
                    R.id.bucksTheme,
                    R.id.jazzTheme
            )) {
                radioGroup.removeViewAt(i)
            }
        }

        // Add custom themes dynamically
        settingsManager.customThemes.forEach { customTheme ->
            val container = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.MATCH_PARENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT
                )
            }

            val radioButton = RadioButton(context).apply {
                id = View.generateViewId()
                text = customTheme.name
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
            }

            val deleteButton = ImageButton(context).apply {
                setImageResource(R.drawable.ic_delete)
                setBackgroundResource(android.R.color.transparent)
                setOnClickListener {
                    settingsManager.removeCustomTheme(customTheme)
                    setupThemeSelection()
                }
            }

            container.addView(radioButton)
            container.addView(deleteButton)
            radioGroup.addView(container)
        }

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                // Setup preset themes
                R.id.lakersTheme -> {
                    settingsManager.selectedTheme = TeamTheme.LAKERS
                    themeManager.applyTheme(TeamTheme.LAKERS)
                }
                R.id.mavsTheme -> {
                    settingsManager.selectedTheme = TeamTheme.MAVERICKS
                    themeManager.applyTheme(TeamTheme.MAVERICKS)
                }
                R.id.celticsTheme -> {
                    settingsManager.selectedTheme = TeamTheme.CELTICS
                    themeManager.applyTheme(TeamTheme.CELTICS)
                }
                R.id.warriorsTheme -> {
                    settingsManager.selectedTheme = TeamTheme.WARRIORS
                    themeManager.applyTheme(TeamTheme.WARRIORS)
                }
                R.id.bullsTheme -> {
                    settingsManager.selectedTheme = TeamTheme.BULLS
                    themeManager.applyTheme(TeamTheme.BULLS)
                }
                R.id.heatTheme -> {
                    settingsManager.selectedTheme = TeamTheme.HEAT
                    themeManager.applyTheme(TeamTheme.HEAT)
                }
                R.id.netsTheme -> {
                    settingsManager.selectedTheme = TeamTheme.NETS
                    themeManager.applyTheme(TeamTheme.NETS)
                }
                R.id.sunsTheme -> {
                    settingsManager.selectedTheme = TeamTheme.SUNS
                    themeManager.applyTheme(TeamTheme.SUNS)
                }
                R.id.bucksTheme -> {
                    settingsManager.selectedTheme = TeamTheme.BUCKS
                    themeManager.applyTheme(TeamTheme.BUCKS)
                }
                R.id.jazzTheme -> {
                    settingsManager.selectedTheme = TeamTheme.JAZZ
                    themeManager.applyTheme(TeamTheme.JAZZ)
                }
                else -> {
                    val radioButton = radioGroup.findViewById<RadioButton>(checkedId)
                    val customTheme = settingsManager.customThemes.find { it.name == radioButton.text }
                    customTheme?.let { themeManager.applyTheme(it) }
                }
            }
        }
    }

    private fun setupCreateThemeButton() {
        dialog.findViewById<Button>(R.id.createThemeButton).setOnClickListener {
            ThemeCreatorDialog(
                context,
                settingsManager,
                themeManager
            ) {
                // Refresh the theme list
                setupThemeSelection()
            }.show()
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