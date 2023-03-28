package com.pteam.kotlin.kade.chapter10.code

import org.jetbrains.annotations.NotNull

class AnnotationTarget {

    data class HasNotNullName(
        @get:NotNull
        val name: String?
    )

    fun printNotNullName(name: String) {
        val hasNullName = HasNotNullName(null)
        println(hasNullName.name)

        val hasNotNullName = HasNotNullName("Test")
        println(hasNotNullName.name)
    }

//    property 프로퍼티 전체 자바에서 선언된 애노테이션에는 이 시용 지점 대상 을 사용할수없다
//    field 프로퍼티에 의해 생성되는 (뒷받침하는) 필드
//    get 프로퍼티 게터
//    set 프로퍼티 세터
//    receiver 확장 함수나 프로퍼티의 수신 객체 파라미터 param 생성자 파라미터
//    setparam 세터 파라미터
//    delegate 위임 프로퍼티의 위임 인스턴스를 담아둔 필드
//    file 파일 안에 선언된 최상위 함수와 프로퍼티를 담아두는 클래스
}