package com.pteam.kotlin.juyoung.chapter05.code

fun main() {
    println("[5.4.2] sam 생성자: 람다를 함수형 인터페이스로 명시적으로 변경")
    createAllDoneRunnable().run()
}

fun createAllDoneRunnable(): Runnable {
    return Runnable { println("All done!") }
}