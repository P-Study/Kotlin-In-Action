package com.pteam.kotlin.kade.chapter10.code

import com.pteam.kotlin.kade.chapter3.code.joinToString
import java.lang.StringBuilder

class KotlinReflectionSerialize {

    fun serialize(obj: Any): String = buildString { serializeObject(obj) }
}

private fun StringBuilder.serializeObject(x: Any) {
    val kClass = x.javaClass.kotlin
    val properties = kClass.members

//    properties.joinToString(this, prefix = "{", postfix = "}") { prop ->
//        serializeString(prop.name)
//        append(" : ")
//        serializePropertyValue(prop.get(x))
//    }
}

//private fun StringBuilder.serializeObject(obj: Any) {
//    obj.javaClass.kotlin.memberProperties
//        .filter { it.findAnnotation<JsonExclude>() == null }
//        .joinToString(this, prefix = "{", postfix = "}") {
//            serializeProperty(it, obj)
//        }
//}