package com.pteam.kotlin.kade.chapter9.code

import java.util.*

class Chapter933Covariant {
    /**
     * A가 B의 하위 타입이면 List<A>는 List<B>의 하위 타입이다.
     * 이것을 공변적이라고 한다.
     */
    interface Producer<out T> { // 클래스가 T에 대해 공변적이라고 한다
        fun produce() : T
    }

    open class Animal {
        class Herd<out T : Animal> {
            val size: Int get() = 1
            operator fun get(i: Int): T {
                val herds = Herd<T>()
                return herds[i]
            }
        }

        fun feed() {}

        fun feedAll(animals: Herd<Animal>) {
            for (i in 0 until animals.size) {
                animals[i].feed()
            }

        }


        class Cat : Animal() {
            fun cleanLitter() {}
        }

        /**
         * 무공변 컬렉션이기 때문에 타입 불일치 오류 발생
         * 공변성을 부여해주면 오류 해결 가능하다
         */
        fun takeCareOfCats(cats: Animal.Herd<Cat>) {
            for (i in 0 until cats.size) {
                cats[i].cleanLitter()
                feedAll(cats)
            }
        }
    }

    interface TransformOut<out T> {

        /**
         * out 키워드를 사용하면 in 위치에서 사용하지 못하게 제한을 두고
         * 하위 타입 관계가 유지 된다.
         * out위치가 꼭 반환 타입 위치는 아니다
         */
        fun transform(): T
    }

    /**
     * 변셩 규칙은 클래스 외부의 사용자가 잘못 사용하는 것을 막기 위함이므로
     * 클래스 내부 구현에 적용되지 않는다.
     */
}