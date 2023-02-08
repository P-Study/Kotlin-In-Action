package com.pteam.kotlin.juyoung.chapter04.code.c41

sealed class Expr {
    class Num(val value:Int) : Expr()
    class Sum(val left: Expr, val right: Expr) : Expr()
}

fun eval(e: Expr): Int =
    // when 식이 모든 하위 클래스를 검사하므로 else 분기가 없어도 됨
    when (e) {
        is Expr.Num -> e.value
        is Expr.Sum -> eval(e.right) + eval(e.left)
    }