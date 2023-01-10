package com.pteam.kotlin.chan.chapter03

import com.pteam.kotlin.juyoung.chapter03.code.strings.joinToString

fun main() {
    // toString()이 이미 있음
    val list1 = listOf(1, 2, 3)
    println(list1)      // [1, 2, 3]

    // custom toString()
    val list2 = listOf(1, 2, 3)
    println(joinToString(list2, "; ", "(", ")"))        // (1; 2; 3)

    // 함수 매개변수에 값 지칭하기
    println(joinToString(list2, prefix = "(", separator = ", ", postfix = ")"))     // (1, 2, 3)

    // 디폴트 매개변수 값 사용하기
    println(joinToString(list2))    // 1, 2, 3
}

fun <T> joinToString(
    collections: Collection<T>,
    separator: String = ", ",
    prefix: String = "",
    postfix: String = ""
): String {
    val result = StringBuilder(prefix)

    for ((index, element) in collections.withIndex()) {
        if (index > 0) {
            result.append(separator)
        }
        result.append(element)
    }

    result.append(postfix)
    return result.toString()
}
