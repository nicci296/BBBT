package com.example.bbbt

import android.os.Handler
import android.os.Looper

enum class TimerState {
    RUNNING,
    STOPPED,
    FINISHED
}

class TimerManager {
    private var startTime = 0L
    private var currentTime = 0.0
    private var isRunning = false
    private var lastStoppedTime = 0.0
    private var maxTime = 24.0
    private var currentState = TimerState.STOPPED
    private lateinit var timerCallback: (Double) -> Unit
    private lateinit var timerFinishedCallback: () -> Unit
    private lateinit var stateCallback: (TimerState) -> Unit
    private var handler = Handler(Looper.getMainLooper())

    private val timerRunnable = object : Runnable {
        override fun run() {
            if (isRunning) {
                val elapsedTime = (System.currentTimeMillis() - startTime) / 1000.0
                currentTime = elapsedTime
                if (currentTime >= maxTime) {
                    stopTimer()
                    currentState = TimerState.FINISHED
                    stateCallback(currentState)
                    timerFinishedCallback()
                } else {
                    timerCallback(currentTime)
                    handler.postDelayed(this, 10)
                }
            }
        }
    }

    fun setCallbacks(
        timeCallback: (Double) -> Unit,
        finishedCallback: () -> Unit,
        stateCallback: (TimerState) -> Unit
    ) {
        this.timerCallback = timeCallback
        this.timerFinishedCallback = finishedCallback
        this.stateCallback = stateCallback
    }

    fun startTimer(seconds: Int = 24) {
        maxTime = 24.0
        currentTime = if (seconds == 14) 10.0 else 0.0
        startTime = System.currentTimeMillis() - (currentTime * 1000).toLong()
        isRunning = true
        currentState = TimerState.RUNNING
        stateCallback(currentState)
        handler.post(timerRunnable)
    }

    fun stopTimer() {
        isRunning = false
        lastStoppedTime = currentTime
        handler.removeCallbacks(timerRunnable)
        currentState = TimerState.STOPPED
        stateCallback(currentState)
    }

    fun clear() {
        isRunning = false
        currentTime = 0.0
        lastStoppedTime = 0.0
        handler.removeCallbacks(timerRunnable)
        currentState = TimerState.STOPPED
        stateCallback(currentState)
        timerCallback(currentTime)
    }

    fun resume() {
        if (!isRunning && lastStoppedTime < maxTime) {
            startTime = System.currentTimeMillis() - (lastStoppedTime * 1000).toLong()
            isRunning = true
            currentState = TimerState.RUNNING
            stateCallback(currentState)
            handler.post(timerRunnable)
        }
    }

    fun adjustStoppedTime(seconds: Double) {
        currentTime = (currentTime + seconds).coerceIn(0.0, maxTime)
        lastStoppedTime = currentTime
        timerCallback(currentTime)
    }

    fun getRemainingTime(): Double = maxTime - lastStoppedTime
    fun isRunning(): Boolean = isRunning
}