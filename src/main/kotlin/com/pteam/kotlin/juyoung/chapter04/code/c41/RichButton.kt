package com.pteam.kotlin.juyoung.chapter04.code.c41

// 열린 클래스: open 변경자를 사용하면 다른 클래스가 이 클래스를 상속 가능
open class RichButton: Clickable {
    fun disable() {}    // final 함수로 오버라이드 불가
    open fun animate() {}   // 오버라이드 가능
    final override fun click() {}   // final을 사용하여 오버라이드 금지
}