package com.pteam.kotlin.kade.chapter4.code

interface Clickable {
    fun click()

    /**
     * Java 8의 default 키워드를 사용하는 것과 정의형태는 동일
     * 하지만 Kotlin은 Java 6과 호환되게 설계되어 있어 정적 메소드가 들어있는 클래스를 조합해 구현함
     */
    fun showOff() = println("I'm clickable!")
}