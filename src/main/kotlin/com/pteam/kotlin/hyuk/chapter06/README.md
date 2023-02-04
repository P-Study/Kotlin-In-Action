# Chapter06 코틀린 타입 시스템

## 6.1 널 가능성

널 가능성은 `NullPointerException`를 피할 수 있게 돕기 위한 코틀린 타입 시스템의 특성이다. 

코틀린을 비롯한 최신 언어에서 null에 대한 접근 방법은 가능한 이 문제를 실행시점에서 컴파일시점으로 옮기는 것이다. 

### 널이 될 수 있는 타입

코틀린과 자바의 첫 번째이자 가장 중요한 차이는 코틀린 타입 시스템이 널이 될 수 있는 타입을 명시적으로 지원하다는 점이다.

기본적으로 코틀린의 모든 타입은 널이 될 수 없는 타입이다. 뒤에 `?`가 붙어야 널이 될 수 있다.
`Type? = Type or null`

널이 될 수 있는 타입은 널이 될 수 없는 타입과 비교해 제약사항이 많다. 따라서 거의 null과 비교하는 작업이 중요하다.
```kotlin
fun strLenSafe(s: String?): Int = if (s != null) s.length else 0
```
- 문제점
  - 널이 될 수 있는 타입에는 항상 if 검사가 필요하다. (번잡함)

코틀린에서는 위 문제를 해결하기 위해 널이 될 수 있는 타입을 좀 더 편하게 사용할 수 있는 여러 도구를 제공해준다.

### 타입의 의미

타입은 분류로 타입은 어떤 값들이 가능한지와 그 타입에 대해 수행할 수 있는 연산의 종류를 결정한다.

자바의 경우 타입 분류를 잘 하고 있지 않다. 
```java
String nullStr = null
if (nullStr instanceof String) {
    System.out.println("nullStr is String type")
}
```
위 코드에서 instanceof 연산자는 nullStr이 String이 아니라고 한다. 즉, 자바의 타입 시스템이 널을 제대로 다루지 못하고 있다.

코틀린의 널이 될 수 있는 타입은 위 문제에 대한 종합적인 해법을 제공한다.

-Tip
  - 실행 시점에 널이 될 수 있는 타입이나 널이 될 수 없는 타입의 객체는 같다. (벼로의 실행 시점 부가 비용이 들지 않는다.)

### 안전한 호출 연산자: ?.

`?.`는 null 검사와 메소드 호출을 한 번의 연산으로 수행한다. 호출하려는 값이 null이 아니라면 ?.은 일반 메소드 호출처럼 작동한다. 하지만 호출하려는 값이
null이면 이 호출은 무시되고 null이 결과 값이 된다.
```kotlin
s?.toUpperCase() // 1번

if (s != null) s.toUpperCase() else null // 1번과 동일한 의미다.
```

### 엘비스 연산자: ?:

엘비스 연산자`?:`를 이용해 null 대신 사용할 디폴트 값을 지정을 편리하게 할 수 있다. 좌항 값이 널이 아니면 조항 값을 결과로 하고, 좌항 값이 널이면 우항 값을 결과로
한다.
```kotlin
fun strLenSafe(s: String?): Int = s?.length ?: 0
```

엘비스 연산자의 우항에 `return`, `throw`등의 연산을 넣을 수 있고, 이를 이용해 좌항이 널이면 즉시 어떤 값을 반환하거나 예외를 던질 수 있다.
이런 패턴은 함수의 전제 조건을 검사하는 경우 유용하다.
```kotlin
class Address(val streetAddress: String, val zipCode: Int, val city: String, val country: String)
class Company(val name: String, val address: Address?)
class Person(val name: String, val company: Company?)

fun printShippingLabel(person: Person) {
    val address = person.company?.address
      ?: throw IllegalArguementException("No address")
    with (address) {
        println(streetAddress)
        println("${zipCode} ${city} ${country}")
    }
}
```

### 안전한 케스트: as?

`as?` 연산자는 어떤 값을 지정한 타입으로 캐스트한다. `as?`는 값을 대상 타입으로 변환할 수 없으면 null을 반환한다.

