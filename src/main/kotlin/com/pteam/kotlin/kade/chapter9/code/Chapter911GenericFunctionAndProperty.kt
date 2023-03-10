package com.pteam.kotlin.kade.chapter9.code

class Chapter911GenericFunctionAndProperty {

    fun callGeneric() {
        val letters = ('a'..'z').toList()
        println(letters.slice<Char>(0..2))
        println(letters.slice(10..13))
    }

    /**
     * 컴파일러는 filter가 List<T> 타입의 리스트에 대해 호출될 수 있다는 사실과 filter의 수신 객체인 reader의 타입이 List<String>이라는 사실을 알고 T가 String이라는 사실을 추론한다.
     */
    fun callGenericHigherOrder() {
        val authors = listOf("Dmitry", "Svelana")
        val readers = mutableListOf<String>("")
        readers.filter { it !in authors}
    }

    /**
     * 확장 프로퍼티도 제너릭으로 선언 가능하다
     *
     * 주의 : 확장 프로퍼티가 아닌 일반 프로퍼티는 제너릭하게 만들 수 없다
     */
    val <T> List<T>.penultimate: T
        get() = this[size - 2]
}