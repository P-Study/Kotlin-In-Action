package com.pteam.kotlin.hyuk.chapter03.code

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.startWith

class HyukExtKtTest : StringSpec({
    "행운아" {
        val list = listOf(7, 7, 7, 7, 7)

        val result = list.isLuckyList()

        result shouldBe  "I'm lucky"
    }

    "불행아" {
        val list = listOf(4, 4, 7, 7)

        val result = list.isLuckyList()

        result shouldBe "I'm unlucky"
    }

    "룰 이해 못함" {
        val wrongList = listOf(1, 2)

        val exception = shouldThrowExactly<IllegalStateException> { wrongList.isLuckyList() }
        exception.message should startWith("too small list. list's size has to be more than 3!")
    }
})