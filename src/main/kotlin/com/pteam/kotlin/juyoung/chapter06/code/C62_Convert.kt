package com.pteam.kotlin.juyoung.chapter06

fun main() {
    val x = 1
    val list = listOf(1L, 2L, 3L)
//    println(x in list)
    println(x.toLong() in list)

    val xx = x + 1L
    println(xx in list)
}