안전한 케스트는 일반적으로 엘비스 연산자와 같이 사용된다.
```kotlin
class Person(val firstName: String, val lastName: String) {
    override fun equals(other: Any?): Boolean {
        val otherPerson = o as? Person ?: returen false
      
        return otherPerson.firstName == firstName && otherPerson.lastName == lastName
    }
  
    override fun hashCode(): Int{ ... }
}
```

### 널 아님 단언: !!

`!!`은 어떤 값이든 널이 될 수 없는 타입으로 강제로 바꿀 수 있다. 실제 널에 대해 `!!`를 사용하면 NPE가 발생한다.

코틀린 설계자들은 컴파일러가 검증할 수 없는 단언(`!!`)을 사용하기 보다는 더 나은 방법을 사용하는 걸 권장한다. 하지만 널이 아닌 값을 전달 받는 사실이 분명한 경우는
널 아님 단언이 적합하다.

- Tip
  - `!!`에서 발생한 NPE의 스택 트레이스에는 어떤 파일의 몇 번째 줄인지에 대한 정보는 들어있지만 어떤 식에서 예외가 발생했는지에 대한 정보는 들어있지 않다. 따라서 `!!`를 한 줄에 함께 쓰는 일을 피하는게 좋다.

### let 함수

let 함수를 안전한 호출 연산자(`?.`)와 함께 사용하면 원하는 식을 평가해서 결과가 널인지 검사한 다음에 그 결과를 변수에 넣는 작업을 간단한 식을 사용해 한꺼번에 처리할 수 있다.

```kotlin
fun sendEmailTo(email: String) { /* ... */ }

email?.let { email -> sendEmailTo(email)}
```

- Tip
  - 여러 값이 널인지 검사해야 한다면 `let` 호출을 중첩시켜 처리할 수 있다. 하지만 중첩시키면 코드가 복잡해서 가독성이 떨어진다. 이런 경우는 `if`를 사용해 모든 값을 한꺼번에 검사하는 편이 낫다.

### 나중에 초기화할 프로퍼티

코틀린에서는 일반적으로 생성자에서 모든 프로퍼티를 초기화해야 한다. 게다가 프로퍼티 타입이 널이 될 수 없는 타입이라면 반드시 널이 아닌 값으로 그 프로퍼티를 초기화해야 한다.
따라서 초기화 값을 제공할 수 없으면 널이 될 수 있는 타입을 사용할 수 밖에 없다. 그러면 프로퍼티에 접근할 때마다 널 검사를 넣거나 `!!`을 사용해야해 번거롭다.

위 문제를 나중에 초기화를 이용해 해결할 수 있다.

```kotlin
class MyService {
    fun performAction(): String = "foo"
}

class MyTest {
    private lateinit var myService: MyService
    
    @Before fun setUp() {
        myService = MyService()
    }
  
    @Test fun testAction() {
        Assert.assertEquals("foo", myService.performAction())
    }
}
```

### 널이 될 수 있는 타입 확장

널이 될 수 있는 타입에 대한 확장 함수를 정의하면 null 값을 다루는 강력한 도구로 활용할 수 있다. 어떤 메소드를 호출하기 전에 수신 객체 역할을 하는 변수가 널이 될 수 없다고 보장하는 대신,
직접 변수에 대해 메소드를 호출해도 확장 함수인 메서드가 알아서 널을 처리해준다.

- 참고
  - 정적 디스패치
    - 컴파일러가 컴파일 시점에 어떤 메소드가 호출될지 결정해서 코드를 생성하는 방식
  - 동적 디스패치
    - 객체별로 자신의 메소드에 대한 테이블을 저장하는 방법
      - 메소드 테이블은 클래스마나 하나씩 만들고 각 객체는 자신의 클래스에 대한 참조를 통해 그 메소드 테이블을 찾는 경우가 많다.

안전한 호출 없이도 널이 될 수 있는 수신 객체 타입에 대해 선언된 확장 함수를 호출 가능하다.
```kotlin
fun verifyUserInput(input: String?) {
    if (input.isNullOrBlank()) {
      println("Please fill in the required fields")
    }
}
``` 

