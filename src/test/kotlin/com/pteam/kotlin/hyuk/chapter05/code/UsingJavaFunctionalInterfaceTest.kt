package com.pteam.kotlin.hyuk.chapter05.code

import io.kotest.core.spec.style.StringSpec

internal class UsingJavaFunctionalInterfaceTest() : StringSpec() {
    init {
        "ver1" {
            UsingJavaFunctionalInterface.ver1()
        }

        "ver2" {
            UsingJavaFunctionalInterface.ver2("Hello Kotlin!")
        }
    }
}
