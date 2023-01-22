# Chapter 04 클래스, 객체, 인터페이스

## 4.1 클래스 계층 정의

### 코틀린 인터페이스

코틀린 인터페이스는 자바 8 인터페이스와 비슷하다. 

```kotlin
interface Clickable {
    fun click()
}
```

하지만 구현 측면에서 재밌는 점이 있다. 코틀린에서는 *override 변경자*를 꼭 사용해야 한다.
이는 실수로 상위 클래스의 메소드를 오버라이드 하는 경우를 방지해준다.

```kotlin
class Button : Clickable {
    override fun click() = println("I was clicked")
}
```

### open, final, abstract 변경자: 기본적으로 final

자바 클래스는 기본적으로 상속을 허용한다. 이는 편리한 경우도 많지만 문제가 생기는 경우도 많다.

상속을 자유롭게 허용한다면, 취약한 기반 클래스 문제가 발생할 수 있다. 이에 대한 문제는 `Effective Java`에서
Joshua Block가 언급한적 있다. 

*취약한 기반 클래스 : 하위 클래스가 기반 클래스에 대해 가졌던 가정이 기반 클래스를 변경함으로써 깨져버린 경우*

코틀린은 이런 문제를 막기 위해 클래스와 메소드는 기본적으로 final로 설계되었다.

```kotlin
open class RichButton : Clickable {
    fun disable() {} // 하위 클래스가 이 메소드를 오버라이드 할 수 없다.
    
    open fun animate() {} // 하위 클래스에서 이 메소드를 오버라이드 할 수 있다.
    
    override fun click() {} // 오버라이드한 메소드는 기본적으로 열려있다.
}
```

### 가시성 변경자: 기본적으로 공개

코틀린에서는 4가지 가시성 변경자가 존재한다.

| 변경자             | 클래스 맴버              |
|-----------------|---------------------|
| public(default) | 모든 곳에서 볼 수 있다.      |
| internal        | 같은 모듈 안에서만 볼 수 있다.  |
| protected       | 하위 클래스 안에서만 볼 수 있다. |
| private         | 같은 클래스 안에서만 볼 수 있다. |

### 내부 클래스와 중첩된 클래스: 기본적으로 중첩 클래스

코틀린에서는 중첩 클래스(nested class)는 명시적으로 요청하지 않는 한 바깥쪽 클래스 인스턴스에 대한 접근 권한이 없다.
만약 이를 내부 클래스로 변경해 바깥쪽 클래스에 대한 참조를 포함하게 만들고 싶다면 inner 변경자를 붙여야 한다.

```kotlin
class Outer {
    class Inner { // Outer 클래스에 대한 참조를 갖지 않는다.
    }
}

Class Outer {
    inner class Inner { // Outer 클래스에 대한 참조를 갖는다.
        fun getOuterReference() : Outer = this@Outer
    }
}
```

이렇게 설계한 이유는 중첩 클래스가 외부에 클래스에 대한 참조를 갖고 있다는 점을 명시함하게 강제해
모호성을 없애주기 때문이다.

### 봉인된 클래스: 클래스 계층 정의 시 계층 확장 제한

코틀린에서는 sealed 변경자를 통해 상위 클래스를 상속한 하위 클래스 정의를 제한할 수 있다.
이때 하위 클래스는 반드시 상위 클래스 안에 중첩 시켜야 한다.

```kotlin
sealed class Expr {
    class Num(val value: Int) : Expr()
    class Sum(val left: Expr, val right: Expr) : Expr()
}
```
sealed 변경자는 인터페이스에 적용할 수 없다. 왜냐하면 인터페이스를 자바 쪽에서 구현하지 못하게 막을 수 있는 수단이
코틀린 컴파일러에 없기 때문이다.


sealed 변경자의 장점은 when과 같이 사용할 때 많은 편리함을 제공해준다.

sealed class가 아닌경우
```kotlin
interface Expr
class Num(val value: Int) : Expr
class Sum(val left: Expr, val right: Expr) : Expr

fun eval(e: Expr): Int = 
    when(e) {
        is Num -> e.value
        is Sum -> eval(e.right) + eval(e.left)
        else -> throw IllegalArgumentException("Unkown expression")
    }
```
- 문제점
  - when에서 항상 default 분기를 추가해야한다.
  - 하위 클래스를 추가해도 컴파일러가 when이 모든 경우를 처리하는지 제대로 검사할 수 없다.

