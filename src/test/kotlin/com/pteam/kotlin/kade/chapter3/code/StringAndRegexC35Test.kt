package com.pteam.kotlin.kade.chapter3.code

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class StringAndRegexC35Test {

    @Test
    fun parsePath() {
        // given
        val path = "/Users/kade/uu/uk.uk"

        // when
        val parsePath = parsePath(path)

        // then
        assertEquals("/Users/kade/uu/uk.uk", parsePath)
    }

    @Test
    fun parsePathForRegex() {
        // given
        val path = "/Users/kade/uu/uk.uk"

        // when
        val parsePath = parsePathForRegex(path)

        // then
        assertEquals("/Users/kade/uu/uk.uk", parsePath)
    }
}