# Chapter08 고차 함수: 파라미터와 반환 값으로 람다 사용

고차 함수를 사용하면 코드를 더 간결하게 다듬고 코드 중복을 없앨 수 있다.

## 8.1 고차 함수 정의

고차 함수 : 다른 함수를 인자로 받거나 함수를 반환하는 함수다.

### 함수 타입

함수 타입 정의 방법 : 함수 파라미터의 타입을 괄호 안에 넣고, 그 뒤에 화살표를 추가한 다음, 함수의 반환 타입을 지정하면 된다.
```kotlin
val sum: (Int, Int) -> Int = { x, y -> x + y}
```

### 인자로 받은 함수 호출

인자로 받은 함수를 호출하는 구문은 일반 함수를 호출하는 구문과 같다.

간단한 버전의 `filter` 함수
```kotlin
fun String.filter(predicate: (Char) -> Boolean): String {
    val sb = StringBuilder()
    for (index in 0 until length) {
        val element = get(index)
        if (predicate(element)) sb.append(element)
    }
    return sb.toString()
}
```

- Tip
    - IntelliJ IDEA는 디버깅할 때 람다 코드 내부를 한 단계씩 실행해볼 수 있는 스마트 스테핑(smart stepping)을 제공한다.

### 자바에서 코틀린 함수 타입 사용

- 컴파일된 코드 안에서 함수 타입은 일반 인터페이스로 바뀐다.
  - 함수 타입은 `FunctionN` 인터페이스를 구현하는 객체를 저장한다.
- 함수 타입을 사용하는 코틀린 함수를 자바에서도 쉽게 호출할 수 있다.
  - 자바 8이상에서는 람다를 넘기면 된다.
  - 자바 8이전에서는 `FunctionN` 인터페이스의 `invoke` 메서드를 구현하는 무명 클래스를 넘기면 된다.
- 반환 타입이 `Unit`인 함수 타입의 파라미터 위치에 `void`를 반환하는 자바 람다를 넘길 수 없다.
  - `Unit` 타입에는 값이 존재하기 때문이다.

### 디폴트 값을 지정한 함수 타입 파라미터나 널이 될 수 있는 함수 타입 파라미터

어떤 함수를 호출할 때마다 매번 람다를 넘기게 만들면 기본 동작으로도 충분한 대부분의 경우 함수 호출을 오히려 더 불편하게 만들 수 있다.
이런 문제는 디폴트 값을 지정하면 해결할 수 있다.

```kotlin
fun<T> Collection<T>.joinToString(
    separator: String = ", ",
    prefix: String = "",
    postfix: String = "",
    transform: (T) -> String = { it.toString() }
) : String {
    val result = StringBuilder(prefix)
    
    for ((index, element) in this.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(transform(element))
    }
    result.append(postfix)
    return result.toString()
}
```

함수 타입도 널이 될 수 있는 타입을 사용할 수 있다.
```kotlin
fun<T> Collection<T>.joinToString(
    separator: String = ", ",
    prefix: String = "",
    postfix: String = "",
    transfor: ((T) -> String)? = null
): String {
    val result = StringBuilder(prefix)
    
    for ((index, element) in this.withIndex()) {
        if (index > 0) result.append(separator)
        val str = transform?.invoke(element)
            ?: element.toString()
        result.append(str)
    }
    result.append(postfix)
    return result.toString()
}
```

### 함수를 함수에서 반환

다른 함수를 반환하는 함수를 정의하려면 함수의 반환 타입으로 함수 타입을 지정하면 된다.
```kotlin
enum class Delivery { STANDARD, EXPEDITED}
class Order(val itemCount: Int)
fun getShippingCostCalculator(
    delivery: Delivery) (Order) -> Double {
    if (delivery == Delivery.EXPEDITED) { 
        return { order -> 6 + 2.1 * order.itemCount }
    }
    return { order -> 1.2 * order.itemCount }     
}
```

### 람다를 활용한 중복 제거

함수 타입과 람다 식은 재활용하기 좋은 코드를 만들 때 쓸 수 있는 훌륭한 도구다.

예시 : 웹사이트 방문 기록 분석

