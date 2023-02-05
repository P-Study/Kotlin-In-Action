package com.pteam.kotlin.hyuk.chapter05.code

object MyLambdaFunction {
    fun myFunction(msg: String, lambda: (String) -> String) = lambda(msg)
}