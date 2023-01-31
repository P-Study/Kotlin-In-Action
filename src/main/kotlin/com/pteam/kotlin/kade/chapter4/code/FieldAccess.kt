package com.pteam.kotlin.kade.chapter4.code

// Type mismatch: inferred type is () -> Unit but Unit was expected
class Usar(val name: String) {
    var address: String = "unspecified"
        set(value: String) {
            println("""
                Address was changed for $name:
                "$field" -> "$value".""".trimIndent())
            field=value
        }
}