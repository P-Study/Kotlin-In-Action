package com.pteam.kotlin.kade.chapter4.code

/**
 * 기반 클래스를 변경하는 경우 하위 클래스의 동작이 예기치 않게 바뀔 수도 있다는 면에서 기반 클래스는 취약하다
 * 취약한 문제를 해결하기 위해 상속을 위한 설계와 문서를 갖추거나, 상속을 금지하라고 한다
 * 코틀린은 기본적으로 클래스와 메소드는 final이다. (상속이 닫혀있다는 것이다)
 *
 * 상속을 열 수 있는 방법으로 open 키워드를 사용한다
 */
open class RichButton : Clickable {
    // 기본 메소드이기 때문에 final 이고 하위 클래스에서 오버라이드 할 수 없다
    fun disable() {}
    // open 키워드를 사용하였기 때문에 하위 클래스에서 오버라이드 할 수 있다.
    open fun animate() {}
    // 오버라이드한 메소드는 기본적으로 하위 클래스에 열려있다.
//    override fun click() {}
    // 오버라이드 금지하는 방법
    final override fun click() {}
}

abstract class Animated {
    // 하위 클래스에서 반드시 오버라이드 해야 한다
    abstract fun animate()

    /**
     * 추상 클래스에서 비추상 함수는 기본적으로 파이널이다
     * open 키워드로 오버라이드 허용 가능하다
     */
    open fun stopAnimating() {

    }
    fun animateTwice() {

    }
}