package com.pteam.kotlin.hyuk.chapter06.code

import io.kotest.core.spec.style.FunSpec

class MyClassesTest : FunSpec({
    context("Kotlin Collection With Java") {
        test("print collection elements") {
            val listOf = listOf(null, "hello", null, "bye")
            PrintProcessor().process(listOf)
        }

        test("doubling collection and print elements") {
            val listOf = mutableListOf("hello", "hi", "bye")
            SizeUpAndPrintProcessor().process(listOf)
        }
    }
})