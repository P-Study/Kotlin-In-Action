package com.pteam.kotlin.kade.chapter3.code

// listOf 인자를 여러 개 전달할 수 있다
val list = listOf(2, 3, 4, 5, 6, 7, 8)

// mapOf 인자를 여러 개 전달할 수 있다.
// infix 함수는 객체 - 메소드 이름 - 인자 를 통해 호출한다
val map = mapOf(1 to "one", 7 to "seven", 53 to "fifty-three")

fun destructuringDeclaration() {
    for ((index, element) in list.withIndex()) {
        println("$index, $element")
    }
}