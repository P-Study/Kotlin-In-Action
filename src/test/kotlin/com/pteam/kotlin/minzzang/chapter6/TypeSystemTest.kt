package com.pteam.kotlin.minzzang.chapter6

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe

internal class TypeSystemTest : StringSpec() {
    init {
        "null이 될 수 있는 값은 if 검사를 하면 안전하게 사용할 수 있다" {
            fun safe(s: String?): String {
                if (s != null) return s
                return ""
            }
        }

        "안전한 호출 연산자를 사용하면 null 검사와 메소드 호출을 한번에 할 수 있다" {
            fun length(s: String?): Int? {
                return s?.length
            }

            length(null) shouldBe null
        }

        "엘비스 연산자를 사용해 디폴트 값을 지정할 수 있다." {
            fun length(s: String?): Int {
                return s?.length ?: 0
            }

            length(null) shouldBe 0
        }

        "!!은 한 줄에 하나씩만 사용하자" {
            val person = Person(null)

            person.company!!
                .team!!
                .name
        }

        "널이 될 수 있는 값을 널 아닌 값만 인자로 받는 함수에 넘길 때는 let을 사용한다" {
            fun send(email: String) = email

            val email: String? = "good email"
            email.isNullOrBlank()
            email?.let { send(it) }
        }

        "확장 함수는 널이 될 수 없는 타입으로 정의하고 필요하면 널이 될 수 있는 타입으로 변경해라" {
            fun String?.lengthOrNull(): Int {
                return this?.length ?: 0
            }

            val str = null
            str.lengthOrNull() shouldBe 0
        }

        "Any는 자바의 Object를 대신하지만, 자바와 달리 Int 등의 원시 타입을 포함한 모든 타입의 조상 타입이다" {
            val answer: Any = 42
            print(answer.javaClass)
        }

        "Unit은 자바의 void와 같지만 void와 달리 타입 인자로 쓸 수 있다." {
            class NoResultProcessor : Processor<Unit> {
                override fun process() {
                    println("good~~")
                }
            }
        }

        "널이 될 수 있는 값으로 이뤄진 컬렉션은 fillterNotNull을 사용하여 분리할 수 있다" {
            val numbers: List<Int?> = listOf(1, 2, null)
            numbers.filterNotNull()

            numbers shouldContainExactly listOf(1, 2)
        }

        "가능하면 항상 읽기 전용 인터페이스를 사용해라" {
            fun getList(): List<Int> {
                val list = mutableListOf(1, 2, 3)
                list.add(4)
                list.add(5)

                return list.toList()
            }

            getList()
        }

        "자바에서 가져온 컬렉션을 코틀린에서 사용할 땐 구현하는 작업에 따라 컬렉션 타입이 달라질 수 있다"

        "박싱된 타입의 배열은 arrayOf를 원시 타입의 배열은 typeArrayOf로 생성한다" {
            val box = arrayOf(1, 2, 3)
            val primitive = intArrayOf(1, 2, 3)
        }
    }
}

class Person(val company: Company?)
class Company(val team: Team?)
class Team(val name: String)

interface Processor<T> {
    fun process(): T
}
