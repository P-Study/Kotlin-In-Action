package com.pteam.kotlin.minzzang.chapter2.learn

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import java.io.BufferedReader
import java.io.StringReader

internal class KotlinBasicLearningTest : BehaviorSpec({

    given("함수가") {
        `when`("식이 본문이면") {
            then("리턴이 없다") {
                fun max(a: Int, b: Int): Int = if (a > b) a else b

                max(3, 5) shouldBe 5
            }
            then("타입 추론을 해서 반환값을 생략할 수 있다") {
                fun max(a: Int, b: Int) = if (a > b) a else b

                max(3, 5) shouldBe 5
            }
        }
        When("블록이 본문이면") {
            then("리턴이 있다") {
                fun max(a: Int, b: Int): Int {
                    return if (a > b) a else b
                }

                max(3, 5) shouldBe 5
            }
        }
    }

    given("변수는") {
        xwhen("웬만하면 val로 만들면") {
            then("좋다") {
                val first = 3

                first shouldBe 3
            }
        }
        `when`("변경이 필요할 때만") {
            then("var로 만들자") {
                var first = 3
                first = 5

                first shouldBe 5
            }
        }
    }

    given("문자열 템플릿을") {
        `when`("사용할 땐") {
            then("꼭 중괄호를 사용하자") {
                val samsamsamsun = "zza zang"

                println("먹고싶다, ${samsamsamsun}")
            }
        }
    }

    given("프로퍼티에서") {
        `when`("val은") {
            class Property(
                val value: Int
            )

            then("읽기 전용이고 비공개 필드와 게터를 만든다") {
                val property = Property(10)

                property.value shouldBe 10
            }
        }
        `when`("var은") {
            class Property(
                var value: Int
            )

            then("쓰기 가능이고 비공개 필드, 게터, 세터를 만든다") {
                val property = Property(10)
                property.value = 5

                property.value shouldBe 5
            }
        }
    }

    given("커스텀 접근자는") {
        `when`("왜 쓰는지") {
            then("잘 모르겠다....") {
            }
        }
    }

    given("클래스 없이") {
        `when`("함수 선언을") {
            then("할 수 있다") {
                이게_되넹() shouldBe "good"
            }
        }
    }
})

internal class LoopLearningTest : StringSpec({

    "when을 이용해 올바른 내가 좋아하는 색을 찾을 수 있다" {
        fun 내가_제일_좋아하는_색은(color: Color) =
            when (color) {
                Color.RED -> "아냐"
                Color.YELLOW -> "아냐"
                Color.BLUE -> "맞앙"
            }

        내가_제일_좋아하는_색은(Color.BLUE) shouldBe  "맞앙"
    }

    "when을 사용하면 분기 안에 여러 값도 사용할 수 있다" {
        fun 내가_제일_좋아하는_색은(color: Color) =
            when (color) {
                Color.RED, Color.YELLOW -> "아냐"
                Color.BLUE -> "맞앙"
            }

        내가_제일_좋아하는_색은(Color.BLUE) shouldBe  "맞앙"

    }

    "인자 없이 when을 사용할 수 있다" {
        val number = 10
        val flag = true

        val answer = when {
            number == 5 -> "오답"
            number == 10 && !flag -> "노답"
            number == 10 && flag -> "정답"
            else -> "?"
        }

        answer shouldBe "정답"
    }

    "is를 사용하면 스마트 캐스트를 할 수 있다" {
        fun cast(number: Number): Int {
            if (number is Number) {
                return number.toInt()
            }
            if (number is Int) {
                return number.compareTo(3)
            }
            return 0
        }
    }

    "..을 사용하면 범위를 반복 범위를 만들 수 있다" {
        var sum = 0
        for (i in 1..10) {
            sum += i
        }

        sum shouldBe 55
    }

    "downTo는 역방향 수열을 만든다" {
        var lastNumber = 0
        for (i in 10 downTo  5) {
            lastNumber = i
        }

        lastNumber shouldBe 5
    }

    "in을 사용하면 컬렉션이나 범위에 값이 포함되는지 확인할 수 있다" {
        val contains = "d" in listOf("a", "b", "c")

        contains shouldBe false
    }

    "kotlin에서 throw는 식이다" {
        val sick = IllegalArgumentException("식이 식이")

        sick shouldHaveMessage "식이 식이"
    }

    "kotlin은 checked 예외를 구분하지 않는다" {
        fun readNumber(reader: BufferedReader) = try {
            Integer.parseInt(reader.readLine())
        } catch (e: NumberFormatException) {
            null
        }

        val reader = BufferedReader(StringReader("숫자가 아니지롱"))
        readNumber(reader) shouldBe null
    }
})

enum class Color {
    RED, YELLOW, BLUE
}

fun 이게_되넹(): String {
    return "good"
}
