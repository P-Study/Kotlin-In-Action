package com.pteam.kotlin.kade.chapter3.code

import java.lang.StringBuilder

fun String.lastChar() : Char = this.get(this.length - 1)

fun <T> Collection<T>.joinToString(
    separator: String = ", "
    , prefix: String = ""
    , postfix: String = ""
) : String {
    val result = StringBuilder(prefix)

    for ((index, element) in this.withIndex()) {
        if (index > 0) {
            result.append(separator)
        }
        result.append(element)
    }

    result.append(postfix)
    return result.toString()
}

fun Collection<String>.join(
    separator: String = ", "
    , prefix: String = ""
    , postfix: String = ""
) : String = joinToString(separator, prefix, postfix)

open class View {
    open fun click() = println("View clicked")
    fun View.showOff() = println("I'm a view!")
}

class Button : View() {
    override fun click() = println("View clicked")
    fun Button.showOff() = println("I'm a button!")
}

val String.lastChar: Char
    get() = get(length - 1)

var StringBuilder.lastChar: Char
    get() = get(length - 1)
    set(value: Char) {
        this.setCharAt(length -1, value)
    }