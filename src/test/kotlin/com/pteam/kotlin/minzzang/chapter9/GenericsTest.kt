package com.pteam.kotlin.minzzang.chapter9

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

internal class GenericsTest : StringSpec() {
    init {
        "타입 파라미터의 상한을 정할 수 있다" {
            fun <T : Comparable<T>> max(first: T, second: T): T {
                return if (first > second) first else second
            }
            max("ara", "mj") shouldBe "mj"
        }

        "타입 파라미터의 상한은 반드시 그 상한 타입과 같거나 하위 타입이어야 한다" {
            open class 아버지 : 부모님
            class 할아버지 : 조부모님
            class 아들 : 아버지()

            fun <T : 부모님> 상한가능(self: T): T {
                return self
            }

            상한가능(아들())
            상한가능(아버지())
//            상한가능(할아버지())
        }

        "인라인 함수에서는 실체화한 타입 인자를 쓸 수 있다" {
            listOf("a", 123, "mj").filterIsInstance<Int>() shouldBe listOf(123)
        }

        "무공변은 제네릭 타입을 인스턴스화 할 때 타입 인자로 서로 다른 타입이 들어가면 인스턴스 타입 사이의 하위 타입 관계가 성립하지 않는다" {
            class Herd<T : Animal> {
                val list = mutableListOf<T>()
                val size = list.size
                operator fun get(i: Int): T {
                    return list[i]
                }

                fun set(animal: T) {
                    list.add(animal)
                }
            }

            fun feedAll(herd: Herd<Animal>) {
                for (i in 0 until herd.size) {
                    herd[i].feed()
                }
            }

            val cats = Herd<Cat>()
            cats.set(Cat())
            cats.set(Cat())
            cats.set(Cat())

//            feedAll(cats)
        }

        "공변으로 만들면 제네릭 하위 타입 관계가 유지 된다" {
            class Herd<out T : Animal> {
                val list = listOf<T>()
                val size = list.size
                operator fun get(i: Int): T {
                    return list[i]
                }
            }

            fun feedAll(herd: Herd<Animal>) {
                for (i in 0 until herd.size) {
                    herd[i].feed()
                }
            }

            val cats = Herd<Cat>()

            feedAll(cats)
        }

        "반 공변성은 타입 인자의 하위 타입 관계가 제네릭 타입에서 뒤집힌다" {
            fun enumerateCats(f: (Cat) -> Number) {
                f
            }

            fun Animal.getIndex() = 3

            enumerateCats(Animal::getIndex)
        }

        "코틀린의 사용 지점 변셩 선언은 자바의 한정 와일드 카드와 같다" {
            fun <T> copyData(source: MutableList<T>,
                             destination: MutableList<in T>) {
                for (item in source) {
                    destination.add(item)
                }
            }

            copyData(mutableListOf<Int>(), mutableListOf<Number>())
        }

        "스타 프로젝션은 Any?와 같지 않다." {
            val list: MutableList<Any?> = mutableListOf("a", 1)
            val chars = mutableListOf('a', 'b')

            val unknown: MutableList<*> = list

            list.add(32)
//            unknown.add(32)
        }
    }
}

interface 부모님 : 조부모님
interface 조부모님

inline fun <reified T>
        Iterable<*>.filterIsInstance(): List<T> {
    val destination = mutableListOf<T>()
    for (element in this) {
        if (element is T) {
            destination.add(element)
        }
    }
    return destination
}


open class Animal {
    open fun feed() {
        println("울음 소리 ~")
    }
}

class Cat : Animal() {
    fun clean() {
        println(" 냐 아 옹 ~ ")
    }
}
