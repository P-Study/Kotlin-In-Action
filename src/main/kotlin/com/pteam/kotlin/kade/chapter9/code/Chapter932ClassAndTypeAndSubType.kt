package com.pteam.kotlin.kade.chapter9.code

class Chapter932ClassAndTypeAndSubType {

    /**
     * 상위 타입은 하위 타입의 반대다
     * A 타입이 B 타입의 하위 타입이라면
     * B 타입은 A 타입의 상위 타입이다
     */
    fun test(i: Int) {
        // Int는 Number의 하위 타입이어서 컴파일이 가느하다
        val n: Number = i

        // Int는 String의 하위 타입이 아니어서 컴파일 되지 않음
        fun f(s: String) { }
//        f(i)
    }

    /**
     * null이 될 수 있는 변수는 null이 될 수 없는 변수의 상위 타입이다.
     * (하위 타입과 하위 클래스가 같지 않은 경우)
     */
    fun test2() {
        val s: String = "abc"
        val t: String? = s
    }

    /**
     * 제네릭 타입을 인스턴스화할 때 타입 인자로 서로 다른 타입이 들어가면 인스턴스 타입 사이의
     * 하위 타입 관계가 성립하지 않으면 제너릭 타입을 무공변이라고 한다
     */
}