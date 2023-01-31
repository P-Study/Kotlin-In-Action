package com.pteam.kotlin.kade.chapter4.code

/**
 * 데이터 클래스 : 컴파일러가 생성한 메소드
 *
 * toString, equals, hashCode
 *
 * 주 생성자 밖에 정의된 프로퍼티는 equals나 hashCode를 계산할 때 고려 대상이 아니다
 */
data class DataClass(
    val name: String,
    val email: String
)