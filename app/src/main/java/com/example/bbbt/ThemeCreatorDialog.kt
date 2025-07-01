package com.example.bbbt

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import com.skydoves.colorpickerview.ColorEnvelope
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener

class ThemeCreatorDialog(
    private val context: Context,
    private val settingsManager: SettingsManager,
    private val themeManager: ThemeManager,
    private val onThemeSaved: () -> Unit
) {
    private val dialog = Dialog(context)
    private var currentPrimaryColor = Color.BLUE
    private var currentSecondaryColor = Color.MAGENTA
    private var currentBackgroundColor = Color.WHITE
    private var currentTextOnPrimary = Color.WHITE
    private var currentTextOnSecondary = Color.BLACK
    private var currentTextOnBackground = Color.BLACK

    init {
        dialog.setContentView(R.layout.theme_creator_layout)
        setupColorPickers()
        setupSaveButton()
    }

    private enum class ColorType {
        PRIMARY, SECONDARY, BACKGROUND
    }

    private fun setupColorPickers() {
        dialog.findViewById<View>(R.id.primaryColorPreview).setOnClickListener {
            showColorPicker(ColorType.PRIMARY)
        }
        dialog.findViewById<View>(R.id.secondaryColorPreview).setOnClickListener {
            showColorPicker(ColorType.SECONDARY)
        }
        dialog.findViewById<View>(R.id.backgroundColorPreview).setOnClickListener {
            showColorPicker(ColorType.BACKGROUND)
        }

    }

    private fun showColorPicker(colorType: ColorType) {
        ColorPickerDialog.Builder(context)
            .setTitle(getColorPickerTitle(colorType))
            .setPositiveButton("Select", object : ColorEnvelopeListener {
                override fun onColorSelected(envelope: ColorEnvelope, fromUser: Boolean) {
                    val color = envelope.color
                    updateColor(colorType, color)
                    updatePreview()
                }
            })
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun updateColor(colorType: ColorType, color: Int) {
        when (colorType) {
            ColorType.PRIMARY -> {
                currentPrimaryColor = color
                currentTextOnPrimary = getContrastColor(color)
            }
            ColorType.SECONDARY -> {
                currentSecondaryColor = color
                currentTextOnSecondary = getContrastColor(color)
            }
            ColorType.BACKGROUND -> {
                currentBackgroundColor = color
                currentTextOnBackground = getContrastColor(color)
            }
        }
    }

    private fun getContrastColor(backgroundColor: Int): Int {
        // Calculate luminance and return appropriate contrast color
        val luminance = (0.299 * Color.red(backgroundColor) +
                0.587 * Color.green(backgroundColor) +
                0.114 * Color.blue(backgroundColor)) / 255
        return if (luminance > 0.5) Color.BLACK else Color.WHITE
    }

    private fun updatePreview() {
        // Update the preview views in the dialog
        dialog.findViewById<View>(R.id.primaryColorPreview).setBackgroundColor(currentPrimaryColor)
        dialog.findViewById<View>(R.id.secondaryColorPreview).setBackgroundColor(currentSecondaryColor)
        dialog.findViewById<View>(R.id.backgroundColorPreview).setBackgroundColor(currentBackgroundColor)
    }


    private fun setupSaveButton() {
        dialog.findViewById<Button>(R.id.saveThemeButton).setOnClickListener {
            val themeName = dialog.findViewById<TextInputEditText>(R.id.themeNameInput).text.toString().trim()
            if (themeName.isNotEmpty()) {
                try {
                    val newTheme = CustomTheme(
                        name = themeName,
                        primaryColor = currentPrimaryColor,
                        secondaryColor = currentSecondaryColor,
                        backgroundColor = currentBackgroundColor,
                        textOnPrimary = currentTextOnPrimary,
                        textOnSecondary = currentTextOnSecondary,
                        textOnBackground = currentTextOnBackground
                    )
                    
                    // Save the theme first
                    settingsManager.addCustomTheme(newTheme)
                    
                    // Then set it as selected
                    settingsManager.setSelectedCustomTheme(newTheme)
                    
                    // Apply the theme
                    themeManager.applyTheme(newTheme)
                    
                    // Dismiss dialog first to prevent UI conflicts
                    dialog.dismiss()
                    
                    // Then refresh the parent dialog
                    onThemeSaved()
                    
                } catch (e: Exception) {
                    // Handle any errors gracefully
                    e.printStackTrace()
                    // Could show a toast or error message here
                }
            }
        }
    }

    private fun getColorPickerTitle(colorType: ColorType): String {
        return when (colorType) {
            ColorType.PRIMARY -> "Wähle Primärfarbe"
            ColorType.SECONDARY -> "Wähle Sekundärfarbe"
            ColorType.BACKGROUND -> "Wähle Hintergrundfarbe"
        }
    }

    fun show() {
        dialog.show()
    }
}