package com.example.bbbt

import android.content.Context
import androidx.core.content.ContextCompat

enum class TeamTheme {
    LAKERS,
    MAVERICKS,
    CELTICS,
    WARRIORS,
    BULLS,
    HEAT,
    NETS,
    SUNS,
    BUCKS,
    JAZZ
}

class ThemeManager(private val context: Context) {
    private var themeUpdateCallback: ((Int, Int) -> Unit)? = null

    fun setThemeUpdateCallback(callback: (primaryColor: Int, secondaryColor: Int) -> Unit) {
        themeUpdateCallback = callback
    }

    fun applyTheme(theme: TeamTheme) {
        when (theme) {
            TeamTheme.LAKERS -> applyColors(R.color.lakers_primary, R.color.lakers_secondary)
            TeamTheme.MAVERICKS -> applyColors(R.color.mavs_primary, R.color.mavs_secondary)
            TeamTheme.CELTICS -> applyColors(R.color.celtics_primary, R.color.celtics_secondary)
            TeamTheme.WARRIORS -> applyColors(R.color.warriors_primary, R.color.warriors_secondary)
            TeamTheme.BULLS -> applyColors(R.color.bulls_primary, R.color.bulls_secondary)
            TeamTheme.HEAT -> applyColors(R.color.heat_primary, R.color.heat_secondary)
            TeamTheme.NETS -> applyColors(R.color.nets_primary, R.color.nets_secondary)
            TeamTheme.SUNS -> applyColors(R.color.suns_primary, R.color.suns_secondary)
            TeamTheme.BUCKS -> applyColors(R.color.bucks_primary, R.color.bucks_secondary)
            TeamTheme.JAZZ -> applyColors(R.color.jazz_primary, R.color.jazz_secondary)
        }
    }

    fun applyTheme(theme: CustomTheme) {
        themeUpdateCallback?.invoke(theme.primaryColor, theme.secondaryColor)
    }

    fun previewColors(primary: Int, secondary: Int) {
        themeUpdateCallback?.invoke(primary, secondary)
    }

    private fun applyColors(primaryColorRes: Int, secondaryColorRes: Int) {
        val primary = ContextCompat.getColor(context, primaryColorRes)
        val secondary = ContextCompat.getColor(context, secondaryColorRes)
        themeUpdateCallback?.invoke(primary, secondary)
    }
}