```kotlin
data class SiteVisit(
  val path: String,
  val duration: Double,
  val os: OS
)
enum Class OS { WINDOWS, LINUX, MAC, IOS, ANDROID }
val log = listOf(
  SiteVisit("/", 34.0, OS.WINDOWS),
  SiteVisit("/", 22.0, OS.MAC),
  SiteVisit("/login", 12.0, OS.WINDOWS),
  SiteVisit("/signup", 8.0, OS.IOS),
  SiteVisit("/", 16.3, OS.ADROID),
)
```

평균 방문시간 출력 - ver1
```kotlin
val averageWindowsDuration = log
  .filter { it.os == OS.WINDOWS }
  .map(SiteVisit::duration)
  .average()
```

위 코드를 람다를 활용한 확장 함수를 이용하면 더 범용적이게 만들 수 있다.
```kotlin
fun List<SiteVisit>.averageDurationFor(predicate: (SiteVisit) -> Boolean) =
    filter(predicate).map(SiteVisit::duration).average()
```

## 8.2 인라인 함수: 람다의 부가 비용 없애기

`inline` 변경자를 어떤 함수에 붙이면 컴파일러는 그 함수를 호출하는 모든 문장을 함수 본문에 해당하는 바이트코드로 바꿔치기 해준다. 이는 람다를 사용해도 
자바의 일반 명령문만큼 효율적인 코드를 생성하게 해준다.

### 인라인이 작동하는 방식

어떤 함수를 `inline`으로 선언하면 함수를 호출하는 코드를 함수를 호출하는 바이트코드 대신에 함수 본문을 번역한 바이트 코드로 컴파일한다.

sample
```kotlin
inline fun<T> synchronized(lock: Lock, action: () -> T): T {
    lock.lock()
    try {
        return action()
    }
    finally {
        lock.unlock()
    }
}

fun foo(l : Lock) {
    println("Before sync")
    synchronized(l) {
        println("Action")
    }
    println("After sync")
}

/* compiled code */
fun __foo__(l: Lock) {
    println("Before sync")
    l.lock()
    try {
        print("Action")
    } finally {
        l.unlock()
    }
    println("After sync")
}
```
- 함수의 본문뿐 아니라 `inline`함수에 전달된 람다의 본문도 함께 인라인된다.
- 람다를 넘기는 대신에 함수 타입을 넘기면 람다 본문은 인라이닝되지 않는다.
  - 인라인 함수를 코드 위치에서 변수에 저장된 람다의 코드를 알 수 없기 때문이다.

### 인라인 함수의 한계

인라이닝을 하는 방식으로 인해 람다를 사용하는 모든 함수를 인라이닝할 수는 없다. 
- 가능
  - 함수 본문에서 파라미터로 받은 람다를 호출
- 불가능
  - 파라미터로 받은 람다를 다른 변수에 저장하고 나중에 그 변수를 사용
    - 람다를 표현하는 객체가 어딘가에 존재해야 하기 때문에 람다를 인라이닝할 수 없다.

둘 이상의 람다를 인자로 받는 함수에서 일부 람다만 인라이닝할 수 있다. 잉ㄴ라이닝을 하면 안 되는 람다에 `noinline` 변경자를 붙이면된다.
```kotlin
inline fun foo(inlined: () -> Unit, noinline notInlined: () -> Unit) { /* ... */ }
```

자바에서 코틀린에서 정의한 인라인 함수를 호출하는 경우 컴파일러는 인라이닝하지 않고 일반 함수 호출로 컴파일한다.

### 컬렉션 연산 인라이닝

코틀린 라이브러리의 컬렉션 함수는 대부분 람다를 인자로 받는다. 그리고 대부분 `inline` 함수다. 따라서 코틀린다운 연산을 컬렉션에 대해 안전하게 사용할 수 있고,
코틀린이 제공하는 함수 인라이닝을 믿고 성능에 신경 쓰지 않아도 된다.

- 주의
  - 시퀀스는 람다를 인라인하지 않는다. 따라서 크기가 작은 컬렉션은 오히려 일반 컬렉션 연산이 더 성능이 나을 수 있다.
    - Why?
      - 시퀀스는 람다를 필드에 저장하는 객체로 표현되며, 최종 연산은 중간 시퀀스에 있는 여러 람다를 여러 호출한다. 이런 구현 방식 때문에 람다를 인라인할 수 없다.

