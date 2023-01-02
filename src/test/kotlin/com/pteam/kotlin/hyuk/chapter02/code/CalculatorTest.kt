package com.pteam.kotlin.hyuk.chapter02.code

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.comparables.shouldBeEqualComparingTo

internal class CalculatorTest : FreeSpec() {
    init {
        "계산기 테스트" - {
            "곲하기_성공" {
                val eval = Calculator.eval(Multiply(Multiply(Num(1), Num(2)), Num(3)))
                eval shouldBeEqualComparingTo 6
            }
            "잘못된 식" {
                shouldThrowExactly<IllegalArgumentException> { Calculator.eval(object : Expr {}) }
            }
        }
    }
}