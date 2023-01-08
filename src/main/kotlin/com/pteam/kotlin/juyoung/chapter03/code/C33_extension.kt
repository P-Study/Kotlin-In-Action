package com.pteam.kotlin.juyoung.chapter03.code

import com.pteam.kotlin.juyoung.chapter03.code.strings.join
import com.pteam.kotlin.juyoung.chapter03.code.strings.joinToString2
import com.pteam.kotlin.juyoung.chapter03.code.strings.lastChar    // as 를 사용하여 이름 충동 방지
import com.pteam.kotlin.juyoung.chapter03.code.strings.lastCharProperty

fun main() {
    val c = "Kotlin".lastChar()
    println(c)

    val list = arrayListOf(1, 2, 3)
    println(list.joinToString2())
    println(list.joinToString2(seperator = "; ", prefix = "(", postfix = ")"))
    println(list.joinToString2(" "))

    println(listOf("a", "b", "c").join())

    val button = Button();
    button.click()

    val view: View = Button()   // view가 가리키는 실제 타입이 Button이지만 view의 타입은 View
    view.showOff()  // 확장 함수는 오버라이드 불가능

    val sb = StringBuilder("Kotlin?")
    sb.lastCharProperty = '!'
    println(sb)
}

open class View {   // open 변경자를 붙여서 상속 허용
    open fun click() = println("View clicked")
}

class Button: View() {  // View 확장
    override fun click() = println("Button clicked")
}

// 확장 함수
fun View.showOff() = println("I'm a view!")
fun Button.showOff() = println("I'm a button!")