sealed class
```kotlin
sealed class Expr {
    class Num(val value: Int) : Expr()
    class Sum(val left: Expr, val right: Expr) : Expr()
}

fun eval(e: Expr): Int =
    when(e) {
        is Num -> e.value
        is Sum -> eval(e.right) + eval(e.left)
    }
```
- 장점
  - default 분기가 필요없다.
  - 하위 클래스가 추가되면 컴파일러가 알려준다.

## 4.2 뻔하지 않은 생성자와 프로퍼티를 갖는 클래스 선언

### 클래스 초기화: 주 생성자와 초기화 블록

코틀린에서 클래스를 선언하는 방법에 대해 알아보자.

```kotlin
class User constructor(_nickname: String)/* 주 생성자 */ {
    val nickname: String
    
    init {
        nickname = _nickname
    }
}
```
- 객체를 생성할때 별도의 코드가 필요하면 초기화 블록을 활용할 수 있다.
- `_`는 프로퍼티와 생성자 파라미터를 구분해준다.
- 주 생성자 앞에 별다른 애노테이션이나 가시성 변경자가 없다면 `constructor`를 생략해도된다.

위 코드를 좀 더 간결하게 바꿔보자.
```kotlin
class User(_nickname: String) {
    val nickname = _nickname
}
```
- 프로퍼티를 초기화하는 식이나 초기화 블록 안에서만 주 생성자의 파라미터를 참조할 수 있다.

더 간략하게 바꿔보자
```kotlin
class User(val nickname: String)
```

클래스에 기반 클래스가 있다면 주 생성자에서 기반 클래스의 생성자를 호출해야한다.
```kotlin
open class User(val username: String) { ... }
class TwitterUser(nickname: String) : User(nickname) { ... }
```
- 클래스 정의에 있는 상위 클래스 및 인터페이스 목록에서 이름 뒤에 괄호를 통해 쉽게 기반 클래스와 인터페이스를 구분할 수 있다.

코틀린에서 간단한 주 생성자 문법을 제공하는 이유는, 실제로 대부분의 경우 클래스의 생성자는 아주 간단하기 때문이다.
이를 이용하면 코드를 간결하게 작성할 수 있다.

### 부 생성자: 상위 클래스를 다른 방식으로 초기화

일반적으로 코틀린에서는 생성자가 여럿 있는 경우가 자바보다 훨씬 적다. 왜냐하면 코틀린의 디폴트 파라미터 값과 이름 붙인
인자 문법이 있기 때문이다.

부 생성자
```kotlin
open class View {
  constructor(ctx: Context) {
    //...
  }

  constructor(ctx: Context, attr: AttributeSet) {
      // ...
  }
}
```

### 인터페이스에 선언된 프로퍼티 구현

코틀린에서는 인터페이스에 추상 프로퍼티 선언을 넣을 수 있다.

```kotlin
interface User {
    val nickname: String
}
```

이는 User 인터페이스를 구현하는 클래스가 nickname의 값을 얻을 수 있는 방법을 제공해야 한다는 뜻이다.
그리고 당연히 인터페이스에 있는 프로퍼티 선언에는 뒷받침하는 필드나 게터 등의 정보가 들어있지 않다.

```kotlin
class PrivateUser(override val nickname: String) : User
class SubscribingUser(val email: String) : User {
    override val nickname: String
        get() = email.substringBefore('@')
}
class FaceBookUser(val accountId: Int) : User {
    override val nickname = getFacebookName(accountId)
}
```

### 게터와 세터에서 뒷받침하는 필드에 접근

접근자의 본문에서는 field라는 특별한 식별자를 통해 뒷받침하는 필드에 접근할 수 있다.

컴파일러는 디폴트 접근자 구현을 사용하건 직접 게터나 세터를 정의하건 관계없이 게터나 세터에서 field를 사용하는 프로퍼티에
대해 뒷받침 필드를 생성해준다. 따라서 field를 사용하지 않는 커스텀 접근자 구현을 정의한다면 뒷받침하는 필드는 존재하지 않는다.

