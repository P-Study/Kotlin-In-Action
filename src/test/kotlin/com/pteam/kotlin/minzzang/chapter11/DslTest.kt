package com.pteam.kotlin.minzzang.chapter11

import io.kotest.core.spec.style.StringSpec
import java.time.LocalDate
import java.time.Period

internal class DslTest : StringSpec() {
    init {
        """
            api가 깔끔하다는 의미
            
            1. 코드를 읽는 독자들이 어떤 일이 벌어질지 명확하게 이해할 수 있어야 한다.
            2. 코드가 간결해야 한다. 불필요한 구문이나 번잡한 준비 코드가 적어야 한다.
            
            깔끔한 api를 작성할 수 있게 돕는 코틀린 기능
            1. 확장 함수
            2. 중위 함수 호출
            3. 람다에서 사용할 수 있는 it
            4. 연산자 오버로딩
            
            그렇다면 dsl이란 무엇일까?
            
            특정 과업 또는 영역에 초점을 맞추고 필요하지 않은 기능을 없앤 특화 언어 (SQL)
            
            범용 프로그래밍 언와 다른점?
            1. 선언적이다. 선언적 언어는 원하는 결과를 기술하기만 하고 세부 실행은 언어를 해석하는 엔진에 맡김.
            
            dsl의 단점
            1. 범용 언오로 만든 애플리케이션과 조합하기가 어렵다. 두 가지를 언어를 알아야 한다. (java + sql)
            
            
        """

        "사용하기 편한 코드를 만들기 위해 수신 객체 지정 람다를 사용해보자" {
            // 일반 람다
            fun buildString(
                builderAction: (StringBuilder) -> Unit
            ): String {
                val sb = StringBuilder()
                builderAction(sb)
                return sb.toString()
            }

            // 수신 객체 지정 람다
            fun buildString2(
                builderAction: StringBuilder.() -> Unit
            ): String {
                val sb = StringBuilder()
                sb.builderAction()
                return sb.toString()
            }

            val s = buildString {
                it.append("hello, ")
                it.append("mj")
            }

            val s2 = buildString2 {
                append("hello, ")
                append("mj")
            }
        }

        "함수 타입을 확장하면서 invoke()를 오버라이딩할 수 있다" {
            data class 이슈(
                val id: String, val 프로젝트명: String, val 이슈타입: String, val 우선순위: String
            )

            class `중요 이슈 판별`(val 프로젝트명: String) : (이슈) -> Boolean {
                override fun invoke(이슈: 이슈): Boolean {
                    return 이슈.프로젝트명 == 프로젝트명 && 이슈.`중요한 이슈야?`()
                }

                private fun 이슈.`중요한 이슈야?`(): Boolean {
                    return 이슈타입 == "Bug" &&
                            (우선순위 == "Major" || 우선순위 == "Critical")
                }
            }

            val 이슈1 = 이슈("IDEA-123", "IDEA", "Bug", "Major")
            val 이슈2 = 이슈("KT-76", "Kotlin", "Feature", "Normal")

            val 판별자 = `중요 이슈 판별`("IDEA")
            for (이슈 in listOf(이슈1, 이슈2).filter(판별자)) {
                println(이슈.id)
            }
        }

        "유연한 DSL 문법 제공하기" {
            class `의존성 관리자` {
                fun 컴파일(의존성: String) {
                    println("${의존성}이 추가 되었습니다.")
                }

                operator fun invoke(
                    호출: `의존성 관리자`.() -> Unit
                ) {
                    호출()
                }
            }

            val 의존성들 = `의존성 관리자`()

            의존성들 {
                컴파일("spring cloud")
                컴파일("spring webflux")
            }
        }

        "날짜 조작 dsl을 정의할 수 있다" {
            val 어제 = 1.일.전
            val 내일 = 1.일.후

            println(어제)
            println(내일)
        }

        """
            멤버 확장을 사용하는 이유
            1. 메소드가 적용되는 범위를 제한하기 위해서
            
            멤버 확장의 단점
            1. 확장성이 떨어진다. 클래스 내부에 속해 있기 때문에 기존 클래스의 소스 코드를 손대지 않고 새로운 멤버 확장을 추가할 수 없다.
        """.trimIndent()
    }
}

val Int.일: Period
    get() = Period.ofDays(this)
val Period.전: LocalDate
    get() = LocalDate.now() - this
val Period.후: LocalDate
    get() = LocalDate.now() + this
