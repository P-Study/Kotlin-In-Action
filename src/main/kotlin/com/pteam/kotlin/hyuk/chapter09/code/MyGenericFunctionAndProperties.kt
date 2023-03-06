package com.pteam.kotlin.hyuk.chapter09.code

fun <T> List<T>.mySlice(indices: IntRange) = this.slice(indices)

fun <T> List<T>.myFilter(predicate: (T) -> Boolean) = this.filter(predicate)

val <T> List<T>.penultimate: T
    get() = this[size - 2]

fun calculateSumParamWithStarProjectionCollection(c: Collection<*>): Int {
    val intList = c as? List<Int>
        ?: throw IllegalArgumentException("List is expected")

    return intList.sum()
}

fun calculateSumParamWithSpecificTypeParameterCollection(c: Collection<Int>): Int {
    if (c is List<Int>) {
        return c.sum()
    } else {
        throw IllegalArgumentException("List<Int> is expected")
    }
}