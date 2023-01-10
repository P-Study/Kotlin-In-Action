package com.pteam.kotlin.kade.chapter3.code

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ExtensionC33Test {

    @Test
    fun callStrinLastChar() {
        // given
        val str = "Str"

        // when
        val lastChar = str.lastChar()

        // then
        assertEquals('r', lastChar)
    }

    @Test
    fun callListJoinToString() {
        // given
        val list = listOf(1, 2, 3)

        // when
        val joinToString = list.joinToString("; ", "(", ")")

        // then
        assertEquals("(1; 2; 3)", joinToString)
    }

    @Test
    fun callStrListJoinToString() {
        // given
        val list = listOf("1", "2", "3")

        // when
        val joinToString = list.join("; ", "(", ")")

        // then
        assertEquals("(1; 2; 3)", joinToString)
    }

    @Test
    fun callOverrideFailMethod() {
        val buttonView: View = Button()
        buttonView.click()
    }

    @Test
    fun callExtensionProperty() {
        // given
        val sb = StringBuilder("갓tlin?")

        // when
        sb.lastChar = '!'

        // then
        assertEquals("갓tlin!" , sb.toString())
    }
}