package com.pteam.kotlin.kade.chapter9.code

class Chapter912GenericClass {

    interface List<T> {
        operator fun get(index: Int): T
    }

    /**
     * 하위 클래스(StringList)에서 상위 클래스(List)에 정의된 함수를 오버라이드하거나 사용하려면
     * 타입 인자 T를 구체적 타입(String)으로 치환해야 한다
     */
    class StringList: List<String> {
        override fun get(index: Int): String {
            TODO("Not yet implemented")
        }
    }

    /**
     * 주의 : 상위 클래스(List<T>) 의 T와 하위 클래스(ArrayList<T>)의 T는 다른 타입이다.
     */
    class ArrayList<R> : List<R> {
        override fun get(index: Int): R {
            TODO("Not yet implemented")
        }
    }

    /**
     * 자기 자신을 타입 인자로 참조하는 경우
     */
    interface Comparable<T> {
        fun compareTo(other: T) : Int
    }

    /**
     * Generic Interface를 구현하면서 인터페이스의 타입 T를 Self 자신으로 지정했다
     */
    class Self : Comparable<Self> {
        override fun compareTo(other: Self): Int {
            TODO("Not yet implemented")
        }
    }
}