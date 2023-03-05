package com.pteam.kotlin.hyuk.chapter09.code

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

internal class MyGenericFunctionAndPropertiesKtTest : DescribeSpec({
    describe("generic function") {
        it("extension function") {
            val letters = ('a'..'z').toList()

            val slicedLetters = letters.mySlice(1..2)

            slicedLetters.size shouldBe 2
        }
        it("high order function") {
            val sequence = (1..10).toList()

            val even = sequence.myFilter { it % 2 == 0 }

            even.size shouldBe 5
        }
    }

    describe("generic extend properties") {
        it("properties(Char)") {
            val letter = ('a'..'z').toList()

            letter.penultimate shouldBe 'y'
        }
        it("properties(Int)") {
            val numbers = (1..50).toList()

            numbers.penultimate shouldBe 49
        }
    }
})