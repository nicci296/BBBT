package com.example.bbbt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.os.Handler
import android.os.Looper

class MainActivity : AppCompatActivity() {
    private lateinit var timerManager: TimerManager
    private lateinit var timerDisplay: TextView
    private lateinit var stoppedTimeDisplay: TextView
    private lateinit var vibrationManager: VibrationManager
    private val handler = Handler(Looper.getMainLooper())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vibrationManager = VibrationManager(this)
        setupViews()
        setupTimer()
        setupClickListeners()
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
            },
            finishedCallback = {
                timerDisplay.text = "Finished"
                handler.postDelayed({
                    timerDisplay.text = "0.00"
                }, 2000)
            }
        )
    }

    private fun updateStoppedTimeDisplay() {
        val stopped = String.format("%.1f", timerManager.getStoppedTime())
        val remaining = String.format("%.1f", timerManager.getRemainingTime())
        stoppedTimeDisplay.text = "Stopped: $stopped | Remaining: $remaining"
    }

    private fun setupClickListeners() {
        timerDisplay.setOnClickListener {
            vibrationManager.vibrateTimerAction()
            if (timerManager.isRunning()) {
                timerManager.stopTimer()
                updateStoppedTimeDisplay()
            } else {
                timerManager.resume()
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