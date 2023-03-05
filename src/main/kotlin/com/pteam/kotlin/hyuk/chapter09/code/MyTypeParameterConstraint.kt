package com.pteam.kotlin.hyuk.chapter09.code

fun <T : Int> List<T>.mySum() = this.sum()

fun <T> ensureTrailingPeriod(seq: T) where T : CharSequence, T : Appendable {
    if (!seq.endsWith('.')) {
        seq.append('.')
    }
}

class MyProcessor<T: Any> {
    fun process(value: T) {
        value.hashCode()
    }
}