자바에서는 메소드 안의 this는 그 메소드가 호출된 수신 객체를 가리키므로 항상 널이 아니다. 반면 코틀린에서는 널이 될 수 있는 타입의 확장 함수 안에서는 this가 널이 될 수 있다.
```kotlin
fun String?.isNullOrBlank(): Boolean = this == null || this.isBlank()
```

- Tip
  - 확장 함수를 작성할 때 처음에는 널이 될 수 없는 타입에 대한 확장 함수를 정의하라. 나중에 널이 될 수 있는 타입이 함수를 호출할 필요가 있다면 확장함수 안에서 널처리를 하고 널이 될 수 있는 타입에 대한 확장 함수로 바꿔라.

### 타입 파라미터의 널 가능성

코틀린에서는 함수나 클래스의 모든 타입 파라미터는 기본적으로 널이 될 수 있다.
```kotlin
fun<T> printHashCode(t: T) {
    println(t?.hashCode())
}

printHashCode(null) // T의 타입은 Any? 로 추론된다.
```

타입 파라미터가 널이 아님을 확실히 하려면 널이 될 수 없는 타입 상한을 지정해야 한다.
```kotlin
fun<T: Any> printHashCode(t: T) {
    println(t.hashCode())
}

printHashCode(null) // -> Error: Type parameter bound for `T` is not satisfied
```

### 널 가능성과 자바

자바와 코틀린은 타입 시스템에 차이가 있다. 이 둘을 같이 사용하면 어떻게 될까?

#### 애노테이션 활용

자바 코드에서도 애노테이션으로 널 관련 정보를 줄 수 있다. 코틀린은 이런 정보를 활용한다.
```kotlin
(Java) @Nullable + Type = (Kotlin) Type?
(Java) @NotNull + Type = (Kotlin) Type
```
추가로 코틀린은 JSR-305표준(javax.annotation), 안드로이드(android.support.annotation), 젯브레인스(org.jetbrains.annotation) 등을 이해할 수 있다.

#### 플랫폼 타입

코틀린은 자바 코드에서 널 관련 정보를 찾을 수 없다면 **플랫폼 타입**으로 처리한다.

플랫폼 타입은 널이 될 수 있는 타입으로 처리해도 되고 널이 될 수 없는 타입으로 처리해도 된다. (컴파일러는 모든 연산을 허용한다. 책임은 사용자에게 있다.)
```java
/* Java */
public class Person {
	private final String name;
	
	public Person(String name) {
		this.name = name;
    }
	
	public String getName() {
		return name;
    }
}
```

널이 될 수 없는 타입으로 다루기
```kotlin
/* kotlin */
fun yellAt(person: Person) {
    println(person.name.toUppercase() + "!!!")
}

yellAt(Person(null)) // java.lang.IllegalArgumentException: Parameter specified as non-null, is null: method toUpperCase, parameter $receiver
```
NPE가 터지지 않고, 수신 객체로 널이 받을 수 없다는 더 자세한 예외가 발생한다. 왜냐하면 코틀린 컴파일러는 공개 코틀린 함수의 널이 아닌 타입인 파라미터와 수신 객체에 대한
널 검사를 추가해준다. (함수 호출 시점에 이뤄진다.)

널이 될 수 있는 타입으로 다루기
```kotlin
fun yellAtSafe(person: Person) {
    println((person.name ?: "Anyone").toUpperCase() + "!!!")
}
```

- 플랫폼 타입 도입 이유?
  - 모든 자바 타입을 널이 될 수 있는 타입으로 다루면 더 안전할 수 있다. 하지만 이는 널 안전성으로 얻는 이득보다 검사에 드는 비용이 훨씬 더 크다. 또한 매번 검사를 작성하는 것도 성가신 일이다.
  - Tip
    - 코틀린에서는 플랫폼 타입을 선언할 수 없다.
    - 코틀린 컴파일러는 플랫폼 타입을 `String!`로 표현한다.

#### 상속

코틀린에서 자바 메소드를 오버라이드할 때는 그 메소드의 파라미터와 반환 타입을 널이 될 수 있는 타입으로 선언할지 널이 될 수 없는 타입으로 선언할지 결정해야 한다.

```java
/* Java */
interface StringProcessor {
	void process(String value);
}
```

