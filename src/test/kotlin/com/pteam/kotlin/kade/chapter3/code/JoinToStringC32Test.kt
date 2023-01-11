package com.pteam.kotlin.kade.chapter3.code

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class JoinToStringC32Test {

    @Test
    fun callDefaultFunction() {
        // given
        val list = listOf(1, 2, 3)

        // when
        val joinToString = joinToString(list, "; ", "(", ")")

        // then
        assertEquals("(1; 2; 3)", joinToString)
    }

    @Test
    fun callAddedArgsNameFunction() {
        // given
        val list = listOf(1, 2, 3)

        // when
        val joinToString = joinToString(list, separator = "; ", prefix = "(", postfix =  ")")

        // then
        assertEquals("(1; 2; 3)", joinToString)
    }

    @Test
    fun callAddedDefaultValueFunction() {
        // given
        val list = listOf(1, 2, 3)

        // when
        val joinToString = joinToString(list, ", ", "", "")

        // then
        assertEquals("1, 2, 3", joinToString)
    }

    @Test
    fun callAddedDefaultValueAndOmmitedAllArgsFunction() {
        // given
        val list = listOf(1, 2, 3)

        // when
        val joinToString = joinToString(list)

        // then
        assertEquals("1, 2, 3", joinToString)
    }

    @Test
    fun callAddedDefaultValueAndAddedSeparatorArgsFunction() {
        // given
        val list = listOf(1, 2, 3)

        // when
        val joinToString = joinToString(list, "; ")

        // then
        assertEquals("1; 2; 3", joinToString)
    }
}