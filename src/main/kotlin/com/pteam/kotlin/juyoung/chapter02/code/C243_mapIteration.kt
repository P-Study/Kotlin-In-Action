package com.pteam.kotlin.juyoung.chapter02.code

import java.util.TreeMap

fun main() {
    val binaryReps = TreeMap<Char, String>()
    for(c in 'A'..'F') {
        val binary = Integer.toBinaryString(c.code)
        binaryReps[c] = binary
    }

    for((letter, binary) in binaryReps) {
        println("$letter = $binary")
    }

    var list = arrayListOf("10", "11", "1001")
    for((index, element) in list.withIndex()) {
        println("$index = $element")
    }
}