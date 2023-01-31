package com.pteam.kotlin.kade.chapter5.code

class SequenceCreation {

    val naturalNumbers = generateSequence(0) { it + 1 }
    val numbersTo100 = naturalNumbers.takeWhile { it <= 100 }
}