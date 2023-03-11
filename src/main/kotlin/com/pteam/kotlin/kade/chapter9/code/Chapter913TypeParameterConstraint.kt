package com.pteam.kotlin.kade.chapter9.code

import java.lang.Appendable
import java.lang.StringBuilder

class Chapter913TypeParameterConstraint {

    /**
     * 타입 파라미터(T), 상한(Number) 를 통해 제약을 정의할 수 있다
     */
    // fun <T : Number> List<T>.sum() : T

    /**
     * 주의 : 함수의 인자들은 비교 가능해야 한다. 아닐 경우 컴파일 오류 발생
     */
    fun <T: Comparable<T>> max(first: T, second: T): T = if (first > second) first else second
    fun callMax() {
        // 타입이 동일하여 비교가 가능한 경우
        println(max("kotlin", "java"))

        // 타입이 동일하지 않아 비교가 불가능한 경우
        // Compile error : The integer literal does not conform to the expected type Comparable<String>
        // println(max("kotlin", 1))
    }

    /**
     * 타입 파라미터에 where구문을 통해 여러 제약을 줄 수 있다
     */
    fun <T> ensureTrailingPeriod(seq: T)
        where T : CharSequence, T : Appendable {
            if (!seq.endsWith('.')) {
                seq.append('.')
            }
        }
}