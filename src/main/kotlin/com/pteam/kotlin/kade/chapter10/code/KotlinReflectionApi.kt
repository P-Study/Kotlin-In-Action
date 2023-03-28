package com.pteam.kotlin.kade.chapter10.code

import kotlin.reflect.KFunction
import kotlin.reflect.KFunction2

class KotlinReflectionApi {

    class Person(val name: String, val age: Int)
    fun printKotlinReflectionApi() {
        val person = Person("Yoo", 32)
        val kClass = person.javaClass.kotlin
        println(kClass.simpleName)
        kClass.members.forEach { println(it.name) }
    }

    fun foo(x: Int) = println(x)
    fun printFoo() {
        val kFunction = ::foo
        kFunction.call(42)
    }

    fun sum(x: Int, y: Int) = x + y
    fun printSum() {
        val kFunction: KFunction2<Int, Int, Int> = ::sum
        println(kFunction.invoke(1, 2) + kFunction(3, 4))
    }

    var counter = 0
    fun printProperty() {
        val kProperty = ::counter
        kProperty.setter.call(21)
        println(kProperty.get())
    }

    fun printProperty2() {
        val person = Person("Yoo", 32)
        val memberProperty = Person::age
        println(memberProperty.get(person))
    }
}