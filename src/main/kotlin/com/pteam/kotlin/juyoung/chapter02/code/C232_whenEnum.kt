package com.pteam.kotlin.juyoung.chapter02.code

fun getMnemonic(color: Color2) =
    when (color) {
        Color2.RED -> "Richard"
        Color2.YELLOW -> "York"
        Color2.GREEN -> "Gave"
        Color2.BLUE -> "Battle"
    }

fun main() {
    println(getMnemonic(Color2.YELLOW))
}