코틀린으로 자바 메소드 오버라이드
```kotlin
class StringPrinter : StringProcessor {
    override fun process(value: String) {
        println(value)
    }
}

class NullableStringPrinter : StringProcessor {
    override fun process(value: String?) {
        if (value != null) {
            println(value)
        }
    }
}
```

## 코틀린 원시 타입

코틀린은 원시 타입과 래퍼 타입을 구분하지 않는다.

### 원시 타입: Int, Boolean 등

코틀린은 자바와 달리 원시 타입과 래퍼 타입을 구분하지 않는다. 따라서 항강 같은 타입을 사용해 편리성을 제공한다.

위 구현은 한가지 걱정을 준다. 코틀린이 원시 타입을 항상 객체로만 표현한다면 비효율적일 것이다. 하지만 코틀린은 그러지 않는다. 실행 시점에서 숫자 타입은
가능한 한 가장 효율적인 방식으로 표현된다.

- 코틀린 <-> 자바
  - Int 같은 코틀린 타입은 널이 들어갈 수 없어 자바의 원시 타입으로 컴파일할 수 있다.
  - 자바의 원시 타입의 값도 널이 될 수 없으므로 널이 될 수 없는 타입으로 취급될 수 있다.

### 널이 될 수 있는 원시 타입: Int?, Boolean? 등

코틀린에서 널이 될 수 있는 원시 타입을 사용하면 그 타입은 자바의 래퍼 타입으로 컴파일된다.

- 제네릭 클래스의 경우 래퍼 타입을 사용하는 이유
  - JVM에서 제네릭을 구현할 때 타입 인자로 원시 타입을 허용하지 않는다.
    - 원시 타입으로 이뤄진 대규모 컬렉션을 효율적으로 저장해야 한다면 원시 타입으로 이뤄진 효율적인 컬렉션을 제공하는 서드 파티 라이브러리를 사용하거나 배열을 사용해야한다.

### 숫자 변환

코틀린은 자바와 달리 한 타입의 숫자를 다른 타입의 숫자로 자동 변환하지 않는다. 왜냐하면이는 혼란을 줄 수도 있기 때문이다.
```kotlin
val i = 1
val l: Long = i // Error: type mismatch
```

대신 코들린은 모든 원시 타입에 대해 변환 함수를 제공한다. 
ex) `toByte()`, `toShrot()`, `toChar()`

숫자 리터럴을 사용할 때는 변환 함수를 호출할 필요가 없다.
```kotlin
val l = 1L // Long
```

### Any, Any?: 최상위 타입

코틀린에서는 `Any` 타입이 모든 널이 될 수 없는 타입의 조상 타입이다. 추가로 Int 등의 원시 타입도 포함한다.

### Unit 타입: 코틀린의 void

코틀린 Unit 타입은 자바 void와 같은 기능을 한다.
```kotlin
fun f(): Unit { ... }
fun f() { ... } // 위 함수와 같은 의미다.
```

- Unit 특징
  - void와 달리 타입 인자로 쓸 수 있다.
  - Unit 타입에 속한 값은 단 하나뿐이며, 그 이름도 Unit이다.

위 두 특징 덕분에 제네릭 파라미터를 반환하는 함수를 오버라이드할 때 유용한다.
```kotlin
inerface Processor<T> {
    fun process(): T
}

class NoResultProcessor : Processor<Unit> {
    override fun process() {
        // 업무 처리 코드
        // Unit 값을 묵시적으로 반환한다.
    }
}
```

- 네이밍이 Unit인 이유?
  - Unit은 함수형 프로그래밍에서 `단 하나의 인스턴스만 갖는 타입`을 의미한다.
  - Void 네이밍은 `Nothing` 타입과 헷갈릴 수 있다.

### Nothing 타입: 이 함수는 결코 정상적으로 끝나지 않는다.

Nothing은 결코 성공적으로 값을 돌려주는 일이 없음을 의미한다. 
```kotlin
fun fail(message: String) : Nothing {
    throw IllegalStateException(message)
}
```

Nothing 타입은 아무 값도 포함하지 않는다. 따라서 함수의 반환 타입이나 반환 타입으로 쓰일 타입 파라미터로만 쓸 수 있다.