package com.example.bbbt

import android.content.Context

enum class TeamTheme {
    LAKERS, MAVERICKS, CELTICS, WARRIORS
}

class ThemeManager(private val context: Context) {
    fun applyTheme(theme: TeamTheme) {
        when (theme) {
            TeamTheme.LAKERS -> {
                applyColors(R.color.lakers_primary, R.color.lakers_secondary)
            }
            TeamTheme.MAVERICKS -> {
                applyColors(R.color.mavs_primary, R.color.mavs_secondary)
            }
            TeamTheme.CELTICS -> {
                applyColors(R.color.celtics_primary, R.color.celtics_secondary)
            }
            TeamTheme.WARRIORS -> {
                applyColors(R.color.warriors_primary, R.color.warriors_secondary)
            }
        }
    }

    private fun applyColors(primaryColorRes: Int, secondaryColorRes: Int) {
        // Implementation coming in next step
    }
}