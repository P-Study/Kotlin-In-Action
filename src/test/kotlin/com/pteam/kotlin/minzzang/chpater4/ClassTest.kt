package com.pteam.kotlin.minzzang.chpater4

import com.pteam.kotlin.minzznag.chapter4.Clickable
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

internal class ClassTest : StringSpec({

    "override된 메소드나 프로퍼티에 상속을 제한하려면 final을 붙혀야 한다" {
        open class RichButton : Clickable {
            final override fun showOff() = super.showOff()
            override fun showOn() = "show on"
        }

        class SuperRichButton : RichButton() {
            override fun showOn() = "디폴트는 열려 있다"
        }
    }

    "코틀린에서는 자바와 다르게 중첩 클래스가 바깥 클래스에 대한 참조를 저장하지 않는다" {
        class Outer {
            inner class Inner {
                fun getOuterReference() = this@Outer
            }
        }

        val outer: Outer.Inner = Outer().Inner()
        outer should {
            it.getOuterReference().javaClass == Outer::class.java
        }
    }

    "sealed 클래스에 속한 값에 디폴트 분기를 사용하지 않고 when을 사용하면 컴파일 타임에 예외를 잡을 수 있다" {
        val fruit: Fruit = Apple(2000)

        when (fruit) {
            is Apple -> println("apple")
            is Banana -> println("banana")
        }
    }

    "주 생성자는 생성자 파라미터를 지정하고 프로퍼티를 정의한다" {
        class User(val nickname: String)
        class User2 constructor(nickname: String) {
            val nickname = nickname
        }
        class User3 constructor(nickname: String) {
            val nickname: String

            init {
                this.nickname = nickname
            }
        }
    }

    "setter를 통해 뒷받침하는 필드에 접근할 수 있다" {
        class User(val name: String) {
            var address = "서울"
                set(inputAddress) {
                    println("""
                        Address was changed for ${name}:
                        [${field}] -> [${inputAddress}]
                    """.trimIndent())
                }
        }

        User("민제").address = "성남"
    }

    "data class는 toString, equals, hashcode를 자동으로 재정의한다" {
        data class Client(val name: String)
        class Server(val name: String)

        Client("client").toString() shouldBe "Client(name=client)"
        Server("server").toString() shouldNotBe "Server(name=server)"
    }

    "copy 메소드를 사용하면 불변 객체에 일부 프로퍼티를 변경한 복사본을 만들 수 있다" {
        data class Client(val name: String)

        val client = Client("client")
        val copy = client.copy(name = "updatedClient")

        client.toString() shouldBe "Client(name=client)"
        copy.toString() shouldBe "Client(name=updatedClient)"
    }

    "상속을 허용하지 않는 클래스에 클래스 위임을 해 새로운 기능을 추가할 수 있다" {
        class Original(val name: String) : Special {
            override fun getSpecialName() = "${name}!!"
        }

        class Decorator(val original: Original = Original("민제")) : Special by original

        val decorator = Decorator(Original("민제"))
        decorator.getSpecialName() shouldBe "민제!!"
    }

    "object 키워드를 사용하면 싱글턴 객체를 쉽게 만들 수 있다" {
        Controller.get("Notice") shouldBe "getNotice"
    }

    "companion object로 팩토리 메소드를 만들 수 있다" {
        class User private constructor(val name: String) {
//            companion object {
//                fun newSubscriberUser(email: String) = User(email.substringBefore(('@')))
//                fun newFacebookUser(accountId: Int) = User("facebook")
//            }
        }
    }

    "compaion object는 일반 객체 처럼 사용할 수 있다" {
        class Person (val name: String) {
//            companion object Loader : Special {
//                override fun getSpecialName() = "special"
//            }
        }
    }
})

sealed class Fruit
class Apple(val price: Int) : Fruit()
class Banana(val price: Int) : Fruit()
//class Grape(val price: Int) : Fruit()

interface Special {
    fun getSpecialName(): String
}

object Controller {
    fun get(entity: String) = "get${entity}"
}

class Person (val name: String) {
    companion object Loader : Special {
        override fun getSpecialName() = "special"
    }
}
