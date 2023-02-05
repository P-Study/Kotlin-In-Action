package com.pteam.kotlin.hyuk.chapter05.code

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

internal class MyLambdaFunctionTest() : StringSpec() {
    init {
        "람다 호출 ver1" {
            val result = MyLambdaFunction.myFunction("hello lambda", { msg: String -> msg })
            result shouldBe "hello lambda"
        }

        "람다 호출 ver2" {
            val result = MyLambdaFunction.myFunction("hello lambda") { msg: String -> msg }
            result shouldBe "hello lambda"
        }

        "람다 호출 ver3" {
            val result = MyLambdaFunction.myFunction("hello lambda") { msg -> msg }
            result shouldBe "hello lambda"
        }

        "람다 호출 ver4" {
            val result = MyLambdaFunction.myFunction("hello lambda") { it }
            result shouldBe "hello lambda"
        }
    }
}
