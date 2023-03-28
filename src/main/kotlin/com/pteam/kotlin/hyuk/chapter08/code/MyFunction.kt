package com.pteam.kotlin.hyuk.chapter08.code

inline fun myInlineFunction(arg: Int = 200, operation: (Int) -> Unit) {
    println("start myInlineFunction")
    operation(arg)
    println("End myInlineFunction")
}

val a = { println("hi") }
fun foo() {
    println("start myInlineFunction")

    listOf("hi").filter { it-> it == "hi" }

    println("End myInlineFunction")
}

inline fun foo2() {
    println("start myInlineFunction")

    listOf("hi").filter { it-> it == "hi" }

    println("End myInlineFunction")
}



fun myNormalFunction(arg: Int = 200, operation: (Int) -> Unit) {
    println("start myNormalFunction")
    operation(arg)
    println("End myNormalFunction")
}

fun main() {
    myInlineFunction(100) {
        for (i in 0..it) {
            /* noting */
        }
    }

    myNormalFunction(100) {
        for (i in 0..it) {
            /* noting */
        }
    }

    foo()
}


fun getOddsUsingSequence(list: List<Int>): List<Int> {
    return list.asSequence()
        .filter { it % 2 == 1 }
        .toList()
}

fun getOdds(list: List<Int>): List<Int> {
    return list.filter { it % 2 == 1 }.toList()
}
