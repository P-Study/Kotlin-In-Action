package com.pteam.kotlin.kade.chapter9.code

class Chapter934Contravariance {

    /**
     * 공변 관계와 반대
     */
    interface Comparator<in T> {
        fun compare(e1: T, d2: T): Int = 1
    }

//    val anyComparator = Comparator<Any> (el, e2 -> el.hashCode() - e2.hashCode()
    val strings : List<String> = arrayListOf("")

    /**
     * 공변성, 반공변성 둘 다 사용 가능
     */
    interface Function<in T, out R> {
        operator fun invoke (p: T) : R
    }
}