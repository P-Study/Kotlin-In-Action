package com.pteam.kotlin.hyuk.appendix.code

import kotlinx.coroutines.*
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

fun now() = ZonedDateTime.now().toLocalDateTime().truncatedTo(ChronoUnit.MILLIS)

fun log(msg: String) = println("${now()}:${Thread.currentThread()}:${msg}")

fun launchGlobalScope() {
    GlobalScope.launch { log("coroutine started.") }
}

fun runBlockingExample() {
    runBlocking {
        launch {
            log("GlobalScope.launch started.")
        }
    }
}

fun yieldExample() {
    runBlocking {
        launch {
            log("1")
            yield()
            log("3")
            yield()
            log("5")
        }
        log("after first launch")
        launch {
            log("2")
            delay(1000L)
            log("4")
            delay(1000L)
            log("6")
        }
        log("after second launch")
    }
}

fun sumAll() {
    runBlocking {
        val d1 = async {
            delay(1000L)
            1
        }
        log("after async(d1)")

        val d2 = async {
            delay(2000L)
            2
        }
        log("after async(d2)")

        val d3 = async {
            delay(3000L)
            3
        }
        log("after async(d3)")

        log("1+2+3 = ${d1.await() + d2.await() + d3.await()}")
        log("after await all & add")
    }
}