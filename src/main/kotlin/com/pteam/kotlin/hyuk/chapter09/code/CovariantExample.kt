package com.pteam.kotlin.hyuk.chapter09.code

import javax.print.attribute.standard.Destination

open class Animal {
    fun feed() {
        println("feed to animal")
    }
}

class Cat(val name: String) : Animal() {
    fun cleanLitter() { println("clean Litter ")}
}

class InvariantHerd<T : Animal>(private val animals: List<T>) {
    val size: Int
        get() = animals.size

    operator fun get(i: Int): T {
        return animals[i]
    }
}

fun feedToInvariantHerd(animals: InvariantHerd<Animal>) {
    for (i in 0 until animals.size) {
        animals[i].feed()
    }
}

class CovariantHerd<out T : Animal>(private val animals: List<T>) {
    val size: Int
        get() = animals.size

    operator fun get(i: Int): T {
        return animals[i]
    }
}

fun feedToCovariantHerd(animals: CovariantHerd<Animal>) {
    for (i in 0 until animals.size) {
        animals[i].feed()
    }
}

fun compareSomething(comparable: Comparable<String>) {
    println("compare something")
}

fun <T: R, R> copyDataWithoutVariance(
    source: MutableList<T>,
    destination: MutableList<R>
) {
    for (item in source) {
        destination.add(item)
    }
}

fun <T> copyDataWithCovariant(
    source: MutableList<out T>,
    destination: MutableList<T>
) {
    for (item in source) {
        destination.add(item)
    }
}

fun <T> copyDataWithContravariance(
    source: MutableList<T>,
    destination: MutableList<in T>
) {
    for (item in source) {
        destination.add(item)
    }
}
