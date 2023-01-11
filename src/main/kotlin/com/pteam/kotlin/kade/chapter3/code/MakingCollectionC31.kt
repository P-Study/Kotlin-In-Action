package com.pteam.kotlin.kade.chapter3.code

fun printSet() {
    val set = hashSetOf(1, 7, 53)
    set.forEach { println(it) }
}

fun printList() {
    val list = arrayListOf(1, 7, 53)
    list.forEach { println(it) }
}

fun printMap() {
    val map = hashMapOf(1 to "one", 7 to "seven", 53 to "fifty-three")
    map.forEach { println(it) }
}