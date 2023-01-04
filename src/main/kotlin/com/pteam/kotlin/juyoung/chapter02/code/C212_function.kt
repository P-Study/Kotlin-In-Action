package com.pteam.kotlin.juyoung.chapter02.code

fun max(a: Int, b: Int): Int {
    return if (a > b) a else b
}

fun max2(a: Int, b: Int): Int = if (a > b) a else b

fun max3(a: Int, b: Int) = if (a > b) a else b

fun main() {
    println(max(46, 43))
    println(max2(68, 11))
    println(max3(3, 90))
}