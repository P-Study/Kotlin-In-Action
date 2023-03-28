package com.pteam.kotlin.kade.chapter6.code

import io.kotest.core.spec.style.DescribeSpec
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class NullableKotlinTypeTest: DescribeSpec({
    describe("") {

    }
}) {

    @Test
    fun strLen() {
    }

    @Test
    fun strLenSafe() {
        val nullableKotlinType = NullableKotlinType()
        nullableKotlinType.strLenSafe(null)
    }
}