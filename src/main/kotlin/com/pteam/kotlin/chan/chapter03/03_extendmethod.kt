package com.pteam.kotlin.chan.chapter03

import com.pteam.kotlin.chan.chapter03.util.joinToString
import kotlin.text.StringBuilder
import com.pteam.kotlin.chan.chapter03.util.lastChar as last

fun main() {
    // 확장 함수 사용 및 "as" 키워드 적용
    val str = "leeheechan"
    println(str.last())     // n

    // 유틸 함수를 확장 함수로:: joinToString() 확장 함수로 변경
    val list = listOf(1, 2, 3)
    println(list.joinToString("; ", "(", ")"))        // (1; 2; 3)

    // 확장 프로퍼티 사용
    val sb = StringBuilder("Kotlin?")
    println(sb.lastC)   // ?
    sb.lastC = '!'
    println(sb)    // Kotlin!
}

// 확장 프로퍼티
var StringBuilder.lastC: Char
    get() = get(length - 1)
    set(value: Char) {
        this.setCharAt(length - 1, value)
    }