package com.pteam.kotlin.kade.chapter9.code

/**
 * Variance(변성) = 기저 타입이 같고 타입 인자가 다른 여러 타입이 서로 어떤 관계가 있는지 설명하는 개념
  */
class Chapter931Variance {

    /**
     * 문자열로 호출할 경우 원소를 Any로 취급하여 안전하게 호출한다
     */
    fun printContents(list: List<Any>) {
        println(list.joinToString())
    }

    /**
     * 문자열 리스트를 넘겼을 때 ClassCastException이 떨어진다
     */
    fun addAnswer(list: MutableList<Any>) {
        list.add(42)
    }
}