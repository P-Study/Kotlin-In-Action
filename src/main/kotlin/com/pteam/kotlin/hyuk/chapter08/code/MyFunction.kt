package com.pteam.kotlin.hyuk.chapter08.code

inline fun myInlineFunction(arg: Int = 100, operation: (Int) -> Unit) {
    println("start myInlineFunction")
    operation(arg)
    println("End myInlineFunction")
}

fun myNormalFunction(arg: Int = 100, operation: (Int) -> Unit) {
    println("start myNormalFunction")
    operation(arg)
    println("End myNormalFunction")
}

fun getOddsUsingSequence(list: List<Int>): List<Int> {
    return list.asSequence()
        .filter { it % 2 == 1 }
        .toList()
}

fun getOdds(list: List<Int>): List<Int> {
    return list.filter { it % 2 == 1 }.toList()
}