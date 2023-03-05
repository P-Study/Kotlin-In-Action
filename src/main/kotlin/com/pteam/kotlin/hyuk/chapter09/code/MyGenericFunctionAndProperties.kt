package com.pteam.kotlin.hyuk.chapter09.code

fun <T> List<T>.mySlice(indices: IntRange) = this.slice(indices)

fun <T> List<T>.myFilter(predicate: (T) -> Boolean) = this.filter(predicate)

val <T> List<T>.penultimate: T
    get() = this[size - 2]