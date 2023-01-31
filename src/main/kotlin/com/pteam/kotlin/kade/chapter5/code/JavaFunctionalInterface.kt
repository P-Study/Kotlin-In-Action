package com.pteam.kotlin.kade.chapter5.code

class JavaFunctionalInterface {

    val runnable = Runnable { println(42) }
    fun postponeComputation(delay: Int, computation: Runnable) {

    }

    fun handleComputation() {
        postponeComputation(1000, runnable)
        postponeComputation(1000, object : Runnable {
            override fun run() {
                println(42)
            }
        })
        postponeComputation(1000) { println(42) }
    }

    fun secondHandleComputation(id: String) {
        postponeComputation(1000) { println(id) }
    }

    fun createAllDoneRunnable(): Runnable {
        return Runnable { println("All done! 헀으면 좋겠다") }
    }
}