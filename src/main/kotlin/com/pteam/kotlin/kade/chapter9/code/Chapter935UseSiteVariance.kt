package com.pteam.kotlin.kade.chapter9.code

class Chapter935UseSiteVariance {

    /**
     * item은 destincation에 저장되기 때문에 하위 타입이어야 한다
     */
    fun <T : R, R> copyData(source : MutableList<T> ,
                            destlnation: MutableList<R>) {
        for (item in source) {
            destlnation.add(item)
        }
    }

    /**
     * out키워드를 통해 타입 프로젝션을 한다(source가 in에 사용될 수 없도록 제한)
     */
    fun <T> copyData2(source : MutableList<out T> ,
                            destlnation: MutableList<T>) {
        for (item in source) {
            destlnation.add(item)
        }
    }

    /**
     * in 프로젝션 타입을 사용하여 상위 타입으로 대치할 수 있다.
     */
    fun <T> copyData3(source : MutableList<T> ,
                      destlnation: MutableList<in T>) {
        for (item in source) {
            destlnation.add(item)
        }
    }
}