package com.pteam.kotlin.chan.chapter03

fun main() {
    // 자바의 기능을 고대로
    val set = hashSetOf(1, 7, 53)
    val list = arrayListOf(1, 7, 53)
    val map = hashMapOf(1 to "one", 7 to "seven", 53 to "fifty- three")

    println(set.javaClass)    // class java.util.HashSet
    println(list.javaClass)   // class java.util.ArrayList
    println(map.javaClass)   // class java.util.HashMap

    // 코틀린으로 컬렉션의 추가기능을
    val strings = listOf("first", "second", "fourteenth")
    println(strings.last())     // fourteenth

    val numbers = setOf(1, 14, 2)
    println(numbers.max())      // 14
}