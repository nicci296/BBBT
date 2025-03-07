package com.example.bbbt

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.os.Handler
import android.os.Looper
import android.view.GestureDetector
import android.view.MotionEvent

class MainActivity : AppCompatActivity() {
    private lateinit var gestureDetector: GestureDetector
    private lateinit var timerManager: TimerManager
    private lateinit var timerDisplay: TextView
    private lateinit var stoppedTimeDisplay: TextView
    private lateinit var vibrationManager: VibrationManager
    private lateinit var pulseAnimation: ObjectAnimator
    private val handler = Handler(Looper.getMainLooper())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupGestureDetector()
        vibrationManager = VibrationManager(this)
        setupViews()
        pulseAnimation = createPulseAnimation()
        setupTimer()
        setupTips()
        setupClickListeners()
    }

    private fun setupGestureDetector() {
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                vibrationManager.vibrateTimerAction()
                timerManager.clear()
                updateStoppedTimeDisplay()
                return true
            }
        })
    }

    private fun setupViews() {
        timerDisplay = findViewById(R.id.timerDisplay)
        stoppedTimeDisplay = findViewById(R.id.stoppedTimeDisplay)
    }


    private fun setupTimer() {
        timerManager = TimerManager()
        timerManager.setCallbacks(
            timeCallback = { time ->
                timerDisplay.text = String.format("%.1f", time)
                if (time >= 19.0) {  // Start pulsing at last 5 seconds
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

    private fun updateStoppedTimeDisplay() {
        val stopped = String.format("%.1f", timerManager.getStoppedTime())
        val remaining = String.format("%.1f", timerManager.getRemainingTime())
        stoppedTimeDisplay.text = "Gestoppt: $stopped | Verbleibend: $remaining"
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
                vibrationManager.vibrateTimerAction()
                if (timerManager.isRunning()) {
                    timerManager.stopTimer()
                    updateStoppedTimeDisplay()
                } else {
                    timerManager.resume()
                }
            }

            setOnTouchListener { v, event ->
                if (gestureDetector.onTouchEvent(event)) {
                    true
                } else {
                    performClick()
                    false
                }
            }
        }

        findViewById<Button>(R.id.button24).setOnClickListener {
            vibrationManager.vibrateButton()
            timerManager.startTimer(24)
        }
        findViewById<Button>(R.id.button14).setOnClickListener {
            vibrationManager.vibrateButton()
            timerManager.startTimer(14)
        }
        findViewById<Button>(R.id.buttonClear).setOnClickListener {
            vibrationManager.vibrateButton()
            timerManager.clear()
            updateStoppedTimeDisplay()
        }
        findViewById<Button>(R.id.buttonPlus).setOnClickListener {
            vibrationManager.vibrateButton()
            timerManager.adjustStoppedTime(1.0)
            updateStoppedTimeDisplay()
        }
        findViewById<Button>(R.id.buttonMinus).setOnClickListener {
            vibrationManager.vibrateButton()
            timerManager.adjustStoppedTime(-1.0)
            updateStoppedTimeDisplay()
        }
    }

}