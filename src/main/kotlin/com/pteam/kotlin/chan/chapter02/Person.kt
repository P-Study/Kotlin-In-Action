package com.pteam.kotlin.chan.chapter02

data class Person(
    val name: String,
    val age: Int
) {
    fun introduce() {
        println("안녕하세요, 이름은 ${name}이고, ${age}살입니다.")
    }

    val isAdult: Boolean
        get() {
            return 20 <= age
        }
}
