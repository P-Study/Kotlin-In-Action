package com.pteam.kotlin.minzznag.chapter3

val BLANK = " "
const val EMPTY = ""
private const val COMMA = ", "

fun <T> joinToString(
    collection: Collection<T>,
    separator: String = COMMA,
    prefix: String = EMPTY,
    postfix: String = EMPTY
): String {
    val result = StringBuilder(prefix)

    for ((index, element) in collection.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(element)
    }

    result.append(postfix)
    return result.toString()
}

fun <T> Collection<T>.join(
    separator: String = COMMA,
    prefix: String = EMPTY,
    postfix: String = EMPTY
): String {
    var result = StringBuilder(prefix)

    for ((index, element) in this.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(element)
    }

    result.append(postfix)
    return result.toString()
}
