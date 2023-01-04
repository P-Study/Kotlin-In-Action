package com.pteam.kotlin.juyoung.chapter02.code

class Person (
    val name: String,   // 읽기 전용 프로퍼티로, 필드(비공개)와 필드를 읽는 단순한 getter(공개)를 만든다
    var isMarried: Boolean  // 쓸 수 있는 프로퍼티로, 필드(비공개), getter(공개), setter(공개)를 만든다
)

fun main() {
    val person = Person("Bob", true)
    println(person.name)
    println(person.isMarried)
}