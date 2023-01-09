package com.pteam.kotlin.juyoung.chapter03.code

import com.pteam.kotlin.juyoung.chapter03.code.strings.joinToString

var opCount = 0     // 최상위 프로퍼티

fun performOperation() {
    opCount++
}

fun reportOperationCount() {
    println("opCount: $opCount")
}

fun main() {
    val list = listOf(1, 2, 3)
    println(joinToString(list, ";", "(", ")"))

    // 이름 붙인 인자(자바에서는 사용 불가)
    println(joinToString(list, separator = " ", prefix = "", postfix = "."))

    println(joinToString(list))

    performOperation()
    reportOperationCount()
}