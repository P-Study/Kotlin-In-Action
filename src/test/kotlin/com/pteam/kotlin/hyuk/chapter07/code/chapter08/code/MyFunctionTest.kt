package com.pteam.kotlin.hyuk.chapter07.code.chapter08.code

import com.pteam.kotlin.hyuk.chapter08.code.getOdds
import com.pteam.kotlin.hyuk.chapter08.code.getOddsUsingSequence
import com.pteam.kotlin.hyuk.chapter08.code.myInlineFunction
import com.pteam.kotlin.hyuk.chapter08.code.myNormalFunction
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlin.system.measureNanoTime

class MyFunctionTest : StringSpec({
    "performance test `inline vs normal function`" {
        val executionTimeOfInlineFunction = measureNanoTime {
            myInlineFunction { arg ->
                for (i in 0..arg) {
                    /* noting */
                }
            }
        }

        val executionTimeOfNormalFunction = measureNanoTime {
            myNormalFunction { arg ->
                for (i in 0..arg) {
                    /* nothing */
                }
            }
        }

        println("executionTimeOfInlineFunction : $executionTimeOfInlineFunction")
        println("executionTimeOfNormalFunction : $executionTimeOfNormalFunction")
        (executionTimeOfInlineFunction < executionTimeOfNormalFunction) shouldBe true
    }

    "performance test `using sequence vs not using sequence`" {
        val executionTimeOfSequence = measureNanoTime {
            getOddsUsingSequence((1..100).toList())
        }

        val executionTImeOfNormal = measureNanoTime {
            getOdds((1..100).toList())
        }

        println("executionTimeOfSequence : $executionTimeOfSequence")
        println("executionTImeOfNormal : $executionTImeOfNormal")
        (executionTimeOfSequence > executionTImeOfNormal) shouldBe true
    }
})