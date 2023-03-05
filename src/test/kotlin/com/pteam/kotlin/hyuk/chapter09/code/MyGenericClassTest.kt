package com.pteam.kotlin.hyuk.chapter09.code

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

internal class MyGenericClassTest : DescribeSpec({
    describe("generic class") {
        it("make my generic class instance") {
            val myGenericClass = MyGenericClass<String>("hello")

            myGenericClass.value shouldBe "hello"
        }
        it("make my generic class instance - 2") {
            val myGenericClass = MyGenericClass<Int>(100)

            myGenericClass.value shouldBe 100
        }
    }
})