package com.pteam.kotlin.hyuk.chapter06.code

class PrintProcessor : MyJavaInterface {
    override fun process(contents: List<String?>) {
        contents.forEach { content ->
            println("content : ${content ?: "null"}")
        }
    }
}

class SizeUpAndPrintProcessor : MyJavaInterface {
    override fun process(contents: MutableList<String>) {
        contents.addAll(contents.toList())

        contents.forEach { content ->
            println("content : $content")
        }
    }
}
