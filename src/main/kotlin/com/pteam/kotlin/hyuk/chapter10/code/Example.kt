package com.pteam.kotlin.hyuk.chapter10.code

object MyDeprecatedClass {
    @Deprecated(message = "This method was deprecated in last release", replaceWith = ReplaceWith("newMethod"))
    fun deprecatedMethod() {
        println("I'm deprecated function!")
    }

    fun newMethod() {
        println("I'm not deprecated function")
    }
}

class Foo {
    @property:Deprecated("")
    var property1: Any? = null

    @field:SuppressWarnings
    var property2: Any? = null

    @get:Deprecated("")
    var property3: Any? = null

    @set:Deprecated("")
    var property4: Any? = null
}

data class Person(
    val name: String,
    val age: Int
)

fun fooFunction() {
    println("Hello")
}

fun hello() = "hello"