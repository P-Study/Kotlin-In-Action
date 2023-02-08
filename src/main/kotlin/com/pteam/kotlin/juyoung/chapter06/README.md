# 코틀린 타입 시스템

## 널 가능성

* ?를 사용하여 널 허용
* 널이 될 수 있는 값을 널이 될 수 없는 타입의 변수에 대입 불가능

### 안전한 호출 연산자: ?.

* null 검사와 메소드 호출을 한 번의 연산으로 수행
* 호출하려는 값이 null이면 이 호출은 무시되고 null이 결과 값이 됨
* ?. 연산자를 연쇄적으로 사용하여 코드를 간결하게 할 수 있음

```kotlin
val s: String?
s?.toUpperCase()
if (s != null) s.toUpperCase() else null
```

### 엘비스 연산자: ?:

* null 대신 사용할 디폴트 값을 지정
* 엘비스 연산자의 우항에 return 이나 throw 등의 연산을 넣을 수 있음

```kotlin
fun foo(s: String?) {
    val t: String = s ?: ""
}

fun strLenSafe(s: String?): Int = s?.length ?: 0
strLenSafe("abc")
strLenSafe(null)
```

### 안전한 캐스트: as?

* 값을 대상 타입으로 변환할 수 없으면 null을 반환

### 널 아님 단언: !!

* 어떤 값이든 널이 될 수 없는 타입으로 강제로 바꿈
* !!를 사용하기 전에 더 나은 방법을 찾아보기
* !!가 더 나은 해법인 경우도 있음
* 연쇄적으로 사용하지 말 것 -> 널에 대해 사용해서 발생하는 예외의 스택 트레이스에는 어떤 파일의 몇 번째 줄인지에 대한 정보는 들어있지만
  어떤 식에서 예외가 발생했는지에 대한 정보는 들어있지 않음

### let 함수

* 널이 될 수 있는 값을 널이 아닌 값만 인자로 받는 함수에 넘김
* let을 중첩시켜 처리하면 코드가 복잡해져서 알아보기 어려워짐 -> if를 사용해 모든 값을 한번에 검사하는 편이 나음

```kotlin
fun sendEmailTo(email: String) {}

fun main() {
    val email: String?
    email?.let { sendEmailTo(it) }
}
```

### 나중에 초기화할 프로퍼티
* 나중에 초기화할 프로퍼티는 var여야 함
```kotlin
class MyService {
    fun performAction(): String = "foo"
}

class MyTest {
    // 초기화하지 않고 널이 될 수없는 프로퍼티 선언
    private lateinit var myService: MyService

    @Before
    fun setUp() {
        myService = MyService()
    }

    @Test
    fun testAction() {
        Assert.assertEquals(
            "foo",
            myService.performAction()   // 널 검사를 수행하지 않고 프로퍼티를 사용
        )
    }
}
```

### 널이 될 수 있는 타입 확장
* 안전한 호출 없이도 널이 될 수 있는 수신 개게 타입에 대해 선언된 확장 함수를 호출 가능
* inNullOrEmpty, isNullOrBlank 메소드

### 타입 파라미터의 널 가능성
* 타입 파라미터가 T를 클래스나 함수 안에서 타입 이름으로 사용하면 이름 끝에 물음표가 없더라고 T가 널이 될 수 있는 타입
* 타입 파라미터가 널이 아님을 확실히 하려면 널이 될 수 없는 타입 상한을 지정해야 함
```kotlin
fun <T> printHashCode(t: T) {
    println(t?.hashCode())  // t가 널이 될 수 있음 -> 안전한 호출 사용해야 함
}

printHashCode(null) // T의 타입은 Any?로 추론

// 타입 상한을 지정하여 널이 될 수 없도록 처리
fun <T: Any> printHashCode(t: T) {
  println(t.hashCode())
}
```

### 널 가능성과 자바
#### 플랫폼 타입
* 코틀린이 널 관련 정보를 알 수 없는 타입
* 플랫폼 타입에 대해 수행하는 모든 연산에 대한 책임은 개발자에게 있음
* 대부분의 라이브러리는 널 관련 애노테이션을 쓰지 않으므로 자바 API를 다룰 때 조심해야 함
* 코틀린에서 플랫폼 타입 선언 불가, 자바 코드에서 가져온 타입만 플랫폼 타입이 됨

#### 상속
* 코틀린에서 자바 메소드를 오버라이드할 때 그 메소드의 파라미터와 반환 타입을 결정해야 함
```java
interface StringProcessor {
    void process(String value);
}
```
```kotlin
class StringPrinter: StringProcessor {
    override fun process(value: String?) {
        if (value != null) {
            println(value)
        }
    }
}
```

