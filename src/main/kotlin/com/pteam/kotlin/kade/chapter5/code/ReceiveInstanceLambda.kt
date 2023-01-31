package com.pteam.kotlin.kade.chapter5.code

class ReceiveInstanceLambda {

    fun alphabet(): String {
        val stringBuilder = StringBuilder()
        for (letter in 'A'..'Z') {
            stringBuilder.append(letter)
        }
        stringBuilder.append("\n알파베지터미네이터키아이스크림창정")
        return stringBuilder.toString()
    }
    fun firstAlphabetForWithFunction(): String {
        val stringBuilder = StringBuilder()
        return with(stringBuilder) {
            for (letter in 'A'..'Z') {
                this.append(letter)
            }
            append("\n알파베지터미네이터키아이스크림창정")
            toString()
        }
    }
    fun secondAlphabetForWithFunction() = with(StringBuilder()) {
        for (letter in 'A'..'Z') {
            append(letter)
        }
        append("\n알파베지터미네이터키아이스크림창정")
        toString()
    }
    fun firstAlphabetForApplyFunction() = StringBuilder().apply {
        for (letter in 'A'..'Z') {
            append(letter)
        }
        append("\n알파베지터미네이터키아이스크림창정")
    }.toString()

    fun secondAlphabetForApplyFunction() = buildString {
        for (letter in 'A'..'Z') {
            append(letter)
        }
        append("\n알파베지터미네이터키아이스크림창정")
    }
}