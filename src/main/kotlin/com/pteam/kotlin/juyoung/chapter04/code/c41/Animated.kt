package com.pteam.kotlin.juyoung.chapter04.code.c41

abstract class Animated {
    // 추상 멤버는 항상 열려있기 때문에 open 변경자를 명시할 필요 없음
    abstract fun animate()

    // 비추상 함수는 기본적으로 final이지만 open으로 오버라이드 허용 가능
    open fun stopAnimating() {
        println("stopAnimating")
    }

    fun animateTwice() {
        println("animateTwice")
    }
}