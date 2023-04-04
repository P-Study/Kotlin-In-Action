package com.pteam.kotlin.juyoung.appendix.e.code

import kotlinx.coroutines.*
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

fun now() = ZonedDateTime.now().toLocalTime().truncatedTo(ChronoUnit.MILLIS)

fun log(msg: String) = println("${now()}:${Thread.currentThread()}:${msg}")

fun launchInGlobalScope() {
    // GlobalScope: 전체 어플리케이션의 라이프타임
    // launch: 해당 코드 블럭에서 현재 스레드를 블로킹하지 않고, 코루틴으로 실행
    GlobalScope.launch {
        log("coroutine started.")
    }
}

fun runBlockingExample() {
    runBlocking{
        launch {
            log("GlobalScope.launch started")
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
            yield()
        }
        log("after first launch")
        launch {
            log("2")
            delay(1000L)    // delay: 스레드를 블로킹할 수 없는 suspending 함수
            log("4")
            delay(1000L)
            log("6")
            delay(1000L)
        }
        log("after second launch")
    }
}

fun sumAll() {
    runBlocking {
        val d1 = async { delay(1000L); 1 }
        log("after async(d1)")
        val d2 = async { delay(1000L); 2 }
        log("after async(d2)")
        val d3 = async { delay(1000L); 3 }
        log("after async(d3)")

        log("1+2+3 = ${d1.await() + d2.await() + d3.await()}")
        log("after await all & add")
    }
}

fun coroutineContext() {
    runBlocking {
        launch {
            println("main runBlocking: I'm working in thread ${Thread.currentThread().name}")
        }

        launch(Dispatchers.Unconfined) {
            println("Default: I'm working in thread ${Thread.currentThread().name}")
        }

        launch(newSingleThreadContext("MyOwnThread")) {
            println("newSingleThreadContext: I'm working in thread ${Thread.currentThread().name}")
        }
    }
}

suspend fun yieldThreeTimes() {
    log("1")
    delay(1000L)
    yield()
    log("2")
    delay(1000L)
    yield()
    log("3")
    delay(1000L)
    yield()
    log("4")
}

fun suspendExample() {
    GlobalScope.launch { yieldThreeTimes() }
}

fun main() {
    log("main() started.")

//    runBlockingExample()
//    log("runBlockingExample() executed")

//    launchInGlobalScope()
//    log("launchInGlobalScope() executed")
//    Thread.sleep(5000L)

//    yieldExample()
//    log("yieldExample() executed")

//    sumAll()
//    log("sumAll() executed")

//    coroutineContext()
//    log("coroutineContext() executed")

    suspendExample()
    log("suspendExample() executed")

    log("main() terminated")
}