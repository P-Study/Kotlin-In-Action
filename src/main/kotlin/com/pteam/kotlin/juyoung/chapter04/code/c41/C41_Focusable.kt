package com.pteam.kotlin.juyoung.chapter04.code.c41

// 인터페이스 멤버는 final, open, abstract를 사용하지 않음
// 인터페이스 멤버는 항상열려있으며 final로 변경 불가
interface C41_Focusable {
    fun setFocus(b: Boolean) = println("I ${if (b) "got" else "lost"} focus.")
    fun showOff() = println("I'm focusable!")
}