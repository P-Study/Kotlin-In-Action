package com.pteam.kotlin.juyoung.chapter02.code

import java.util.Random // 표준 자바 라이브러리 클래스 임포트
import com.pteam.kotlin.juyoung.chapter02.code.function.createRandomRectangle   // 다른 패키지 함수 임포트

fun main() {
    val random = Random()
    println(random)

    println(createRandomRectangle().isSquare)
}