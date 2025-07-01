package com.example.bbbt

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var timerManager: TimerManager
    private lateinit var timerDisplay: TextView
    private lateinit var vibrationManager: VibrationManager
    private lateinit var settingsManager: SettingsManager
    private lateinit var themeManager: ThemeManager
    private lateinit var pulseAnimation: ObjectAnimator
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vibrationManager = VibrationManager(this)
        settingsManager = SettingsManager(this)
        themeManager = ThemeManager(this)

        // Apply saved theme on startup
        setupThemeCallback()
        applySavedTheme()

        setupViews()

        pulseAnimation = createPulseAnimation()
        setupTimer()
        setupClickListeners()

        findViewById<ImageButton>(R.id.settingsButton).setOnClickListener {
            SettingsDialog(this, settingsManager, themeManager).show()
        }
    }

    private fun setupViews() {
        timerDisplay = findViewById(R.id.timerDisplay)
    }

    private fun setupTimer() {
        timerManager = TimerManager()
        timerManager.setCallbacks(
            timeCallback = { time ->
                timerDisplay.text = String.format("%.1f", time)
                val remaining = String.format("%.1f", timerManager.maxTime - time)
                findViewById<TextView>(R.id.remainingTimeDisplay).text = "Verbleibend: $remaining"

                if (time >= 19.0 && settingsManager.isPulseEnabled) {
                    if (!pulseAnimation.isRunning) {
                        pulseAnimation.start()
                    }
                } else {
                    pulseAnimation.cancel()
                }
            },
            finishedCallback = {
                pulseAnimation.cancel()
                timerDisplay.text = "Finished"
                updateRemainingTime()
                handler.postDelayed({
                    timerDisplay.text = "0.0"
                }, 2000)
            },
            stateCallback = { state ->
                updateTimerColor(state)
            }
        )
    }


    private fun updateRemainingTime() {
        val remaining = String.format("%.1f", timerManager.getRemainingTime())
        findViewById<TextView>(R.id.remainingTimeDisplay).text = "Verbleibend: $remaining"
    }

    private fun updateTimerColor(state: TimerState) {
        val color = when (state) {
            TimerState.RUNNING -> getColor(R.color.timer_running)
            TimerState.STOPPED -> getColor(R.color.timer_stopped)
            TimerState.FINISHED -> getColor(R.color.timer_finished)
        }
        timerDisplay.setTextColor(color)
    }

    private fun createPulseAnimation(): ObjectAnimator {
        return ObjectAnimator.ofPropertyValuesHolder(
            timerDisplay,
            PropertyValuesHolder.ofFloat("scaleX", 1f, 1.2f, 1f),
            PropertyValuesHolder.ofFloat("scaleY", 1f, 1.2f, 1f)
        ).apply {
            duration = 500
            repeatCount = ObjectAnimator.INFINITE
        }
    }

    private fun applySavedTheme() {
        val currentCustomTheme = settingsManager.getCurrentSelectedCustomTheme()
        if (currentCustomTheme != null) {
            themeManager.applyTheme(currentCustomTheme)
        } else {
            themeManager.applyTheme(settingsManager.selectedTheme)
        }
    }

    private fun setupThemeCallback() {
        themeManager.setThemeUpdateCallback { primary, secondary, background, textOnPrimary, textOnSecondary, textOnBackground ->
            // Main Timer Buttons
            findViewById<Button>(R.id.button24).apply {
                backgroundTintList = ColorStateList.valueOf(primary)
                setTextColor(textOnPrimary)
            }
            findViewById<Button>(R.id.button14).apply {
                backgroundTintList = ColorStateList.valueOf(primary)
                setTextColor(textOnPrimary)
            }

            // Control Buttons
            findViewById<Button>(R.id.buttonClear).apply {
                backgroundTintList = ColorStateList.valueOf(secondary)
                setTextColor(textOnSecondary)
            }
            findViewById<Button>(R.id.buttonPlus).apply {
                backgroundTintList = ColorStateList.valueOf(secondary)
                setTextColor(textOnSecondary)
            }
            findViewById<Button>(R.id.buttonMinus).apply {
                backgroundTintList = ColorStateList.valueOf(secondary)
                setTextColor(textOnSecondary)
            }

            // Text elements
            findViewById<TextView>(R.id.headerText).setTextColor(textOnBackground)
            findViewById<TextView>(R.id.footerText).setTextColor(textOnBackground)
            findViewById<TextView>(R.id.timerDisplay).setTextColor(textOnBackground)
            findViewById<TextView>(R.id.remainingTimeDisplay).setTextColor(textOnBackground)

            // Background
            window.decorView.setBackgroundColor(background)
        }
    }


    private fun setupClickListeners() {
        timerDisplay.apply {
            setOnClickListener {
                if (settingsManager.isVibrationEnabled) {
                    vibrationManager.vibrateTimerAction()
                }
                if (timerManager.isRunning()) {
                    timerManager.stopTimer()
                    updateRemainingTime()
                } else {
                    timerManager.resume()
                }
            }
        }

        findViewById<Button>(R.id.button24).setOnClickListener {
            if (settingsManager.isVibrationEnabled) {
                vibrationManager.vibrateButton()
            }
            timerManager.handleTimerButton(TimerMode.TIMER_24)
            updateRemainingTime()
        }

        findViewById<Button>(R.id.button14).setOnClickListener {
            if (settingsManager.isVibrationEnabled) {
                vibrationManager.vibrateButton()
            }
            timerManager.handleTimerButton(TimerMode.TIMER_14)
            updateRemainingTime()
        }

        findViewById<Button>(R.id.buttonClear).setOnClickListener {
            if (settingsManager.isVibrationEnabled) {
                vibrationManager.vibrateButton()
            }
            timerManager.clear()
            updateRemainingTime()
        }

        findViewById<Button>(R.id.buttonPlus).setOnClickListener {
            if (settingsManager.isVibrationEnabled) {
                vibrationManager.vibrateButton()
            }
            timerManager.adjustTime(1.0)
            updateRemainingTime()
        }

        findViewById<Button>(R.id.buttonMinus).setOnClickListener {
            if (settingsManager.isVibrationEnabled) {
                vibrationManager.vibrateButton()
            }
            timerManager.adjustTime(-1.0)
            updateRemainingTime()
        }
    }
}