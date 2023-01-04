package com.pteam.kotlin.juyoung.chapter02.code

interface Expr
class Num(val value: Int) : Expr    // value라는 프로퍼티만 존재하는 단순한 클래스로 Expr을 구현
class Sum(val left: Expr, val right: Expr) : Expr   // Expr 타입의 객체라면 어떤 것이나 Sum 연산의 인자가 될 수 있음, Num이나 다른 Sum이 인자로 올 수 있다


fun eval(e: Expr): Int {
    if(e is Num) {
        val n = e as Num
        return n.value
    }
    if(e is Sum) {
        return eval(e.right) + eval(e.left) // e에 스마트 캐스트 표시
    }
    throw IllegalArgumentException("Unknown expression")
}

fun eval2(e: Expr): Int =
    when (e) {
        is Num -> {
            println("num: ${e.value}")
            e.value
        }
        is Sum ->
            eval2(e.right) + eval2(e.left)
        else ->
            throw IllegalArgumentException("Unknown expression")
    }


fun main() {
    println(eval(Sum(Sum(Num(1), Num(2)), Num(4))))
    println(eval2(Sum(Sum(Num(1), Num(2)), Num(4))))
}