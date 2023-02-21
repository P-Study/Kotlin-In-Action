package com.pteam.kotlin.minzzang.chapter7

import com.pteam.kotlin.minzznag.chapter7.JasT
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

internal class OperatorTest : StringSpec() {
    init {
        "operator 연산자를 이용해 +(plus) 연산자를 사용할 수 있다" {
            val p1 = Point(10, 20)
            val p2 = Point(100, 200)

            p1 + p2 shouldBe Point(110, 220)
        }

        "두 피연산자의 타입이 다른 연산자를 정의할 수 있다" {
            operator fun Point.times(scale: Double): Point {
                return Point((x * scale).toInt(), (y * scale).toInt())
            }
            var p = Point(10, 20)

            p * 10.5 shouldBe Point(105, 210)
        }

        "내부 상태를 변경하고 싶을 때는 plusAssign을 사용해라" {
            val number = Number(10)
            val tmp = number

            number += Number(20)
            val tmp2 = number

            tmp shouldBe tmp2
        }

        "순서 연산자 compareTo에 비교 연산자를 사용할 수 있다" {
            val number = Number(100)
            val number2 = Number(200)

            (number < number2) shouldBe true
        }

        "in 관례를 구현해 객체가 컬렉션에 들어 있는지 검사할 수 있다" {
            class Numbers(val elements: List<Int>) {
                operator fun contains(element: Int) = elements.contains(element)
            }

            val numbers = Numbers(listOf(1, 2, 3, 5))

            (4 in numbers) shouldBe false
        }

        "구조 분해 선언을 사용하면 맵 이터레이션을 쉽게할 수 있다" {
            fun printEntries(map: Map<String, String>) {
                for ((key, value) in map) {
                    println("${key} -> ${value}")
                }
            }
        }
    }
}

data class Point(var x: Int, var y: Int) {
    operator fun plus(other: Point) = Point(x + other.x, y + other.y)
}

class Number(var n: Int) : Comparable<Number> {
    operator fun plusAssign(other: Number) {
        this.n += other.n
    }

    override fun compareTo(other: Number): Int {
        if (this.n > other.n) return 1
        if (this.n == other.n) return 0
        return -1
    }
}
