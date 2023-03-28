package com.pteam.kotlin.kade.chapter10.code

import kotlin.annotation.AnnotationTarget

class MetaAnnotation {

    @Target(AnnotationTarget.ANNOTATION_CLASS)
    @Retention
    annotation class MetaAnnotationDeclaration
}