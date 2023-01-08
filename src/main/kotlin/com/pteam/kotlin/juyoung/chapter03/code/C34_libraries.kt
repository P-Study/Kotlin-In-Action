package com.pteam.kotlin.juyoung.chapter03.code

import java.util.Scanner

fun main() {
    val arr = arrayOf("a", "b", "c", "d")
    test(arr)

    // 중위 호출
    val map = mapOf(1 to "one") // to 메서드를 중위 호출 방식으로 호출, 일반 방식은 to("one")

    // 구조 분해 선언
    // to는 Pair의 인스턴스를 반환 -> Pair의 내용으로 두 변수를 즉시 초기화 가능
    val (number, name) = 1 to "one"
}

fun test(args: Array<String>) {
    // listOf 함수에서 vararg 변경자를 사용하여 가변인자로 정의
    // 스프레드 연산자(*)가 배열의 내용을 펼쳐줌
    val list = listOf("args: ", *args)
    println(list)
}