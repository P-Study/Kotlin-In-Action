package com.pteam.kotlin.chan.chapter02

fun main() {

    var a = 7
    var b = 0

    // 기본 조건문
    if (a > 10) {
        println("a는 10보다 크다.")
    }

    // when문 사용
    doWhen(a)

    // while문
    while (b < 5) {
        print(" " + b++)
    }
    println()

    // do-while문
    do {
        print(" " + b--)
    } while (b > 0)
    println()

    // for문
    for (i in 0..9 step 3) {
        print(" $i")
    }
    println()

    // downTo 키워드
    for (i in 9 downTo 0) {
        print(" $i")
    }
    println()

}

fun doWhen(a: Any) {
    val result = when(a) {
        1 -> println("정수 1입니다.")
        "chan" -> println("찬입니다.")
        is Long -> println("Long 타입 입니다.")
        !is String -> println("String 타입이 아닙니다.")
        else -> println("아무것도 아닙니다.")
    }

    return result
}