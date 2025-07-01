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

        // Temporarily disable the listener to prevent triggering during setup
        radioGroup.setOnCheckedChangeListener(null)
        
        // Clear any previous selection
        radioGroup.clearCheck()

        // Set initial selection based on current theme type and selection
        if (settingsManager.selectedThemeType == "preset") {
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
                
                // Always start unchecked - will be set correctly below
                isChecked = false
                
                // Add direct click listener to ensure proper handling
                setOnClickListener {
                    // Temporarily disable listener to prevent conflicts
                    radioGroup.setOnCheckedChangeListener(null)
                    
                    // Apply the custom theme
                    applyCustomTheme(radioGroup, customTheme, this)
                    
                    // Re-enable the listener
                    setupRadioGroupListener(radioGroup)
                }
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

        // After all radio buttons are added, set the correct selection
        // Since RadioGroup doesn't handle nested RadioButtons properly, 
        // we need to manually manage the selection state
        setCorrectSelection(radioGroup)

        // Set up the listener after all UI setup is complete
        setupRadioGroupListener(radioGroup)
    }

    private fun setupRadioGroupListener(radioGroup: RadioGroup) {
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            // Temporarily disable listener to prevent recursion
            radioGroup.setOnCheckedChangeListener(null)
            
            when (checkedId) {
                // Setup preset themes
                R.id.lakersTheme -> {
                    applyPresetTheme(radioGroup, TeamTheme.LAKERS)
                }
                R.id.mavsTheme -> {
                    applyPresetTheme(radioGroup, TeamTheme.MAVERICKS)
                }
                R.id.celticsTheme -> {
                    applyPresetTheme(radioGroup, TeamTheme.CELTICS)
                }
                R.id.warriorsTheme -> {
                    applyPresetTheme(radioGroup, TeamTheme.WARRIORS)
                }
                R.id.bullsTheme -> {
                    applyPresetTheme(radioGroup, TeamTheme.BULLS)
                }
                R.id.heatTheme -> {
                    applyPresetTheme(radioGroup, TeamTheme.HEAT)
                }
                R.id.netsTheme -> {
                    applyPresetTheme(radioGroup, TeamTheme.NETS)
                }
                R.id.sunsTheme -> {
                    applyPresetTheme(radioGroup, TeamTheme.SUNS)
                }
                R.id.bucksTheme -> {
                    applyPresetTheme(radioGroup, TeamTheme.BUCKS)
                }
                R.id.jazzTheme -> {
                    applyPresetTheme(radioGroup, TeamTheme.JAZZ)
                }
                else -> {
                    // Custom theme selection - this is now handled by direct click listeners
                    // but keeping this as fallback
                    val radioButton = radioGroup.findViewById<RadioButton>(checkedId)
                    if (radioButton != null) {
                        val buttonText = radioButton.text?.toString()
                        if (buttonText != null) {
                            val customTheme = settingsManager.customThemes.find { it.name == buttonText }
                            customTheme?.let { 
                                applyCustomTheme(radioGroup, it, radioButton)
                            }
                        }
                    }
                }
            }
            
            // Re-enable the listener
            setupRadioGroupListener(radioGroup)
        }
    }

    private fun setCorrectSelection(radioGroup: RadioGroup) {
        // First, clear all selections manually
        clearAllSelections(radioGroup)
        
        if (settingsManager.selectedThemeType == "preset") {
            // For preset themes, use RadioGroup's check method as normal
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
        } else if (settingsManager.selectedThemeType == "custom") {
            val selectedCustomThemeName = settingsManager.selectedCustomThemeName
            if (selectedCustomThemeName != null) {
                // For custom themes, manually set the checked state
                for (i in 0 until radioGroup.childCount) {
                    val child = radioGroup.getChildAt(i)
                    if (child is LinearLayout) {
                        val radioButton = child.getChildAt(0) as? RadioButton
                        if (radioButton?.text?.toString() == selectedCustomThemeName) {
                            radioButton.isChecked = true
                            // Also set this as the checked radio button in the group
                            radioGroup.check(radioButton.id)
                            break
                        }
                    }
                }
            }
        }
    }

    private fun clearAllSelections(radioGroup: RadioGroup) {
        // Clear RadioGroup selection (preset themes)
        radioGroup.clearCheck()
        
        // Clear custom radio buttons manually since they're in containers
        clearCustomThemeSelections(radioGroup)
    }

    private fun clearCustomThemeSelections(radioGroup: RadioGroup) {
        // Manually clear custom radio buttons since they're in containers
        for (i in 0 until radioGroup.childCount) {
            val child = radioGroup.getChildAt(i)
            if (child is LinearLayout) {
                val radioButton = child.getChildAt(0) as? RadioButton
                radioButton?.isChecked = false
            }
        }
    }

    private fun applyPresetTheme(radioGroup: RadioGroup, theme: TeamTheme) {
        // Get the correct preset radio button ID
        val presetRadioId = when (theme) {
            TeamTheme.LAKERS -> R.id.lakersTheme
            TeamTheme.MAVERICKS -> R.id.mavsTheme
            TeamTheme.CELTICS -> R.id.celticsTheme
            TeamTheme.WARRIORS -> R.id.warriorsTheme
            TeamTheme.BULLS -> R.id.bullsTheme
            TeamTheme.HEAT -> R.id.heatTheme
            TeamTheme.NETS -> R.id.netsTheme
            TeamTheme.SUNS -> R.id.sunsTheme
            TeamTheme.BUCKS -> R.id.bucksTheme
            TeamTheme.JAZZ -> R.id.jazzTheme
        }
        
        // Clear all manually since RadioGroup.check() will handle the new selection
        clearCustomThemeSelections(radioGroup)
        
        // Use RadioGroup's check method to properly manage selection
        // (listener is already disabled when this is called)
        radioGroup.check(presetRadioId)
        
        // Update settings and apply theme
        settingsManager.setSelectedPresetTheme(theme)
        themeManager.applyTheme(theme)
    }

    private fun applyCustomTheme(radioGroup: RadioGroup, customTheme: CustomTheme, radioButton: RadioButton) {
        // Clear RadioGroup selection (preset themes)
        radioGroup.clearCheck()
        
        // Clear all custom theme selections manually
        clearCustomThemeSelections(radioGroup)
        
        // Set this custom radio button as checked
        radioButton.isChecked = true
        
        // Update settings and apply theme
        settingsManager.setSelectedCustomTheme(customTheme)
        themeManager.applyTheme(customTheme)
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