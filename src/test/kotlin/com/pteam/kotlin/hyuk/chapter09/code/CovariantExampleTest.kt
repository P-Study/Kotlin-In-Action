package com.pteam.kotlin.hyuk.chapter09.code

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

class CovariantExampleTest : DescribeSpec({
    describe("invariant") {
        it("compile error because of invariant") {
            val cats = InvariantHerd<Cat>(listOf(Cat("one"), Cat("two")))

            for (i in 0 until cats.size) {
                cats[i].cleanLitter()

                // compile error because InvariantHerd<Animal> and InvariantHerd<Cat> are different type.
                // feedToInvariantHerd(cats)
            }
        }
    }

    describe("covariant") {
        it("success") {
            val cats = CovariantHerd<Cat>(listOf(Cat("one"), Cat("two")))

            for (i in 0 until cats.size) {
                cats[i].cleanLitter()

                // success because CovariantHerd<Cat> is subtype of CovariantHerd<Animal>
                feedToCovariantHerd(cats)
            }
        }
    }

    describe("contravariance") {
        it("test") {
            val stringComparator = object : Comparator<String> {
                override fun compare(o1: String?, o2: String?): Int {
                    return o1!!.length.compareTo(o2!!.length)
                }
            }
            val anyComparator = Comparator<Any> { o1, o2 -> o1.hashCode().compareTo(o2.hashCode()) }

            (String is Any) shouldBe true

            // ?? Why compile error -> Comparator interface isn't contravariance
            // compareSomething(anyComparator)

            val strings = listOf<String>("abc", "def")
            strings.sortedWith(anyComparator)
        }
    }

    describe("use-site variance") {
        it("without variance") {
            val ints = mutableListOf(1, 2, 3)
            val anyItems = mutableListOf<Any>()

            copyDataWithoutVariance(ints, anyItems)

            anyItems shouldHaveSize ints.size
        }

        it("with covariant") {
            val ints = mutableListOf(1, 2, 3)
            val anyItems = mutableListOf<Any>()

            copyDataWithCovariant(ints, anyItems)

            anyItems shouldHaveSize ints.size
        }

        it("with contravariance") {
            val ints = mutableListOf(1, 2, 3)
            val anyItems = mutableListOf<Any>()

            copyDataWithContravariance(ints, anyItems)

            anyItems shouldHaveSize ints.size
        }
    }
})
