package com.pteam.kotlin.hyuk.chapter09.code

open class MyGenericClass<T>(val value: T)

class MyStringClass(value: String = "") : MyGenericClass<String>(value)

class MyIntClass(value: Int = 0) : MyGenericClass<Int>(value)