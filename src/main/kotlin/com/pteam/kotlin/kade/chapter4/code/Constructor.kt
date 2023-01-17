package com.pteam.kotlin.kade.chapter4.code

import javax.naming.Context
import javax.swing.text.AttributeSet

/**
 * 모든 생성자 파라미터에 디폴트 값을 지정하면 컴파일러가 자동으로 파라미터가 없는 생성자를 만들어준다.
 */
class User(val nickname: String) // 주 생성자

/**
 * 기반 클래스의 이름 뒤에는 꼭 빈 괄호가 들어간다
 * 인터페이스 이름 뒤에는 괄호가 없다
 */
class RadioButton: Buttone()

/**
 * 인자에 대한 디폴트 값을 제공하기 위해 부 생성자를 여럿 만들지 말라.
 * 대신 파라미터의 디폴트 값을 생성자 시그니처에 직접 명시하라
 *
 * 그래도 여러 생성자가 필요한 경우가 있다
 */
open class View {
    // 생성자를 통해 상위 클래스에게 객체 생성을 위임한다
    constructor(ctx: Context) : super(ctx) {

    }
    constructor(ctx: Context, attr: AttributeSet) : super(ctx, attr) {

    }
}