## 코틀린의 원시 타입

### 원시 타입: Int, Boolean 등
* 코틀린은 원시타입과 래퍼타입을 구분하지 않으므로 항상 같은 타입을 사용
* 실행 시점에서 숫자 타입은 가능한 한 가장 효율적인 방식으로 표현
* 대부분의 경우 코틀린의 Int 타입은 자바 int 타입으로 컴파일 됨
* 컬렉션의 타입 파라미터로 Int 타입을 넘기면 래퍼 타입에 해당하는 java.lang.Integer 객체가 들어감
* 자바 원시 타입에 해당하는 타입
  * 정수타입 Byte, Short, Int, Long
  * 부동소수점 수 타입 Float, Double
  * 문자 타입 Char
  * 불리언 타입 Boolean

### 널이 될 수 있는 원시 타입: Int?, Boolean? 등
* 코틀린에서 널이 될 수 있는 원시 타입을 사용하면 그 타입은 자바의 래퍼 타입으로 컴파일됨

### 숫자 변환
* 개발자의 혼란을 막기 위해 타입 변환을 명시
* 한 타입의 숫자를 다른 타입의 숫자로 자동 변환하지 않음
* 직접 변환 메소드를 호출해야 함
* 숫자 리터럴을 사용할 때는 보통 변환 함수를 호출할 필요가 없음

### Any, Any?: 최상위 타입
* 코틀린에서 Any 타입이 모든 널이 될 수 없는 타입의 조상 타입
* Any 타입의 변수에는 null 이 들어갈 수 없음
* 널을 포함하는 모든 값을 대입하려면 Any?를 사용
```kotlin
val answer: Any = 42 // Any가 참조 타입이기 때문에 42가 박싱
```

### Unit 타입: 코틀린의 void
* 코틀린 Unit타입은 자바의 void와 같은 기능
* 반환 타입 선언 없이 정의한 블록이 본문인 함수와 같음

### Nothing 타입: 이 함수는 결코 정상적으롤 끝나지 않는다
* Nothing을 반환하는 함수를 엘비스 연산자의 우항에 사용해서 전제 조건을 검사할 수 있음
```kotlin
fun fail(message: String): Nothing {
    throw IllegalStateException(message)
}

fail("Error occurred")

val address = company.address ?: fail("No address")
println(address.city)
```

### 컬렉션과 배열
#### 널 가능성과 컬렉션
* 널이 될 수 있는 값으로 이뤄진 컬렉션으로 널 값을 걸러내는 경우 filterNotNull 함수 사용
```kotlin
fun addValidNumbers(numbers: List<Int?>) {
    // validNumbers는 List<Int> 타입
    val validNumbers = numbers.filterNotNull()
}
```

#### 읽기 전용과 변경 가능한 컬렉션
* Collection에는 원소를 추가하거나 제거하는 메소드가 없음
* 컬렉션의 데이터를 수정하려면 MutableCollection 사용
* 가능하면 항상 읽기 전용 인터페이스를 사용하는 것을 일반적인 규칙으로 삼을 것
* 읽기 전용 컬렉션이 항상 스레드세이프 하지는 않음
* 다중 스레드환경에서 데이터를 다루는 경우 그 데이터를 적절히 동기화하거나 동시 접근을 허용하는 데이터 구조 활용

#### 코틀릭 컬렉션과 자바
* 컬렉션을 자바로 넘기는 코틀린 프로그램을 작성한다면 호출하려는 자바 코드가 컬렉션을
변경할지 여부에 따라 올바른 파라미터 타입을 사용해야 함

#### 컬렉션을 플랫폼 타입으로 다루기
* 컬렉션이 널이 될 수 있는가?
* 컬렉션의 원소가 널이 될 수 있는가?
* 오버라이드하는 메소드가 컬렉션을 변경할수 있는가?

#### 객체의 배열과 원시 타입의 배열
* toTypedArray를 사용하여 컬렉션을 배열로 변환
* forEachIndexed 함수와 람다 사용
```kotlin
val strings = listOf("a", "b", "c")
println("%s/%s/%s".format(*strings.toTypedArray()))

val fiveZeros = IntArray(5)
val fiveZerosToo = intArrayOf(0, 0, 0, 0, 0)

val squares = IntArray(5) { i -> (i+1) * (i+1) }
println(squares.joinToString())

fun main(args: Array<String>) {
    args.forEachIndexed { index, element ->
      println("Argument $index is: $element")
    }
}
```