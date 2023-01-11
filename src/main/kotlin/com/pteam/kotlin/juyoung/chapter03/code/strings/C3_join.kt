package com.pteam.kotlin.juyoung.chapter03.code.strings

fun<T> joinToString(
    collections: Collection<T>,
    // 디폴트 파라미터 개념이 자바에 없음 -> joinToString에 @JvmOverloads 애노테이션 사용하면 오버로딩 함수가 만들어 짐
    separator: String = ", ",
    prefix: String = "",
    postfix: String = ""
//    separator: String,
//    prefix: String,
//    postfix: String
): String {
    val result = StringBuilder(prefix)

    for ((index, element) in collections.withIndex()) {
        if(index > 0) result.append(separator)
        result.append(element)
    }
    result.append(postfix)

    return result.toString()
}

fun<T> Collection<T>.joinToString2(
    separator: String = ", ",
    prefix: String = "",
    postfix: String = ""
): String {
    val result = StringBuilder(prefix)

    for((index, element) in this.withIndex()) { // this는 수신객체로 T타입의 원소로 이뤄진 컬렉화
        if(index > 0) result.append(separator)
        result.append(element)
    }

    result.append(postfix)
    return result.toString()
}

fun Collection<String>.join(
    separator: String = ", ",
    prefix: String = "",
    postfix: String = ""
) = joinToString2(separator, prefix, postfix)

//fun String.lastChar(): Char = this.get(this.length - 1)
//fun String.lastChar(): Char = this[this.length - 1]
fun String.lastChar(): Char = get(length - 1)   // 수신객체 멤버에 this 없이 접근

// 확장 프로퍼티
//val String.lastChar: Char
//    get() = get(length - 1)

// 변경 가능한 확장 프로퍼티
var StringBuilder.lastCharProperty: Char
    get() = get(length - 1)
    set(value: Char) {
        this.setCharAt(length - 1, value)
    }