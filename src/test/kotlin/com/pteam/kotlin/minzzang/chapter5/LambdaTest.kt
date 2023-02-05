package com.pteam.kotlin.minzzang.chapter5

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe

internal class LambdaTest : StringSpec() {
    init {
        "메소드가 하나뿐인 무명 객체 대신 람다를 사용할 수 있다" {
            object : Runnable {
                override fun run() {
                    println("무명 객체 실행")
                }
            }.run()

            Runnable { println("람다 실행") }.run()
        }

        "람다를 사용하면 컬렉션을 쉽게 검색할 수 있다" {
            val pDev = listOf(Person("mj", 26), Person("heefull", 27))

            val oldMan = pDev.maxBy { p -> p.age } // 람다
            val youngMan = pDev.minBy(Person::age) // 멤버 참조

            oldMan.name shouldBe "heefull"
            youngMan.name shouldBe "mj"
        }

        "람다의 파라미터가 하나뿐이고 컴파일러가 타입 추론이 가능하다면 it을 사용할 수 있다" {
            val pDev = listOf(Person("mj", 26), Person("heefull", 27))

            pDev.maxBy { it.age }.age shouldBe 27
        }

        "final이 아닌 변수도 람다 안에서 접근할 수 있다" {
            fun what(): Int {
                var count = 0
                val counter = { count++ }

                counter()
                counter()
                return count
            }

            what() shouldBe 2
        }

        "바운드 멤버 참조는 수신 대상 객체를 별도로 지정할 필요가 없다" {
            val person = Person("mj", 30)
            val ageFunction = person::age

            person.age = 27

            ageFunction() shouldBe 27
        }

        "모든 원소가 만족하면 all, 하나라도 있는지 궁금하면 any" {
            val p팀에서_제일_어리니 = { p: Person -> p.age <= 27}

            val p팀 = listOf(Person("민제", 30), Person("희찬", 27))

            p팀.all(p팀에서_제일_어리니) shouldBe false
            p팀.any(p팀에서_제일_어리니) shouldBe true
        }

        "컬렉션의 크기를 계산할 때는 size대신 count를 사용해라" {
            val p팀 = listOf(Person("민제", 30), Person("희찬", 27))

            p팀.filter { p -> p.age > 26 }.count() shouldBe 2
        }

        "중첩된 리스트의 원소를 한 리스트로 모을 때는 flatMap을 사용하라" {
            class Book(val title: String, val authors: List<String>)

            val books = listOf(
                Book("스프링", listOf("이희찬", "장민제")),
                Book("코틀린", listOf("장민제")),
                Book("자바", listOf("이희찬"))
            )

            books.flatMap { it.authors }.toSet() shouldContainExactly listOf("이희찬", "장민제")
        }

        "큰 컬렉션에 대해서 연산을 연쇄할 때는 시퀀스를 사용하라"{
            val people = listOf(
                Person("이희찬", 40),
                Person("장민제", 30)
            )

            val person = people.asSequence()
                .map(Person::name)
                .filter { it.startsWith("장") }
                .toList()

            person shouldBe "장민제"
        }

        "컬렉션에 대해 수행하는 연산의 순서도 성능에 영향을 끼친다" {
            val people = listOf(
                Person("이희찬", 40),
                Person("장민제", 30)
            )

            val mapFirst = people.asSequence()
                .map(Person::age)
                .filter { it > 30 }
                .toList()

            val filterFirst = people.asSequence()
                .filter { it.age > 30 }
                .map(Person::age)
                .toList()

            mapFirst shouldBe  filterFirst
        }

        "람다가 주변 영역의 변수를 포획하면 매 호출마다 다른 인스턴스를 사용한다" {
            fun handleComputation(id: String) {
                Runnable { print(id) }
            }
        }

        "with를 사용하면 객체의 이름을 반복하지 않고도 객체에 다양한 연산을 사용할 수 있다" {
            fun p개발팀() = with(StringBuilder()) {
                append("김록영")
                append("이희찬")
                append("유형민")
                append("장민제")
                append("방은혁")
                append("이주영")
                append("박진형")
            }

            buildString {  }
            p개발팀() shouldBe "김록영이희찬유형민장민제방은혁이주영박진형"
        }

        "수신 객체를 반환하고 싶을 때는 apply를 사용해라" {
            val apply = StringBuilder().apply {
                append("수신 객체")
                append("반환")
            }

            apply.javaClass.name shouldBe "java.lang.StringBuilder"
        }
    }
}

data class Person(val name: String, var age: Int)
