package com.pteam.kotlin.hyuk.chapter05.code

object UsingJavaFunctionalInterface {
    fun ver1() {
        JavaFunction.postponeComputation(3) { println("Hi!") }
    }

    fun ver2(msg: String) {
        JavaFunction.postponeComputation(3) { println("${msg}") }
    }
}