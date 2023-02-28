# Chapter09 제네릭스

## 9.1 제네릭 타입 파라미터

타입 파라미터를 사용하면 '이 변수는 리스트다.'라고 말하는 대신 정확하게 '이 변수는 문자열을 담는 리스트다'라고 말할 수 있다.

- 참고
  - 코틀린 제네릭 raw 타입을 허용하지 않는다.
    - 자바의 경우 하위 호환성을 위해 raw 타입을 지원한다. 하지만 코틀린은 처음부터 제네릭 개념이 있었기 때문에 raw 타입을 지원하지 않는다.

### 제네릭 함수와 프로퍼티

제네릭 함수를 정의할 때와 마찬가지 방법으로 제네릭 확장 프로퍼티를 선언할 수 있다.
```kotlin
val <T> List<T>.penultimate: T
    get() = this[size - 2]
```
- 주의
  - 일반 프로퍼티는 타입 파라미터를 가질 수 없다.
    - 클래스 프로퍼티에 여러 타입의 값을 저장할 수 없으므로 당연하다.

### 제네릭 클래스 선언

자바와 마찬가지로 코틀린에서도 타입 파라미터를 넣은 꺾쇠 기호`<>`를 클래스 이름 뒤에 붙이면 클래스를 제네릭하게 만들 수 있다.
```kotlin
interface List<T> {
    operator fun get(index: Int): T
}
```

제네릭 클래스를 확장하는 클래스는 기반 타입의 제네릭 파라미터에 대해 타입 인자를 지정해야 한다. 이때 구체적인 타입을 넘길 수 있고 타입 파라미터로 받은 타입을
넘길 수도 있다.
```kotlin
class StringList: List<String> {
    override fun get(index: Int): String = ...
}

class ArrayList<T>: List<T> {
    override fun get(index: Int): T = ...
}
```

### 타입 파라미터 제약

타입 파라미터 제약 : 클래스나 함수에 사용할 수 있는 타입 인자를 제한하는 기능
```kotlin
fun <T : Number> List<T>.sum(): T
```
`T`는 `Number`의 하위 타입이거나 `Number`여야 한다.

타입 파라미터에 둘 이상의 제약을 가할 수도 있다.
```kotlin
fun <T> ensureTrailingPeriod(seq: T)
    where T : CharSequence, T : Appendable { /* do something */ }
```
타입인자가 반드시 `CharSequence`와 `Appendable` 인터페이스를 구현해야 한다.

### 타입 파라미터를 널이 될 수 없는 타입으로 한정

타입 파라미터에 아무런 상한을 정하지 않으면 `<T : Any?>`와 같다. 즉, 널을 허용한다. 만약 널 가능성을 제한하고 싶다면 `<T : Any>`를
사용하면된다.

## 9.2 실행 시 제네릭스 동작: 소거된 타입 파라미터와 실체화된 타입 파라미터

JVM의 제네릭스는 보통 타입 소거를 사용해 구현된다. 이는 장점도 있지만 단점도 있다.
가끔 타입 인자 정보가 런타임에 필요할 때가 있다. 코틀린에서는 이런 경우 `inline` 함수를 이용해 타인 인자가 지워지지 않게 할 수 있다.

### 실행 시점의 제네릭: 타입 검사와 캐스트

자바와 마찬가지로 코틀린 제네릭 타입 인자 정보는 런타임에 지워진다.
```kotlin
val list1: List<String> = listOf("a", "b")
val list2: List<Int> = listOf(1, 2, 3)
```
- `list1`, `list2`는 실행 시점에는 같은 `List` 타입이다.
- 타입 소거법 장단점
  - 장점
    - 저장해야 하는 타입 정보의 크기가 줄어 전반적인 메모리 사용량이 줄어든다.
  - 단점
    - 실행 시점에 타입 인자를 검사할 수 없다.

    
코틀린는 raw 타입이 없어 어떤 값이 집합이나 맵이 아니라 리스트라는 사실을 raw 타입으로 확인할 수 없다. 이때 스타 프로젝션을 사용해 검사할 수 있다.
```kotlin
if (value is List<*>) { ... }
 ```

코틀린 컴파일러는 컴파일 시점에 타입 정보가 주어진 경우에는 `is` 검사를 수행하게 허용한다.
```kotlin
fun printSum(c: Collection<Int>) {
    if (c is List<Int>) {
        println(c.sum)
    }
}
```

### 실체화한 타입 파라미터를 사용한 함수 선언

인라인 함수를 사용하면 실행 시점에 인라인 함수의 타입 인자를 알 수 있다. 이를 타입 '파라미터가 실체화된다'고 한다.
```kotlin
fun <T> isA(value: Any) = value is T // compile error (타입 인자가 실행 시점에 소거된다.)

inline fun <reified T> isA(value: Any) = value is T // 성공 (타입 파라미터를 reified로 지정해야 한다.)
```

