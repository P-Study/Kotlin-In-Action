package com.pteam.kotlin.juyoung.chapter02.code

import java.io.BufferedReader
import java.io.StringReader

fun readNumber(reader: BufferedReader): Int? {
    return try {
        val line = reader.readLine()
        Integer.parseInt(line)
    }
    catch (e: NumberFormatException) {
        null
    }
    finally {
        reader.close()
    }
}

fun readNumber2(reader: BufferedReader) {
    val number = try {
        val line = reader.readLine()
        Integer.parseInt(line)
    }
    catch (e: NumberFormatException) {
//        return
        null
    }
    println(number)
}

fun main() {
    var reader = BufferedReader(StringReader("123"))
    println(readNumber(reader))
    reader = BufferedReader(StringReader("not a number"))
    readNumber2(reader)
}
