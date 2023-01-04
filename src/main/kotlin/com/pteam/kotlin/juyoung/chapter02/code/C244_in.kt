package com.pteam.kotlin.juyoung.chapter02.code

fun isLetter(c: Char) = c in 'a'..'z' || c in 'A'..'Z'
fun isNotDigit(c: Char) = c !in '0'.. '9'
fun recognize(c: Char) = when(c) {
    in '0'..'9' -> "digit"
    in 'a'..'z' -> "letter"
    else -> "???"
}

fun main() {
    println(isLetter('1'))
    println(isLetter('q'))
    println(isNotDigit('a'))
    println(isNotDigit('0'))

    println("Kotlin" in setOf("Java", "Scala"))
}
