package com.pteam.kotlin.hyuk.chapter05.code

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveAtLeastSize
import io.kotest.matchers.sequences.shouldHaveAtLeastSize
import kotlin.random.Random

private const val MAX_SIZE = 100000

class SequencePerformanceTest() : StringSpec() {
    init {
        "중간 임시 컬렉션 때문에 성능 저하" {
            val randomNumbers = makeRandomNumbers(MAX_SIZE)

            val result = randomNumbers.map { it / 2 }.filter { it % 2 == 0 }

            result shouldHaveAtLeastSize 0
        }

        "시퀀스 이용해 최적화" {
            val randomNumbers = makeRandomNumbers(MAX_SIZE)

            val result = randomNumbers.asSequence()
                .map { it / 2 }
                .filter { it % 2 == 0 }

            result shouldHaveAtLeastSize 0
        }
    }

    private fun makeRandomNumbers(size: Int): List<Int> {
        val randomNumbers = mutableListOf<Int>()

        for (number in 0 until size)
            randomNumbers.add(Random.nextInt())

        return randomNumbers
    }
}
