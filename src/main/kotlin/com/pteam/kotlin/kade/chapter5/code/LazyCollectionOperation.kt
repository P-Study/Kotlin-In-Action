package com.pteam.kotlin.kade.chapter5.code

class LazyCollectionOperation {
    private val people = listOf(Person("Yoo", 22), Person("Lee", 21))

    fun firstExample() = people.map(Person::name).filter { it.startsWith("A") }
    fun secondExample() = people.asSequence().map(Person::name).filter { it.startsWith("A") }.toList()
    fun thirdExample() =
        listOf(1,2,3,4).asSequence()
        .map { print("map($it) "); it * it }
        .filter { print("filter($it) "); it % 2 == 0 }

    fun fourthExample() =
        listOf(1,2,3,4).asSequence()
            .map { print("map($it) "); it * it }
            .filter { print("filter($it) "); it % 2 == 0 }
            .toList()
    class Person(
        val name: String,
        val age: Int
    )
}