package com.pteam.kotlin.kade.chapter4.code

import java.io.Serializable

/**
 * 클래스의 구현에 대한 접근을 제한함으로써 그 클래스에 의존하는 외부 코드를 깨지 않고도 클래스 내부 구현을 변경할 수 있다
 *
 * internal -> 모듈 내부에서만 볼 수 있는 특성을 가진 keyword : 모듈이란 한 번에 컴파일되는 코틀린 파일
 */

internal open class TalkativeButton : Focusable {
    private fun yell() = println("Hey!")
    protected fun whisper() = println("Let's talk!")
}

/**
 * 자신보다 가시성이 낮은 타입을 참조하지 못하게 한다.
 *
 * 'public' member exposes its 'internal' receiver type TalkativeButton
 */
fun TalkativeButton.giveSpeech() {
    // Cannot access 'yell': it is private in 'TalkativeButton'
    yell()
    // Cannot access 'whisper': it is protected in 'TalkativeButton'
    whisper()
}
interface Focusable {
    fun setFocus(b: Boolean) = println("I ${if (b) "got" else "lost"} focus.")
    fun showOff() = println("I'm focusable!")
}

/**
 * 중첩 클래스는 명시적으로 요청하지 않는 한 바깥쪽 클래스 인스턴스에 대한 접근 권한이 없다
 */
interface State: Serializable
interface Viewu {
    fun getCurrentSate(): State
    fun restoreState(state: State) {}
}
class Buttone : Viewu {
    override fun getCurrentSate(): State = ButtonState()
    override fun restoreState(state: State) {}
    // Java의 static 클래스와 동일하다
    class ButtonState : State {}
}

/**
 * 바깥쪽 클래스의 인스턴스를 가리키기 위해선 내부 클래스를 inner키워드를 통해 내부 클래스로 정의하면 된다
 */
class Outer {
    inner class Inner {
        fun getOuterReference(): Outer = this@Outer
    }
}