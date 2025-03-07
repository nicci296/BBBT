package com.example.bbbt

import android.os.Handler
import android.os.Looper

class TimerManager {
    private var startTime = 0L
    private var currentTime = 0.0
    private var isRunning = false
    private var lastStoppedTime = 0.0
    private var maxTime = 24.0
    private lateinit var timerCallback: (Double) -> Unit
    private lateinit var timerFinishedCallback: () -> Unit
    private var handler = Handler(Looper.getMainLooper())

    private val timerRunnable = object : Runnable {
        override fun run() {
            if (isRunning) {
                val elapsedTime = (System.currentTimeMillis() - startTime) / 1000.0
                currentTime = elapsedTime
                if (currentTime >= maxTime) {
                    stopTimer()
                    timerFinishedCallback()
                } else {
                    timerCallback(currentTime)
                    handler.postDelayed(this, 10) // Update every 10ms
                }
            }
        }
    }

    fun setCallbacks(timeCallback: (Double) -> Unit, finishedCallback: () -> Unit) {
        timerCallback = timeCallback
        timerFinishedCallback = finishedCallback
    }

    fun startTimer(seconds: Int = 24) {
        maxTime = 24.0  // Always count to 24
        currentTime = if (seconds == 14) 10.0 else 0.0  // Start at 10 for 14s mode
        startTime = System.currentTimeMillis() - (currentTime * 1000).toLong()
        isRunning = true
        handler.post(timerRunnable)
    }

    fun stopTimer() {
        isRunning = false
        lastStoppedTime = currentTime
        handler.removeCallbacks(timerRunnable)
    }

    fun adjustStoppedTime(seconds: Double) {
        lastStoppedTime = (lastStoppedTime + seconds).coerceIn(0.0, maxTime)
    }

    fun getStoppedTime(): Double = lastStoppedTime

    fun clear() {
        isRunning = false
        currentTime = 0.0
        lastStoppedTime = 0.0
        handler.removeCallbacks(timerRunnable)
        timerCallback(currentTime)
    }

    fun resume() {
        if (!isRunning && lastStoppedTime < maxTime) {
            startTime = System.currentTimeMillis() - (lastStoppedTime * 1000).toLong()
            isRunning = true
            handler.post(timerRunnable)
        }
    }

    fun getRemainingTime(): Double = maxTime - lastStoppedTime

    fun isRunning(): Boolean = isRunning
}