package com.pteam.kotlin.hyuk.chapter06.code

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

class KotlinCollectionTest : FunSpec({
    test("ReadOnly Collection Interface Reference Mutable List") {
        val mutableListOf = mutableListOf(1, 2, 3)
        val readOnlyList: List<Int> = mutableListOf

        (readOnlyList == mutableListOf) shouldBe true
        readOnlyList shouldHaveSize 3

        mutableListOf.add(4)

        readOnlyList shouldHaveSize 4
    }
})