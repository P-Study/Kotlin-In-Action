package com.pteam.kotlin.juyoung.chapter04.code.c41

interface Clickable {
    fun click()

    // 디폴트로 구현 제공
    fun showOff() = println("I'm clickable!")
}