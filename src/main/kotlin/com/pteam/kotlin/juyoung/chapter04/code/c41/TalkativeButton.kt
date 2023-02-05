package com.pteam.kotlin.juyoung.chapter04.code.c41

internal open class TalkativeButton: Focusable {
    private fun yell() = println("Hey!")
    protected fun whisper() = println("Let's talk!")
}

// 오류를 없애려면 giveSpeech를 internal로 바꾸거나 TalkativeButton의 가시성을 public으로 바꿔야 함
//fun TalkativeButton.giveSpeech() {   // public 멤버가 internal TalkativeButton 노출
//    yell()    // yell은 private멤버로 접근 불가
//    whisper() // protected멤버로 접근 불가
//}