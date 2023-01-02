package com.pteam.kotlin.hyuk.chapter02.code

object Calculator {
    fun eval(e: Expr): Int = when (e) {
        is Num -> e.value
        is Multiply -> eval(e.right) * eval(e.left)
        else -> throw IllegalArgumentException("Unkown expression")
    }
}