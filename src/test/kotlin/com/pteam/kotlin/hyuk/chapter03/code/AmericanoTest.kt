package com.pteam.kotlin.hyuk.chapter03.code

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class AmericanoTest : ShouldSpec({
    context("make americano") {
        should("create success") {
            val iceAmericano = Americano.IceAmericano(10, 30, 20)

            iceAmericano shouldNotBe null
        }

        should("create fail") {
            val exception = shouldThrowExactly<IllegalArgumentException> { Americano.IceAmericano(0, 0, 0) }
            exception.message shouldBe "Wrong recipe"
        }

        xshould("ignore this test") {
            /* No operation */
        }
    }
})