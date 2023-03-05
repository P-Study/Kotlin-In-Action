package com.pteam.kotlin.hyuk.chapter09.code

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

internal class MyTypeParameterConstraintKtTest : DescribeSpec({
    describe("type parameter constraint") {
        it("mySum called success") {
            val numbers = (1..10).toList()

            numbers.mySum() shouldBe 55
        }
        it("mySum called fail") {
            val letter = ('a'..'z').toList()

//            letter.mySum()
        }
        it("type parameter constraints") {
            val seq = StringBuilder("Hi Kotlin")

            ensureTrailingPeriod(seq)

            seq.toString() shouldBe "Hi Kotlin."
        }
        it("not null type parameter") {
            val myProcessor = MyProcessor<String>()

            myProcessor.process("Hello") shouldNotBe null
        }
    }
})