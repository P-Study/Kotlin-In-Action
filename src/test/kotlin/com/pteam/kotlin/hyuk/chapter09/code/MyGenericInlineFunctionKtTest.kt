package com.pteam.kotlin.hyuk.chapter09.code

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

internal class MyGenericInlineFunctionKtTest : DescribeSpec({
    describe("generic inline function") {
        it("isA function return true") {
            isA<String>("abc") shouldBe true
        }
        it("isA function return false") {
            isA<String>(100) shouldBe false
        }
        it("using filterInInstance") {
            val items = listOf("Hello", "Kotlin", 100, 10L, false)

            val filteredItems = items.filterIsInstance<String>()

            filteredItems shouldHaveSize 2
            filteredItems shouldContainInOrder (listOf("Hello", "Kotlin"))
        }
    }
})