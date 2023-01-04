package com.pteam.kotlin.juyoung.chapter02.code

class Rectangle(val height: Int, val width: Int) {
    val isSquare: Boolean
        get() { // 커스텀 게터
            return height == width
        }
}