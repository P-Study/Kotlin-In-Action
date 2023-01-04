package com.pteam.kotlin.juyoung.chapter02.code

import com.pteam.kotlin.juyoung.chapter02.code.Color2.*
import java.lang.Exception

fun mix(c1: Color2, c2: Color2) =
    when (setOf(c1, c2)) {
        setOf(YELLOW, BLUE) -> GREEN
        else -> throw Exception("Dirty Color")
    }