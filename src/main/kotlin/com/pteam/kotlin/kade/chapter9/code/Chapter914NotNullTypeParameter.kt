package com.pteam.kotlin.kade.chapter9.code

class Chapter914NotNullTypeParameter {

    class Processor<T> {    // null이 가능한 경우
    // class Processor<T : Any> {    // null이 불가능한 경우

        /**
         * T는 null이 가능하므로 안전한 호출을 사용해야 한다
         */
        fun process(value: T) {
            value?.hashCode()
        }
    }

    fun call() {
        // null이 가능한 경우 String?이 타입 인자로 들어간다
        // null이 불가능한 경우 Compile Error를 발생시킨다
        val nullableStringProcessor = Processor<String?>()
        nullableStringProcessor.process(null)
    }
}