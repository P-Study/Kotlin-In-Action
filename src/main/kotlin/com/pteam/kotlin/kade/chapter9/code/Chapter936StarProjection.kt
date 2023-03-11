package com.pteam.kotlin.kade.chapter9.code

import java.lang.IllegalArgumentException
import java.util.*
import kotlin.reflect.KClass

class Chapter936StarProjection {

    /**
     * 모든 타입 인자를 받아들일 수 있는 *
     * 원소의 타입을 정확히 모른다는 사실
     *
     * <*>는 안전하게 Any? 타입의 원소를 꺼내올 수는 있지만 타입을 모르는 리스트에 원소를 마음대로 넣을 수는 없다.
     */
    fun test() {
        val list: MutableList<Any?> = mutableListOf('a', 1, "qwe")
        val chars = mutableListOf('a', 'b', 'c')
        val unknownElement: MutableList<*> = if (Random().nextBoolean()) list else chars
    }

    interface FieldValidator<in T> {
        fun validate(input: T): Boolean
    }

    object DefaultStringValidator : FieldValidator<String> {
        override fun validate(input: String) = input.isNotEmpty()
    }

    object DefaultIntValidator : FieldValidator<Int> {
        override fun validate(input: Int) = input >= 0
    }

    /**
     * String, Int 타입을 지정하여 선언해주었지만
     * 어떤 타입의 FieldValidator인지 정확히 알지 못한다.
     * 이유는 알수 없는 타입에 구체적인 타입의 값을 넘기면 안전하지 못하여 그렇다
     */
    fun validator() {
        val validators = mutableMapOf<KClass<*>, FieldValidator<*>>()
        validators[String::class] = DefaultStringValidator
        validators[Int::class] = DefaultIntValidator

        // 명시적인 캐스팅으로 정상 동작 가능
        val stringValidator = validators[String::class] as FieldValidator<String>
        stringValidator.validate("")

        // 타입을 잘못 지정했을 때
        val intValidator = validators[Int::class] as FieldValidator<String>
        stringValidator.validate("")
    }

    /**
     * 타입 안정성을 보장하기 위해 안전하지 못한 로직은 내부에 감추고
     * 컴파일러가 타입 인자가 일치하지 않는 경우를 등록하지 못하게 한다
     */
    object Validators {
        private val validators = mutableMapOf<KClass<*>, FieldValidator<*>>()

        fun <T: Any> registerValidator(kClass: KClass<T>, fieldValidator: FieldValidator<T>) {
            validators[kClass] = fieldValidator
        }

        @Suppress("UNCHECKED_CAST")
        operator fun <T: Any> get(kClass: KClass<T>): FieldValidator<T> =
            validators[kClass] as? FieldValidator<T>
                ?: throw IllegalArgumentException("No validator for ${kClass.simpleName}")
    }
}