- Tip
  - 자바 코드에서는 `reified` 타입 파라미터를 사용하는 `inline` 함수를 호출할 수 없다.
    - 왜냐하면 자바는 인라인 함수를 보통 함수처럼 호출하기 때문이다.
  - `inline` 함수를 서능 향상이 아니라 실체화한 타입 파라미터를 사용하기 위해 사용할 때도 있다.

### 실체화한 타입 파라미터로 클래스 참조 대신

실체화한 타입 파라미터를 사용해 `java.lang.Class` 타입 인자를 파라미터로 받는 API에 대한 코틀린 어댑터를 구현할 수 있다.

ver1
```kotlin
val serviceImpl = ServiceLoader.load(Service::class.java) // ::class.java 는 코틀린 클래스에 대응하는 java.lang.Class 참조를 얻는 방법이다.
```

ver2
```kotlin
inline fun <reified T> loadService() {
    return ServiceLoader.load(T::class.java)
}

val serviceImple = loadService<Service>()
```

### 실체화한 타입 파라미터의 제약

실체화한 타입 파라미터에는 몇 가지 제약이 있다. 일부는 실체화의 개념으로 생기는 제약이고, 나머지는 코틀린이 실체화를 구현하는 방식에 의해 생기는 제약이다.

- 실체화한 타입 파라미터를 사용할 수 있다.
  - 타입 검사와 캐스팅 (`is`, `!is`, `as`, `as?`)
  - 코틀린 리플랙션 API(`::class`)
  - 코틀린 타입에 대응하는 `java.lang.Class`를 얻기
  - 다른 함수를 호출할 때 타입 인자로 사용

- 실체화한 타입 파라미터를 사용할 수 없다.
  - 타입 파라미터 클래스의 인스턴스 생성하기
  - 타입 파마리터 클래스의 동반 객체 메소드 호출하기
  - 실체화한 타입 파라미터를 요구하는 함수를 호출하면서 실체화하지 않은 타입 파라미터로 받은 타입을 타입 인자로 넘기기
  - 클래스, 프로퍼티, 인라인 함수가 아닌 함수의 타입 파라미터를 `reified`로 지정하기 (== 인라인 함수에만 `reified`사용 가능)

## 9.3 변성: 제네릭과 하위 타입

변성 : `List<String>`와 `List<Any>`와 같이 기저 타입이 같고 타입 인자가 다른 여러 타입이 서로 어떤 관계가 있는지 설명하는 개념

변성을 잘 활용하면 사용에 불편하지 않고 타입 안전성을 보장하는 제네릭 클래스나 함수를 정의할 수 있다.

### 변성이 있는 이유: 인자를 함수에 넘기기

`List<Any>` 타입의 파라미터를 받는 함수에 `List<String>`을 넘기면 안전할까? 

`String`이 `Any`의 하위 타입이기 때문에 안전할 것 같지만 그렇지 않다. 함수가 읽기 전용 리스트를 받는다면 더 구체적인 타입의 원소를 갖는 리스트를
그 함수에 넘길 수 있다. 하지만 리스트가 변경 가능하다면 그럴 수 없다.

읽이 전용인 경우 - 타입 안전
```kotlin
fun printContents(list: List<Any>) {
    println(list.joinToString())
}

>> printContents(listOf("abc", "bac")) // abc, bac
```

쓰기 - 타입 안전 X
```kotlin
fun addAnswer(list: MutableList<Any>) {
    list.add(42)
}

addAnswer(mutableListOf("abc", "bac")) // compile error
```

변성이라는 개념을 잘 이해해야 제네릭 클래스, 함수를 유연하고 타입 안전하게 사용할 수 있다.

### 클래스, 타입, 하위 타입

- 타입과 클래스
  - 타입과 클래스는 같지 않다.
  - 모든 코틀린 클래스가 적어도 둘 이상의 타입을 구성할 수 있다.
    - `var x: String`, `var x: String?`
    - 제네릭 클래스는 무수히 많은 타입을 만들어낼 수 있다.
      - `List<Int>`, `List<String?>`, `List<List<String>>` ...

- 하위 타입 : 어떤 타입 A의 값이 필요한 장소에 어떤 타입 B의 값을 넣어도 아무 문제가 없다면 타입 B는 타입 A의 하위 타입이다.
  - 간단한 경우 하위 타입은 하위 클래스와 근본적으로 같다.
    - 복잡한 경우
      - 널이 될 수 없는 타입은 널이 될 수 있는 타입의 하위 타입이다.
      - 제네릭
        - 무공변 : 제네릭 타입을 인스턴스화할 때 타입 인자로 서로 다른 타입이 들어가면 인스턴스 사이의 하위 타입 관계가 성립하지 않은 경우.
        - 공변적 : A가 B의 하위 타입일때 타입 인자 A를 받은 제네릭 클래스가 B를 타입 인자로 받은 제네릭 클래스의 하위 타입인 경우.