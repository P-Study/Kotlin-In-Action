package com.pteam.kotlin.chan.chapter02

fun main() {

    val chan = Person("leeheechan", 27)
    val hong = Person("honggildong", 321)

    chan.introduce()
    hong.introduce()

    if (chan.isAdult) {
        println("히차이 으른이네~")
    }

}
