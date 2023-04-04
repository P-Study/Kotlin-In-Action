package com.pteam.kotlin.hyuk.appendix.code

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class GeneratorBuilderTest : DescribeSpec() {
    init {
        it("generator builder") {
            val gen = idMaker()

            gen.next(Unit) shouldBe 0
            gen.next(Unit) shouldBe 1
            gen.next(Unit) shouldBe 2
            gen.next(Unit) shouldBe null
        }
    }

    fun idMaker() = generate<Int, Unit> {
        var index = 0
        while (index < 3)
            yield(index++)
    }
}