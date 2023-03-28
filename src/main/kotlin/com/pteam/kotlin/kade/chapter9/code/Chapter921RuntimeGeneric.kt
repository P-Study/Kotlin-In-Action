package com.pteam.kotlin.kade.chapter9.code

import java.lang.IllegalArgumentException

/**
 * 제너릭 타입 인자 정보는 런타임 시 지워진다
 * 제너릭 클래스 인스턴스가 생성될 때 쓰인 타입 인자에 대한 정보를 유지할 수 없다는 것이다.
 */
class Chapter921RuntimeGeneric {

    // 실행 시점에 두 리스트는 같은 타입의 객체가 된다
    // 컴파일러가 타입 인자를 올바르게 갖도록 보장해준다
    val list1: List<String> = listOf("a", "b")
    val list2: List<Int> = listOf(1, 2, 3)

    /**
     * 실행 시점 타입 소거로 인해 타입 인자를 검사할 수 없다
     * 근데 되네?? 뭐지??
     */
    fun validateList() {
        if (list1 is List<String>) { }
        if (list1 is List<*>) { }
    }

    /**
     * 제너릭 타입으로 캐스팅하기
     */
//    fun printSum(c: Collection<*>) {
//        val initList = c as? List<Int> ?: throw IllegalArgumentException("List is expected")
//        println(initList.sum())
//    }

    /**
     * 실행 시점에 Exception이 발생한다
     */
//    fun callPrintSum() {
//        printSum(setOf(1, 2, 3))
//        printSum(listOf("A", "B", "C"))
//    }

    /**
     * 알려진 타입 인자를 사용해 타입 검사하기
     */
    fun printSum(c: Collection<Int>) {
        if (c is List<Int>) {
            println(c.sum())
        }
    }
}