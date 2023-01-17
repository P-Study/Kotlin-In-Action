package com.pteam.kotlin.kade.chapter4.code

import io.kotest.core.spec.style.FunSpec

class InterfaceC411Test : FunSpec({
    test("인터페이스 Clickable의 click method를 구현한 Button클래스의 click을 호출합니다.") {
        Button().click()
    }

    test("시그니처가 같은 메소드를 상속하는 경우 메소드를 호출할 수 있는지 확인한다") {
        val button = Button();
        button.showOff()
        button.setFocus(true)
        button.click()
    }
})