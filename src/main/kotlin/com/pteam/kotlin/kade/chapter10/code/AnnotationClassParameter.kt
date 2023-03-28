package com.pteam.kotlin.kade.chapter10.code

import kotlin.reflect.KClass

class AnnotationClassParameter {

    annotation class ClassAnnotation(val targetClass: KClass<out Any>)
}