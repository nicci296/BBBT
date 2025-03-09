package com.example.bbbt

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
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

    init {
        dialog.setContentView(R.layout.theme_creator_layout)
        setupColorPickers()
        setupSaveButton()
    }

    private fun setupColorPickers() {
        dialog.findViewById<View>(R.id.primaryColorPreview).setOnClickListener {
            showColorPicker(true)
        }

        dialog.findViewById<View>(R.id.secondaryColorPreview).setOnClickListener {
            showColorPicker(false)
        }
    }

    private fun showColorPicker(isPrimary: Boolean) {
        ColorPickerDialog.Builder(context)
            .setTitle(if (isPrimary) "Choose Primary Color" else "Choose Secondary Color")
            .setPositiveButton("Select", object : ColorEnvelopeListener {
                override fun onColorSelected(envelope: ColorEnvelope, fromUser: Boolean) {
                    val color = envelope.color
                    if (isPrimary) {
                        currentPrimaryColor = color
                        dialog.findViewById<View>(R.id.primaryColorPreview).setBackgroundColor(color)
                        themeManager.previewColors(currentPrimaryColor, currentSecondaryColor)
                    } else {
                        currentSecondaryColor = color
                        dialog.findViewById<View>(R.id.secondaryColorPreview).setBackgroundColor(color)
                        themeManager.previewColors(currentPrimaryColor, currentSecondaryColor)
                    }
                }
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
            .show()
    }

    private fun setupSaveButton() {
        dialog.findViewById<Button>(R.id.saveThemeButton).setOnClickListener {
            val themeName = dialog.findViewById<TextInputEditText>(R.id.themeNameInput).text.toString()
            if (themeName.isNotEmpty()) {
                val newTheme = CustomTheme(
                    name = themeName,
                    primaryColor = currentPrimaryColor,
                    secondaryColor = currentSecondaryColor
                )
                settingsManager.addCustomTheme(newTheme)
                onThemeSaved()
                dialog.dismiss()
            }
        }
    }

    fun show() {
        dialog.show()
    }
}