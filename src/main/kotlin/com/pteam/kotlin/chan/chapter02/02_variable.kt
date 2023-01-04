package com.pteam.kotlin.chan.chapter02

fun main() {
    /**
     * 정수
     */
    val intValue = 1234
    val longValue = 1234L
    val intValueByHex = 0x1af
    val intValueByBin = 0b010101101
    // 8진수 지원하지 않음.

    println(intValue)
    println(longValue)
    println(intValueByHex)
    println(intValueByBin)


    /**
     * 소수
     */
    val doubleValue = 123.5
    val doubleValueWithExp = 123.5e10
    val floatValue = 123.5f

    println(doubleValue)
    println(doubleValueWithExp)
    println(floatValue)


    /**
     * 문자
     */
    val charValue = 'a'
    val korCharValue = '가'

    println(charValue)
    println(korCharValue)


    /**
     * 논리 값
     */
    val booleanValue = true

    println(booleanValue)


    /**
     * 문자열
     */
    val stringValue = "문자열입니다."
    val multiLineStringValue = """
        이것은 멀티 라인 문자열입니다.
            신기하죠?
        
        ㅋㅋㅋ
    """.trimIndent()

    println(stringValue)
    println(multiLineStringValue)
}