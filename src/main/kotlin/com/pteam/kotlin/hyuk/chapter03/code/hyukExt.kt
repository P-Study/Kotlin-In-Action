package com.pteam.kotlin.hyuk.chapter03.code

fun List<Int>.isLuckyList(): String {
    if (this.size <= 3) {
        throw IllegalStateException("too small list. list's size has to be more than 3!")
    }

    var count = 3;
    for (element in this) {
        if (element == 7) {
            count--;
        }
    }

    return if (count <= 0) "I'm lucky" else "I'm unlucky"
}