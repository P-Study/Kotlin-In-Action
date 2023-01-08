package com.pteam.kotlin.juyoung.chapter03.code

fun main() {
    val set = hashSetOf(2, 34, 73)
    val list = arrayListOf(41, 34, 53)
    val map = hashMapOf(1 to "one", 3 to "three", 25 to "twenty-five")  // to는 일반 함수(키워드 아님)

    println(set.javaClass)
    println(list.javaClass)
    println(map.javaClass)

    val strings = listOf("first", "second", "fourteenth")
    println(strings.last())

    val numbers = setOf(1, 14, 2)
    println(numbers.max())
}