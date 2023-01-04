package com.pteam.kotlin.juyoung.chapter02.code

import com.pteam.kotlin.juyoung.chapter02.code.Color2.*

fun mixOptimized(c1: Color2, c2: Color2) =
    when {
        (c1 == YELLOW && c2 == BLUE) ||
                (c1 == BLUE && c2 == YELLOW) -> GREEN
        else -> throw Exception("Dirty Color")
    }

fun main() {
    println(mixOptimized(BLUE, YELLOW))
    println(mixOptimized(RED, GREEN))
}