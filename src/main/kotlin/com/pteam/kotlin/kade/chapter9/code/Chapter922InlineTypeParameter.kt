package com.pteam.kotlin.kade.chapter9.code

class Chapter922InlineTypeParameter {

    /**
     * 타입 파라미터를 실체화하였다
     */
    inline fun <reified T> isA(value: Any) = value is T
    fun callIsA() {
        println(isA<String>("abc"))
        println(isA<String>(123))
    }

    /**
     * 실체화한 파라미터를 활용하는 라이브러리
     */
    fun callFilterInstance() {
        val items = listOf("one", 2, "three")
        println(items.filterIsInstance<String>())

//        public inline fun <reified R> Iterable<*>.filterIsInstance(): List<R> {
//            for (element in this) {
//                if (element is T) {
//                    destination.add(element)
//                }
//            }
//        }
    }
}