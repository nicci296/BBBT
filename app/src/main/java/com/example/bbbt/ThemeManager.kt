package com.example.bbbt

import android.app.Activity
import android.content.Context

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
    private var themeUpdateCallback: ((Int, Int, Int, Int, Int, Int) -> Unit)? = null

    fun applyTheme(theme: TeamTheme) {
        val colors = when (theme) {
            TeamTheme.LAKERS -> TeamThemeColors(
                primary = context.getColor(R.color.lakers_primary),
                secondary = context.getColor(R.color.lakers_secondary),
                background = context.getColor(R.color.lakers_background),
                textOnPrimary = context.getColor(R.color.lakers_text_on_primary),
                textOnSecondary = context.getColor(R.color.lakers_text_on_secondary),
                textOnBackground = context.getColor(R.color.lakers_text_on_background)
            )
            TeamTheme.MAVERICKS -> TeamThemeColors(
                primary = context.getColor(R.color.mavs_primary),
                secondary = context.getColor(R.color.mavs_secondary),
                background = context.getColor(R.color.mavs_background),
                textOnPrimary = context.getColor(R.color.mavs_text_on_primary),
                textOnSecondary = context.getColor(R.color.mavs_text_on_secondary),
                textOnBackground = context.getColor(R.color.mavs_text_on_background)
            )
            TeamTheme.WARRIORS -> TeamThemeColors(
                primary = context.getColor(R.color.warriors_primary),
                secondary = context.getColor(R.color.warriors_secondary),
                background = context.getColor(R.color.warriors_background),
                textOnPrimary = context.getColor(R.color.warriors_text_on_primary),
                textOnSecondary = context.getColor(R.color.warriors_text_on_secondary),
                textOnBackground = context.getColor(R.color.warriors_text_on_background)
            )
            TeamTheme.HEAT -> TeamThemeColors(
                primary = context.getColor(R.color.heat_primary),
                secondary = context.getColor(R.color.heat_secondary),
                background = context.getColor(R.color.heat_background),
                textOnPrimary = context.getColor(R.color.heat_text_on_primary),
                textOnSecondary = context.getColor(R.color.heat_text_on_secondary),
                textOnBackground = context.getColor(R.color.heat_text_on_background)
            )
            TeamTheme.JAZZ -> TeamThemeColors(
                primary = context.getColor(R.color.jazz_primary),
                secondary = context.getColor(R.color.jazz_secondary),
                background = context.getColor(R.color.jazz_background),
                textOnPrimary = context.getColor(R.color.jazz_text_on_primary),
                textOnSecondary = context.getColor(R.color.jazz_text_on_secondary),
                textOnBackground = context.getColor(R.color.jazz_text_on_background)
            )
            TeamTheme.BUCKS -> TeamThemeColors(
                primary = context.getColor(R.color.bucks_primary),
                secondary = context.getColor(R.color.bucks_secondary),
                background = context.getColor(R.color.bucks_background),
                textOnPrimary = context.getColor(R.color.bucks_text_on_primary),
                textOnSecondary = context.getColor(R.color.bucks_text_on_secondary),
                textOnBackground = context.getColor(R.color.bucks_text_on_background)
            )
            TeamTheme.CELTICS -> TeamThemeColors(
                primary = context.getColor(R.color.celtics_primary),
                secondary = context.getColor(R.color.celtics_secondary),
                background = context.getColor(R.color.celtics_background),
                textOnPrimary = context.getColor(R.color.celtics_text_on_primary),
                textOnSecondary = context.getColor(R.color.celtics_text_on_secondary),
                textOnBackground = context.getColor(R.color.celtics_text_on_background)
            )
            TeamTheme.NETS -> TeamThemeColors(
                primary = context.getColor(R.color.nets_primary),
                secondary = context.getColor(R.color.nets_secondary),
                background = context.getColor(R.color.nets_background),
                textOnPrimary = context.getColor(R.color.nets_text_on_primary),
                textOnSecondary = context.getColor(R.color.nets_text_on_secondary),
                textOnBackground = context.getColor(R.color.nets_text_on_background)
            )
            TeamTheme.SUNS -> TeamThemeColors(
                primary = context.getColor(R.color.suns_primary),
                secondary = context.getColor(R.color.suns_secondary),
                background = context.getColor(R.color.suns_background),
                textOnPrimary = context.getColor(R.color.suns_text_on_primary),
                textOnSecondary = context.getColor(R.color.suns_text_on_secondary),
                textOnBackground = context.getColor(R.color.suns_text_on_background)
            )
            TeamTheme.BULLS -> TeamThemeColors(
                primary = context.getColor(R.color.bulls_primary),
                secondary = context.getColor(R.color.bulls_secondary),
                background = context.getColor(R.color.bulls_background),
                textOnPrimary = context.getColor(R.color.bulls_text_on_primary),
                textOnSecondary = context.getColor(R.color.bulls_text_on_secondary),
                textOnBackground = context.getColor(R.color.bulls_text_on_background)
            )

        }
        applyColors(colors)
    }

    fun applyTheme(theme: CustomTheme) {
        themeUpdateCallback?.invoke(
            theme.primaryColor,
            theme.secondaryColor,
            theme.backgroundColor,
            theme.textOnPrimary,
            theme.textOnSecondary,
            theme.textOnBackground
        )
    }


    private fun applyColors(colors: TeamThemeColors) {
        val window = (context as Activity).window
        window.decorView.setBackgroundColor(colors.background)

        themeUpdateCallback?.invoke(
            colors.primary,
            colors.secondary,
            colors.background,
            colors.textOnPrimary,
            colors.textOnSecondary,
            colors.textOnBackground
        )
    }

    fun setThemeUpdateCallback(callback: (primary: Int, secondary: Int, background: Int, textOnPrimary: Int, textOnSecondary: Int, textOnBackground: Int) -> Unit) {
        themeUpdateCallback = callback
    }
}