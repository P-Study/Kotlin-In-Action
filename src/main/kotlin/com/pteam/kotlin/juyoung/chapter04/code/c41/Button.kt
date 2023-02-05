package com.pteam.kotlin.juyoung.chapter04.code.c41

// 코틀린에서는 콜론으로 클래스 확장과 인터페이스 구현을 모두 처리
class Button: Clickable, Focusable {
    override fun click() = println("I was clicked")

    /*
     명시적으로 새로운 구현 제공
     super<상위타입 이름>을 사용하여 어떤 상위 타입의 멤버 메소드를 호출할 지 지정
     */
    override fun showOff() {
        super<Clickable>.showOff()
        super<Focusable>.showOff()
    }

    // 상속한 구현 중 하나만 사용할 때는 아래처럼
    // override fun showOff() = super<Clickable>.showOff()
}

fun main() {
    val button = Button()
    button.showOff()
    button.setFocus(true)
    button.click()
}