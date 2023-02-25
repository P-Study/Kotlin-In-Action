package com.pteam.kotlin.minzzang.chapter8

import com.pteam.kotlin.minzznag.chapter7.Person
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.condition.OS
import java.io.BufferedReader
import java.io.FileReader

class HighOrderFunctionTest : StringSpec() {
    init {
        "간단하게 고차 함수를 정의할 수 있다" {
            fun twoAndThree(operation: (Int, Int) -> Int): Int {
                return operation(2, 3)
            }

            twoAndThree { a, b -> a + b } shouldBe 5
        }

        "함수 타입의 파리미터도 디폴트 값을 지정할 수 있다" {
            fun String.act(
                act: (String) -> String = { it }
            ): String {
                return act(this)
            }

            "abc".act() shouldBe "abc"
            "abc".act { it.uppercase() } shouldBe "ABC"
        }

        "람다를 사용해 코드 중복을 줄일 수 있다" {
            data class SiteVisit(
                val path: String,
                val duration: Double,
                val os: OS
            )

            val log = listOf(
                SiteVisit("/", 1.0, OS.WINDOWS),
                SiteVisit("/", 2.0, OS.WINDOWS),
                SiteVisit("/", 3.0, OS.MAC),
                SiteVisit("/", 4.0, OS.WINDOWS),
            )
        }

        "크기가 작은 컬렉션은 시퀀스를 사용하지 않는게 성능이 좋다" {
            val person = Person("mj", 30)
            val person2 = Person("mj2", 35)
            val listOf = listOf(person, person2)

            listOf.filter { it.age > 30 }.map { it.name }
        }

        "use 함수를 사용해서 자바 try-with-resource를 구현할 수 있다" {
            fun readFirstLineFromFile(path: String): String {
                BufferedReader(FileReader(path)).use {
                    br -> return br.readLine()
                }
            }
        }

        "forEach는 인라인 함수이기 때문에 non-local return을 한다" {
            fun lookForMj(people: List<Person>): String {
                people.forEach {
                    if (it.name == "mj") {
                        return it.name
                    }
                }
                return ""
            }

            val person = Person("mj", 30)
            val person2 = Person("mj2", 35)
            val listOf = listOf(person, person2)

            lookForMj(listOf()) shouldBe "mj"
        }

        "lable을 사용하면 local return을 할 수 있다" {
            fun lookForMj(people: List<Person>): String {
                people.forEach label@{
                    if (it.name == "mj") {
                        return@label
                    }
                    return it.name
                }
                return ""
            }

            val person = Person("mj", 30)
            val person2 = Person("mj2", 35)
            val listOf = listOf(person, person2)

            lookForMj(listOf()) shouldBe ""
        }

        "무명 함수는 기본적으로 local 리턴이다" {
            val person = Person("mj", 30)
            val person2 = Person("mj2", 35)
            val listOf = listOf(person, person2)

            val filter = listOf.filter(fun(person): Boolean {
                return person.age < 30
            })

            filter shouldContainExactly emptyList()
        }
    }
}

enum class OS { WINDOWS, LINUX, MAC, IOS, AOS}
class Person(val name: String, val age: Int)
