package com.pteam.kotlin.hyuk.chapter05.code

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class CollectionApiPerformanceTest() : StringSpec() {
    init {
        "컬렉션 API 비효율적으로 사용" {
            val (ages, maxAge) = createAges()

            val result = ages.filter { it == ages.maxBy { age -> age } }
            result[0] shouldBe maxAge
        }

        "컬렉션 API 효율적으로 개선" {
            val (ages, maxAge) = createAges()

            val expectedMaxAge = ages.maxBy { age -> age }
            val result = ages.filter { it == expectedMaxAge }

            result[0] shouldBe maxAge
        }
    }

    private fun createAges(): Pair<MutableList<Int>, Int> {
        val ages = mutableListOf<Int>()
        val maxAge = 10000

        for (age in 1..maxAge) {
            ages.add(age)
        }
        return Pair(ages, maxAge)
    }
}