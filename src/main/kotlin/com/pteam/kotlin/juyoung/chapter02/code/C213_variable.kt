package com.pteam.kotlin.juyoung.chapter02.code

fun main() {
    val answer = 33         // 타입 생략
    val answer2: Int = 33;  // 타입 지정

    // 부동소수점 상수를 사용한다면 변수 타입은 Double이 됨
    val yearsToCompute = 7.5e6

    // 초기화 식을 사용하지 않고 변수를 선언하려면 변수 타입을 반드시 명시해야 함
    val answer3: Int
    answer3 = 33

    /*
    기본적으로 val 키워드를 사용하고 꼭 필요할 때만 var로 변경할 것
    val: 변경 불가능한 참조를 저장하는 변수(java의 final 변수)
         정확히 한 번만 초기화돼야 함
         어떤 블록이 실행될 때 오직 한 초기화 문장만 실행됨을 컴파일러가 확인할 수 있다면 조건에 따라 여러 값으로 초기화 가능
    var: 변경 가능한 참조
     */
    val isTrue = true
    val message: String
    if(isTrue) {
        message = "true"
    }
    else {
        message = "false"
    }

    // val 참조 자체는 불변이지만 그 참조가 가리키는 객체의 내부 값은 변경될 수 있다==음
    val language = arrayListOf("Java")
    println(language)
    language.add("Kotlin")
    println(language)

    // var 키워드를 사용하면 변수의 값을 변경할 수 있지만 변수의 타입은 고정돼 바뀌지 않는다
    var num = 11
//    num = "string"    // 컴파일 오류 발생

}
