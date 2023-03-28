package com.pteam.kotlin.kade.chapter10.code

import kotlin.reflect.KAnnotatedElement

class KotlinAnnotationSerialize {

    inline fun <reified T> KAnnotatedElement.findAnnotation() : T? = annotations.filterIsInstance<T>().firstOrNull()
}