예제 : 프로퍼티에 저장된 값의 변경 이력 로그에 남기기
```kotlin
class User(val name: String) {
    var address: String = "unspecified"
        set(value: String) {
            println("""
                Address was changed for ${name}:
                "${field}"-> "${value}".""".trimIndent())
            field = value
        }
}
```

### 접근자 가시성 변경

접근자의 가시성은 기본적으로 프로퍼티의 가시성과 같다. 하지만 원한다면 바꿀 수 있다.

```kotlin
class LengthCounter {
    var counter: Int = 0
        private set
  
    fun addWord(word: String) {
        counter += word.length
    }
}
```

## 4.3 컴파일러가 생성한 메소드: 데이터 클래스와 클래스 위임

자바 플랫폼에서는 클래스가 equals, hashCode, toString 등의 메서드를 구현해야 한다. 따라서 계속 반복되는 행동을 해야하고 
코드가 번잡해진다. 코틀린 컴파일러는 이러한 작업을 보이지 않는 곳에서 해준다.

### 모든 클래스가 정의해야 하는 메소드

자바와 마찬가지로 코틀린 클래스도 toString, equals, hashCode 등을 오버라이드 할 수 있다.

- toString
  - 인스턴스의 문자열 표현을 얻는다. -> 로깅시 유용한 정보를 남길 수 있다.
- equals
  - 객체의 동등성을 검사한다. 
    - 코틀린에서는 동등성을 `==`, 동일성은 `===`을 사용한다.
- hashCode
  - 인스턴스의 HashCode를 반환한다.
    - equals를 오버라이드할 때는 반드시 hashCode를 오버라이드 해야한다.
      - JVM 언어에서는 equals()가 true를 반환하는 두 객체는 반드시 같은 hashCode()를 반환해야 한다. 그렇지 않으면 Hash 기반의
자료구조를 사용할때 의도치 않게 동작할 것이다.

### 데이터 클래스: 모든 클래스가 정의해야 하는 메소드 자동 생성

데이터 클래스는 데이터를 저장하는 역할만을 수행하는 클래스를 의미힌다. 그리고 이런 클래스는 toString, equals, hashCode를 
반드시 오버라이드해야 한다. 코틀린에서는 이를 자동으로 해준다.

```kotlin
data class Client(val name: String, val postalCode: Int)
```
- 주의점
  - 주 생성자 밖에 정의된 프로퍼티는 equals나 hashCode를 계산할 때 고려의 대상이 아니다.

Data 클래스는 추가로 몇 가지 유용한 메서드를 제공해준다. 그 중 copy() 메소드가 있다. copy() 메소드는 객체를
복사하면서 일부 프로퍼티를 바꿀 수 있게 해준다. 이는 불변 객체를 활용하기 더 쉽게 해준다.

```kotlin
fun copy(name: String = this.name, postalCode: Int = this.postalCode) =
    Client(name, postal)


val hyuk = Client("eunhyuk", 7777)
println(hyuk.copy(postalCode = 1111))
```

### 클래스 위임: by 키워드 사용

코틀린이 기본적으로 클래스를 final로 취급한 이유는 대규모 객체지향 시스템에서 무분별한 구현 상속은 시스템을 취약하게 만들기 때문이다.
하위 클래스는 상위 클래스의 메서드를 오바라이드하면서 상위 클래스 세부 구현에 의존하게 된다. 그리고 추후에 상위 클래스가 바뀐다면 
하위 클래스가 상위 클래스에 갖고 있던 가정이 깨져 코드가 정상적으로 작동하지 못하는 경우가 있다.

하지만 종종 상속을 허용하지 않는 클래스에 새로운 동작을 추가해야 할 때가 있다. 그리고 이때 사용하는 일반적인 방법이 데코레이터 패턴이다.
하지만 데이터레이터 패턴을 사용하기 위해서는 준비 코드가 상당히 많이 필요하다. 코틀린에서는 이런 준비 코드를 컴파일러가 만들어준다.

