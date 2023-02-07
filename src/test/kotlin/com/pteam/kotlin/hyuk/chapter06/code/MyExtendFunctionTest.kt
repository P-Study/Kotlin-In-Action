package com.pteam.kotlin.hyuk.chapter06.code

import io.kotest.core.spec.style.FunSpec
import io.kotest.data.headers
import io.kotest.data.row
import io.kotest.data.table
import io.kotest.matchers.shouldBe

class MyExtendFunctionTest : FunSpec({
    context("controller nullable type") {
        test("using extend function") {
            io.kotest.data.forAll(
                table(
                    headers("string", "expected result"),
                    row(null, true),
                    row("hello", false)
                )
            ) { str, expectedResult ->
                str.isNull() shouldBe expectedResult
            }
        }
    }
})