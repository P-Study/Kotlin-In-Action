package com.pteam.kotlin.hyuk.chapter11.code

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

internal class ExampleKtTest : DescribeSpec() {
    init {
        describe("수신 객체 지정 람다와 확장 함수 타입") {
            it("일반 함수 타입") {
                val s = buildStringV1 {
                    it.append("Hello, ")
                    it.append("World!")
                }

                s shouldBe "Hello, World!"
            }

            it("확장 함수 타입") {
                val s = buildStringV2 {
                    append("Hello, ")
                    append("World!")
                }

                s shouldBe "Hello, World!"
            }

            it("확장 함수 타입 개선") {
                val s = buildStringV3 {
                    append("Hello, ")
                    append("World!")
                }

                s shouldBe "Hello, World!"
            }
        }

        describe("HTML internal DSL") {
            it("ver1") {
                val component = buildDropdownV1()

                println(component)
                component shouldNotBe null
            }


            it("ver2") {
                val component = buildDropDownV2()

                println(component)
//                component shouldBe buildDropdownV1()
            }
        }
    }
}