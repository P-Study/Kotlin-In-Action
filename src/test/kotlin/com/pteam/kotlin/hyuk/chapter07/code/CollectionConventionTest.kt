package com.pteam.kotlin.hyuk.chapter07.code

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class CollectionConventionTest : FunSpec({
    test("Collection + 연산자를 사용하면 새로운 Collection이 만들어진다.") {
        // given
        val firstOldList = arrayListOf(1, 2)
        val secondOldList = arrayListOf(3, 4)

        // when
        val newList = firstOldList + secondOldList

        // then
        (newList === firstOldList) shouldBe false
        (newList === secondOldList) shouldBe false
        newList.size shouldBe (firstOldList.size + secondOldList.size)
    }

    test("Collection += 연산자를 사용하면 기존 Collection에 원소가 추가된다.") {
        // given
        val firstList = mutableListOf(1, 2)
        val secondList = mutableListOf(3, 4)

        // when
        firstList += secondList

        firstList.size shouldBe 4
    }

    test("Immutable Collection은 += 연산자를 사용하면 새로운 Collection이 생성된다.") {
        // given
        var firstList = listOf(1, 2)
        val secondList = listOf(3, 4)

        val firstListRef = firstList

        // when
        firstList += secondList

        // then
        (firstList === firstListRef) shouldBe false
    }
})