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

## Sample Code Subject
- using IntelliJ IDEA smart stepping