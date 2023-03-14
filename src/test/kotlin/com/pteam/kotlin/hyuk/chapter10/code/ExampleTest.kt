package com.pteam.kotlin.hyuk.chapter10.code

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlin.reflect.KFunction
import kotlin.reflect.full.memberProperties

internal class ExampleTest : DescribeSpec() {
    init {
        describe("annotation use case") {
            it("using deprecated method") {
                MyDeprecatedClass.deprecatedMethod()
            }
        }
        describe("reflection use case") {
            it("KClass") {
                val person = Person("hello", 30)
                val kClass = person.javaClass.kotlin

                kClass.simpleName shouldBe "Person"
                kClass.memberProperties shouldHaveSize 2
            }

            it("kFunction type unsafe") {
                val kFunction = ::fooFunction
                kFunction.call()
            }
        }
    }
}