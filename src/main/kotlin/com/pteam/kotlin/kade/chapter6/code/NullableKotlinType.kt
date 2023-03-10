package com.pteam.kotlin.kade.chapter6.code

class NullableKotlinType {

    fun strLen(s: String) = s.length

//    // nullable variable 제약 1
//    fun strLenSafe(s: String?) = s.length
//
//    // nullable variable 제약 2
//    fun secondNullableConstraints() {
//        val x: String? = null
//        val y: String = x
//    }
//
//    // nullable variable 제약 3
//    fun thirdNullableConstraints() {
//        val x:String? = null
//        strLen(x)
//    }

    fun strLenSafe(s: String?): Int = if (s != null) s.length else 0
}