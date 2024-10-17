package com.example.notifications

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

object Counter {

    private var counterValue: Int = 0
    private var isRunning: Boolean = true


    fun start() = flow {
        isRunning = true

        while (isRunning && counterValue <= 1) {
            emit(counterValue)
            delay(1000)
            counterValue++

        }
    }


    fun stop() {
        isRunning = false
        counterValue = 0
    }

}