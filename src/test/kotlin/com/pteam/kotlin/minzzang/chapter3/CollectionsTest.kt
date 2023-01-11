package com.pteam.kotlin.minzzang.chapter3

import com.pteam.kotlin.minzznag.chapter3.*
import io.kotest.core.spec.style.StringSpec
import io.kotest.data.blocking.forAll
import io.kotest.data.row
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

internal class CollectionsTest : StringSpec({

    "List에 다양한 toString을 적용할 수 있다" {
        val list = listOf(1, 2, 3)

        forAll(
            row(list, "; ", "(", ")", "(1; 2; 3)"),
            row(list, ": ", "[", "]", "[1: 2: 3]"),
            row(list, ", ", "{", "}", "{1, 2, 3}")
        ) { collection, separator, prefix, postfix, result ->
            joinToString(collection, separator, prefix, postfix) shouldBe result
        }
    }

    "함수를 호출 시 인자 중 하나라도 이름을 명시하면 그 뒤에 인자는 이름을 꼭 명시한다" {
        val list = listOf(1, 2, 3)
        joinToString(list, separator = ",", prefix = "", postfix = ",") shouldBe "1,2,3,"
    }

    "디폴트 파라미터 값을 사용하면 불 필요한 오버로딩을 줄일 수 있다" {
        val list = listOf(1, 2, 3)

        joinToString(list) shouldBe "1, 2, 3"
        joinToString(list, "; ") shouldBe "1; 2; 3"
        joinToString(list, postfix = ")", prefix = "(") shouldBe "(1, 2, 3)"
    }

    "확장 함수를 사용하면 collection에 메서드처럼 사용할 수 있다" {
        val list = listOf(1, 2, 3)

        list.join() shouldBe "1, 2, 3"
    }

    "확장 함수는 컴파일 타임에 결정되기 때문에 오버라이드 할 수 없다" {
        val view: View = Button()

        view.show() shouldNotBe "Im a button"
    }

    "배열에 있는 원소를 가변 인자로 넘기려면 *를 사용한다" {
        val collections = arrayOf("list", "set", "map")
        val list = listOf("vector", *collections)

        list.join() shouldBe "vector, list, set, map"
    }

    "인자가 하나 뿐인 메소드, 확장 함수는 중위 호출을 사용할 수 있다" {
        val (number, string) = 10 to "ten"

        number shouldNotBe null
        string shouldNotBe null
    }

    "여러 구분 문자열을 지정해 split 할 수 있다" {
        val split: List<String> = "12.34,56;78/910".split(";", "/",  ",", ".")

        split shouldContainExactly listOf("12", "34", "56", "78", "910")
    }

    "3중 따옴 문자열에서는 이스케이프 할 필요 없다" {
        val regex = """(.+)/(.+)\.(.+)""".toRegex()

        // illegal escape
        // val regex2 = "(.+)/(.+)\.(.+)".toRegex()
    }

    "3중 따옴 문자열로 여러 줄 문자열을 쉽게 표현할 수 있다" {
        val string = """
아
    이  
        스
            크
                림               
        """

        println(string)
    }

    "로컬 함수는 한 단계만 중첩하자" {
        fun `먹고 싶은 음식을 쭉 정리한다`(): String {
            infix fun String.먹는다(누가먹누: String) = 누가먹누.plus(this).plus("먹는다")

            val 치킨 = "곱창을" 먹는다 "내가"
            val 햄버거 = "햄버거를" 먹는다 "tf찬이..."

            return """
                  ${치킨}
                  ${햄버거}
            """.trimIndent()
        }

        `먹고 싶은 음식을 쭉 정리한다`() shouldBe
                """
                    내가곱창을먹는다
                    tf찬이...햄버거를먹는다
                """.trimIndent()
    }
})
