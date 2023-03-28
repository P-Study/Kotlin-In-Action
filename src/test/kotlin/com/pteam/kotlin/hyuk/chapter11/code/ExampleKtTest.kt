package com.pteam.kotlin.hyuk.chapter11.code

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldHaveSize
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

        describe("invoke 관례") {
            it("기본 사용법") {
                val bavarianGreeter = Greeter("Servus")

                bavarianGreeter("Dmitry") shouldBe "Servus, Dmitry"
            }

            it("로직이 복잡한 람다를 invoke 메소드를 활용해 리펙토링") {
                val i1 = Issue("IDEA-154446", "IDEA", "Bug", "Major", "Save settings failed")
                val i2 = Issue("KT-12183", "Kotlin", "Feature", "Normal", "Intention: ...")

                val predicate = ImportantIssuePredicate("IDEA")
                val result = listOf(i1, i2).filter(predicate)

                result shouldHaveSize 1
                result[0] shouldBe i1
            }

            it("gradle 에서 invoke 관례 활용") {
                val dependencies = DependencyHandler()
                dependencies.compile("org.jetbrains.kotlin:kotlin-stdlib:1.0.0")

                dependencies {
                    compile("org.jetbrains.kotlin:kotlin-stdlib:1.0.0")
                    compile("org.jetbrains.kotlin:kotlin-reflect:1.0.0")
                }
            }
        }

        describe("DSL 만들기") {
            it("kotest DSL 만들기 ver1") {
                "kotlin" myShould MyStartWith("kot")
            }

            it("kotest DSL 만들기 ver2") {
                "kotlin" myShould myStart with "kot"
            }

            it("날짜 처리") {
                val ago = 1.days.ago
                print(ago)
            }
        }
    }
}