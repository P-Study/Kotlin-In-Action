package com.pteam.kotlin.juyoung.chapter03.code

fun main() {
    println("12.345-6.A".split("\\.|-".toRegex()))  // 명시적 정규식
    println("12.345-6.A".split(".", "-"))  // 여러 구분 문자열 지정

    parsePath("/Users/we/myProject/Kotlin-In-Action/README.md")
    parsePathRegex("/Users/we/myProject/Kotlin-In-Action/README.md")
}

fun parsePath(path: String) {
    // String 확장 함수를 이용
    val directory = path.substringBeforeLast("/")
    val fullName = path.substringAfterLast("/")

    val fileName = fullName.substringBeforeLast(".")
    val extension = fullName.substringAfterLast(".")

    println("Dir: $directory, name: $fileName, ext: $extension")
}

fun parsePathRegex(path: String) {
//    val regex = "(.+)/(.+)\\.(.+)".toRegex()
    val regex = """(.+)/(.+)\.(.+)""".toRegex()     // 3중 따옴표를 사용하면 이스케이프할 필요 없음
    val matchResult = regex.matchEntire(path)

    if(matchResult != null) {
        val (directory, fileName, extension) = matchResult.destructured
        println("Dir: $directory, name: $fileName, ext: $extension")
    }
}