### 함수를 인라인으로 선언해야 하는 경우

코드를 더 빠르게 만들고 싶다고 `inline` 키워드를 남용하지 말자. `inline` 키워드는 람다를 인자로 받는 함수에서만 성능이 좋아질 가능성이 높다.
일반 호출의 경우 JVM이 이미 강력하게 인라이닝을 지원한다. 즉, JVM이 코드 실행을 분석해 가장 이익이 되는 방향으로 호출을 인라이닝한다. 반면 람다를
인라이닝해 줄 정도로 똑똑하지는 못하다.

- Tip
  - `inline` 변경자를 함수에 붙일 때는 코드 크기에 주의를 기울여야 한다.
    - 바이트코드를 모든 호출 지점에 복사하기 때문에 인라이닝하는 함수가 큰 경우 바이트코드가 전체적으로 아주 커질 수 있다.

### 자원 관리를 위해 인라인된 람다 사용

람다로 중복을 없앨 수 있는 일반적인 패턴 중 한 가지는 어떤 작업을 하기 전에 자원을 획득하고 작업을 마친 후 자원을 해제하는 자원 관리다. 따라서 코틀린은 
`try-with-resource` 같은 구문을 제공하지 않고, 함수 타입의 값을 파라미터로 받는 표준 라이브러리를 제공해준다.

코틀린 표준 라이브러리 `use` 사용
```kotlin
fun readFirstLineFormFile(path: String): String {
    BufferReader(FileReader(path)).use { br -> 
      return br.readLine()
    }
}
```

- `use` 함수는 닥을 수 있는 자원에 대한 확장 함수며, 람다를 인자로 받는다.
- 인라인 함수다.

## 8.3 고차 함수 안에서 흐름 제어

### 람다 안의 return문: 람다를 둘러싼 함수로부터 반환

non-local return : 자신을 둘러싸고 있는 블록보다 더 바깥에 있는 다른 블록을 반환하게 만드는 `return`문
```kotlin
fun lookForAlice(people: List<Person>) {
    people.forEach {
        if (it.name == "Alice") {
            println("Found!")
            return // non-local return문이다. 즉, lookForAlice 함수에서 반환된다.
        }
    }
    println("Alice is not found")
}
```
- `return`이 바깥쪽 함수를 반환시킬 수 있는 때는 람다를 인자로 받는 함수가 인라인 함수인 경우뿐이다.
  - Why?
    - 인라이닝 되지 않은 함수는 람다를 변수에 저장할 수 있고, 바깥쪽 함수로부터 반환된 뒤에 저장해 둔 람다가 호출될 수 있다.

### 람다로부터 반환: 레이블을 사용한 return

local return : 람다의 실행을 끝내고 람다를 호출했던 코드의 실행을 이어간다. `local return`과 `non-local return`을 구분하기 위해 레이블을 사용해야 한다.

레이블은 람다에 또는 인라인 함수의 이름을 사용할 수 있다. 단, 람다 식의 레이블을 명시하면 함수 이름을 레이블로 사용할 수 없다.

람다 레이블
```kotlin
fun lookForAlice(people: List<Person>) {
    people.forEach label@{
        if (it.name == "Alice") return@label
    }
    println("Alice might be somewhere")
}
```

함수 이름 레이블
```kotlin
fun lookForAlice(people.List<Person>) {
    people.forEach {
        if (it.name == "Alice") return@forEach
    }
    println("Alice might be somewhere")
}
```

### 무명 함수: 기본적으로 로컬 return

무명 함수 : 코드 블록을 함수에 넘길 때 사용할 수 있는 다른 방법. 람다 식에 대한 문법적 편의

```kotlin
fun lookForAlice(people: List<Person>) {
    people.forEach(fun (person) {
        if (person.name == "Alice") return
        println("${person.name} is not Alice")
    })
}
```
- 무명 함수는 함수의 이름이나 파라미터 타입을 생략할 수 있다.
- 무명 함수 안에서 레이블이 붙지 않은 `return` 식은 무명 함수 자체를 반환시킨다.

## Sample Code Subject
- ~~doing performance test using inline modifier~~
- doing performance test sequence vs normal collection