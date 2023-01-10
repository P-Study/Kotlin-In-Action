package com.pteam.kotlin.kade.chapter3.code

fun printSplitRegex() {
    println("12.345-6.A-바".split("\\.|-".toRegex()))
}

fun printSplitStringArgs() {
    println("12.345-6.A-바".split(".", "-"))
}

fun parsePath(path: String) : String {
    val directory = path.substringBeforeLast("/")
    val fullName = path.substringAfterLast("/")
    val fileName = fullName.substringBeforeLast(".")
    val extension = fullName.substringAfterLast(".")
    return "$directory/$fileName.$extension"
}

fun parsePathForRegex(path: String) : String? {
    val regex = """(.+)/(.+)\.(.+)""".toRegex()
    val matchResult = regex.matchEntire(path)
    if (matchResult != null) {
        val (directory, fileName, extension) = matchResult.destructured
        return "$directory/$fileName.$extension"
    }

    return null
}

val kotlinVersion = """
     |   //
    .|  //
    .|  /\
""".trimIndent()