package com.example.bbbt

import android.os.Handler
import android.os.Looper

enum class TimerState {
    RUNNING,
    STOPPED,
    FINISHED
}

enum class TimerMode {
    TIMER_24,
    TIMER_14
}


class TimerManager {
    private var startTime = 0L
    private var currentTime = 0.0
    private var isRunning = false
    private var lastStoppedTime = 0.0
    private var maxTime = 24.0
    private var currentState = TimerState.STOPPED
    private var currentMode: TimerMode = TimerMode.TIMER_24
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

    fun handleTimerButton(mode: TimerMode) {
        startTimer(mode)
    }

    fun startTimer(mode: TimerMode) {
        currentMode = mode
        maxTime = 24.0
        currentTime = if (mode == TimerMode.TIMER_14) 10.0 else 0.0
        startTime = System.currentTimeMillis() - (currentTime * 1000).toLong()
        isRunning = true
        currentState = TimerState.RUNNING
        stateCallback(currentState)
        timerCallback(currentTime)
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

    fun adjustTime(seconds: Double) {
        if (isRunning) {
            currentTime = (currentTime + seconds).coerceIn(0.0, maxTime)
            startTime = System.currentTimeMillis() - (currentTime * 1000).toLong()
        } else {
            currentTime = (currentTime + seconds).coerceIn(0.0, maxTime)
            lastStoppedTime = currentTime
        }
        timerCallback(currentTime)
    }

    fun getRemainingTime(): Double = maxTime - if (isRunning) currentTime else lastStoppedTime
    fun isRunning(): Boolean = isRunning
}