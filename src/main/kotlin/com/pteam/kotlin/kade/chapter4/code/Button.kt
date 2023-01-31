package com.pteam.kotlin.kade.chapter4.code

class Button : Clickable, Focusable {
    /**
     * override 변경자 필수!
     * override 변경자는 상위 클래스나 상위 인터페이스에 있는 프로퍼티나 메소드를 오버라이드한다는 표시
     * 상위 클래스에 있는 메소드와 동일한 시그니처를 가지는 메소드가 있을 경우 컴파일 시점에 오류가 발생하기 때문에 override를 표시해줘야함
     */
    override fun click() = println("I was clicked")

    /**
     * Clickable, Focusable에 오버라이드를 해야하는 시그니처가 같은 메소드가 존재한다
     * 이를 오버라이드 하기 위해 동일한 시그니처의 메소드를 하나만 선언하면 된다
     */
    override fun showOff() {
        super<Clickable>.showOff()
        super<Focusable>.showOff()
    }
}