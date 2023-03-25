package com.pteam.kotlin.minzzang.chapter10

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

internal class ReflectionTest : StringSpec() {
    @get:Role
    val folderName = "name"
    @Test
    fun `사용 지점 대상 선언으로 애노테이션을 붙일 요소를 정할 수 있다`() {
    }

    init {
        """ 
           @JvmName : 코틀린 선언이 만들어내는 자바 필드나 메소드 이름을 변경한다.
           @JvmStatic : 메소드, 객체 선언, 동반 객체에 적용하면 그 요소가 자바 정적 메소드로 노출된다.
           @JvmOverloads : 디폴트 파라미터 값이 있는 함수에 대해 자동으로 오버로딩한 함수를 생성해준다.
           @JvmField : 프로퍼티에 사용하면 게터나 세터가 없는 public java 필드로 프로퍼티를 노출시킨다.
        """

        """
            // kotlin
            annotation class JsonName(val name: String)
            
            // java
            public @interface JsonName {
                String value();
            }
            
            자바에서 선언한 애노테이션을 코틀린의 구성 요소에 적용할 때는
            value는 인자를 붙히지 않아도 된다.
        """

        """
            어노테이션 클래스에 붙힐 수 있는 애노테이션을
            매타 에노테이션이라고 한당
            
            @Target(AnnotationTarget .ANNOTATION_CLASS)
            annotation class BindingAnnotation
        """

        """
            자바 애노테이션 : .class 파일
            코틀린 애노테이션 : 런타임
        """

        "클래스 참조를 파라미터로 하는 애노테이션 클래스를 선언하면 클래스를 선언 메타데이터로 참조할 수 있다" {
            data class CompanyImpl(override val name: String) : Company

            data class Person(
                val name: String,
                @DeserializeInterface(CompanyImpl::class)
                val company: Company
            )
        }

        """
            클래스를 애노테이션 인자로 받기 : KClass<out 클래스명>
            제네릭 클래스를 인자로 받기 : KClass<out 클래스명<*>>
        """

        """
            코틀린에서 리플렉션 사용하는 방법
            
            1. java.lang.reflect 패키지에서 제공하는 표준 리플렉션 
            2. kotlin.reflect 패캐지에서 제공하는 코틀린 리플렉션
            
            코틀린 리플렉션은 기능이 많지 않아서 자바 리플렉션과 함께 사용
            코틀린 리플렉션은 코틀린 뿐만 아니라 jvm 언어에서 생성한 바이트 코드도 다룰 수 있다.
            
            코틀린은 리플렉션 api는 별도의 jar 파일에 담겨 제공된다.
        """

        "KClass를 사용해서 클래스 정보(상위 클래스, 모든 선언)를 알 수 있다" {
            class Person(val name: String, val age: Int)

            val person = Person("민제", 30)
            val kClass = person.javaClass.kotlin

            kClass.simpleName shouldBe "Person"
            kClass.members.forEach {
                println(it.name)
            }
        }

        "최상위 수준이나 클래스 안에 정의된 프로퍼티만 리플렉션으로 접근할 수 있다" {
            val x = "mj"
//          ::x  아직 지원하지 않는다~
        }

        """
            리플렉션 인터페이스 계층 구조
            
            root : KAnnotatedElement
                  |        |        |   
                KClass KCallable KParameter
                           |
                  KFunction KProperty                      
        """

        "findAnnotation을 이용해서 특정 프로퍼티에 적용된 어노테이션을 알 수 있다" {
            class Person(
                @Rule
                val name: String,
                val age : Int
            )

            val kClass = Person("민제", 30).javaClass.kotlin
            val filter = kClass.memberProperties.filter {
                it.findAnnotation<Rule>() == null
            }

            filter.size shouldBe 1
            filter[0].name shouldBe "age"
        }

        "클래스와 객체는 모두 KClass로 표현되는데 클래스는 crateInstance를 따로 호출해야 한다" {
            val objectInstance = Person::class.objectInstance

            val stringInstance = String::class.objectInstance

            val createInstance = String::class.createInstance()

            objectInstance shouldNotBe null
            stringInstance shouldBe null
            createInstance shouldNotBe null
        }

        "callBy를 사용하면 디폴트 파라미터 값을 활용할 수 있다" {
            class Person(
                val name: String,
                val age : Int = 30
            )

            val kClass = Person::class
            val constructors = kClass.primaryConstructor!!

            val parameter = mapOf(
                constructors.parameters.find {
                    it.name == "name"
                }!! to "민제"
            )

            val callBy = constructors.callBy(parameter)
            callBy.name shouldBe "민제"
            callBy.age shouldBe 30
        }

        "let 테스트" {
            val listOf = listOf("1", "2")
            listOf.isNotEmpty().apply {
                if (this) {

                }
            }
        }
    }
}

@Target(AnnotationTarget.PROPERTY)
annotation class Rule

@Target(AnnotationTarget.PROPERTY_GETTER)
annotation class Role
annotation class JsonName(val name: String)

interface Company {
    val name: String
}

@Target(AnnotationTarget.PROPERTY)
internal annotation class DeserializeInterface(
    val targetClass: KClass<out Any>
)

object Person {
    val name = "mj"
}