데코레이터 패턴 직접 구현
```kotlin
class DelegatingCollection<T> : Collection<T> {
    private val innerList = arrayListOf<T>()
  
    override val size: Int get() = innerList.size
    override fun isEmpty(): Boolean = innerList.isEmpty()
    override fun contains(element: T): Boolean = innerList.contains(element)
    override fun iterator(): Iterator<T> = innerList.iterator()
    override fun containsAll (elements: Collection<T>): Boolean = innerList.containsAll(elements)
}
```

`by`를 이용해 데코레이터 패턴 구현
```kotlin
class DelegatingCollection<T>(
  innerList: Collection<T> = ArrayList<T>()
) : Collection<T> by innerList {}
```

## object 키워드: 클래스 선언과 인스턴스 생성

코틀린에서는 `object` 키워드를 클래스를 정의하면서 동시에 인스턴스를 생성할때 사용한다.

### 객체 선언: 싱글턴을 쉽게 만들기

코틀린은 객체 선언 기능을 통해 싱글턴을 언어에서 기본 지원한다.

```kotlin
object CaseInsensitiveFileComparator : Comparator<File> {
    override fun compare(file1: File, file2: File): Int {
        return file1.path.compareTo(file2.path, ignoreCase = true)
    }
}

println(CaseInsensitiveFileComparator.compare(File("/User"), File("/user")))
```

### 동반 객체: 팩토리 메소드와 정적 맴버가 들어갈 장소

코틀린 클래스 안에는 정적인 맴버가 없다. 하지만 정적 맴버가 클래스 내부 정보에 접근하지 않을때는 최상위 함수를 사용하는게 좋다.
그렇지 않을 경우에는 동반 객체를 활용하면 된다.

동적 객체는 팩터리 패턴을 구현하기 가장 적합한 위치다.

```kotlin
class User private constructor(val nickname: String) {
    companion object {
        fun newSubscribingUser(email: String) = User(email.substringBefore('@'))
        fun newFacebookUser(accountId: Int) = User(getFacebookName(accountId))
    }
}
```
- 팩토리 메서드 장점
  - 목적에 따라 이름을 정할 수 있다.
  - 팩토리 메서드가 선언된 클래스의 하위 클래스 객체를 반환할 수 있다.
  - 캐시 역할을 할 수 있다.
- 팩토리 메서드 단점
  - 클래스를 확장해야만 하는 경우 동반 객체 맴버를 하위 클래스에서 오버라이드 할 수 없다.

### 동반 객체를 일반 객체처럼 사용

동반 객체도 클래스 안에 정의된 일반 객체다. 따라서 이름을 붙이거나, 인터페이스를 상속하거나, 확장 함수와 프로퍼티를 정의할 수 있다.

이름 붙이기
```kotlin
class Person(val name: String) {
    companion object Loader {
        fun fromJson(jsonText: String): Person = ...
    }
}

person = Person.Loader.fromJson("{name: 'Dmitry')")
```

인터페이스 구현
```kotlin
interface JSONFactory<T> {
    fun fromJSON(jsonText: String): T
}

class Person(val name: String) {
    companion object : JSONFactory<Person> {
        override fun fromJSON(jsonText: String): Person = ...
    }
}

-------------------------------------
fun loadFromJSON<T>(factory: JSONFactory<T>): T {
    ...
}
loadFromJSON(Person) // 동반 객체가 구현한 JSONFactory의 인스턴스를 넘길 때 Person 클래스 이름을 사용한다.
```

동반 객체 확장
```kotlin
class Person(val firstName: String, val lastName: String) {
    companion object {
    }
}

fun Person.Companion.fromJSON(json: String): Person {
    ...
}

val p = Person.fromJSON(json) 
```
- 주의
  - 동반 객체에 대한 확장 함수를 작성하려면 원래 클래스에 동반 객체를 꼭 선언해야 한다.

### 객체 식: 무명 내부 클래스를 다른 방식으로 작성

무명 객체를 정의할 때도 object 키워드를 쓴다.

```kotlin
fun countClicks(window: Window) {
    var clickCount = 0
    
    window.addMouseListner(object : MouseAdapter()  {
        override fun mouseClicked(e: MouseEvent) {
            clickCount++ // 자바와 달리 final이 아닌 로컬 변수도 식 안에서 사용할 수 있다.
        }
    })
}
```
