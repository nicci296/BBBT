package com.example.bbbt

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
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
    private lateinit var pulseAnimation: ObjectAnimator
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vibrationManager = VibrationManager(this)
        settingsManager = SettingsManager(this)
        setupViews()
        pulseAnimation = createPulseAnimation()
        setupTimer()
        setupTips()
        setupClickListeners()

        findViewById<ImageButton>(R.id.settingsButton).setOnClickListener {
            SettingsDialog(this, settingsManager).show()
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

    private fun setupTips() {
        val tips = resources.getStringArray(R.array.tips)
        val tipsText = StringBuilder("Tips:\n")
        tips.forEach { tip ->
            tipsText.append("• $tip\n")
        }
        findViewById<TextView>(R.id.tipsText).text = tipsText.toString().trimEnd()
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