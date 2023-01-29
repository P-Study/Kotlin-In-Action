package com.pteam.kotlin.juyoung.chapter05.code

fun main() {
    println("[5.5.1] with 함수")
    println(alphabetWith())
    println("[5.5.2] apply 함수")
    // 객체의 인스턴스를 만들면서 즉시 프로퍼티 중 일부를 초기화 해야하는 경우 유용
    println(alphabetApply())
}

fun alphabetWith(): String = with(StringBuilder()) {    // 메소드를 호출하려는 수신 객체를 지정
    for (letter in 'A'..'Z') {
        this.append(letter) // this를 명시해서 앞에서 지정한 수신 객체의 메소드 호출
    }
    append("\nNow I know the alphabet!")    // this 생략하고 메소드 호출
    toString()// 람다에서 값 반환
}

fun alphabetApply(): String = StringBuilder().apply {
    for (letter in 'A'..'Z') {
        this.append(letter)
    }
    append("\nNow I know the alphabet!")
}.toString()