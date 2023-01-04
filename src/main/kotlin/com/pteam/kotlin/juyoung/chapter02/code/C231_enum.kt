package com.pteam.kotlin.juyoung.chapter02.code

enum class Color {
    REC, YELLOW, GREEN, BLUE
}

// 프로퍼티와 메소드가 있는 enum 클래스 선언하기
enum class Color2(var r: Int, var g: Int, var b: Int) {
    RED(255, 0, 0),
    YELLOW(255, 255, 0),
    GREEN(0, 255, 0),
    BLUE(0, 0, 255)
}