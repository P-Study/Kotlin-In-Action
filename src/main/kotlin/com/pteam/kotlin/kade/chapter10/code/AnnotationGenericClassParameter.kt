package com.pteam.kotlin.kade.chapter10.code

import kotlin.reflect.KClass

class AnnotationGenericClassParameter {

    annotation class GenericClassParameter(val target: KClass<out IndexedValue<*>>)
}