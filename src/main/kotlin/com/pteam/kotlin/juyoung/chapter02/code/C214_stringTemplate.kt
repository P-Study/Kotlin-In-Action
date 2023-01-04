package com.pteam.kotlin.juyoung.chapter02.code

fun main(args: Array<String>) {
    val name = if (args.isNotEmpty()) args[0] else "Kotlin"
    println("Hello, $name!")

    // 중괄호{}로 둘러싸서 문자열 템플릿 안에 넣을 수 있다
    if(args.isNotEmpty()) {
        println("Hello, ${args[0]}!")
    }

    println ("Hello, ${if (args.isNotEmpty()) args[0] else "someone"}'")
}