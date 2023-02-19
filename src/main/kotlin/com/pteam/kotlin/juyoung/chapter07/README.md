# 연산자 오버로딩과 기타 관례

## 산술 연산자 오버로딩

### 이항 산술 연산 오버로딩

* 연산자를 오버로딩하는 함수 앞에 operator 키워드를 꼭 붙여야 함

```kotlin
data class Point(val x: Int, val y: Int) {
    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }
}

val p1 = Point(10, 20)
val p2 = Point(30, 40)
println(p1 + p2)    // +로 계산하면 plus가 호출된다 -> a + b -> a.plus(b)

// 연산자를 멤버 함수 대신 확장함수로 정의 가능
operator fun Point.plus(other: Point): Point {
    return Point(x + other.x, y + other.y)
}
```

* 오버로딩 가능한 이항 산술 연산자
    * 연산자 우선 순위는 언제나 표준 숫자 타입에 대한 연산자 우선순위와 같음

| **식** | **함수 이름*       |
|:------|:---------------|
| a * b | times          |
| a / b | div            |
| a % b | mod(1.1부터 rem) |
| a + b | plus           |
| a - b | minus          |

* 두 피연산자의 타입은 같지 않아도 됨

```kotlin
operator fun Point.times(scale: Double): Point {
    return Point((x * scale).toInt(), (y * scale).toInt())
}

val p = Pooint(10, 20)
println(p * 1.5)    // 1.5 * p 라고 쓰려면 대응되는 연산자 함수를 정의해야 함
```

* 연산자의 반환 타입이 두 피연산자 중 하나와 일치해야만 하는 것도 아님

```kotlin
operator fun Char.times(count: Int): String {
    return toString().repeat(count)
}
```

### 복합 대입 연산자 오버로딩

* +=, -= 등의 연산자가 복합 대입 연산자
* 코틀린 표준 라이브러리는 변경 가능한 컬렉션에 대해 plusAssign을 정의함
* +와 -는 항상 새로운 컬렉션을 반환
* +=와 -= 연산자는 항상 변경 가능한 컬렉션에 작용해 메모리에 있는 객체 상태를 변화시킴
* 읽기 전용 컬렉션에서 +=와 -=는 변경을 적용한 복사본을 반환

```kotlin
var point = Point(1, 2)
point += Point(3, 4)
println(point)
```

```kotlin
operator fun <T> MutableCollection<T>.plusAssign(element: T) {
    this.add(element)
}
```

```kotlin
val list = arrayListOf(1, 2)
list += 3   // +=는 list를 변경
val newList = list + listOf(4, 5)   // +는 두 리스트의 모든 원소를 포함하는 새로운 리스트 반환
```

### 단항 연산자 오버로딩

* 미리 정해진 이름의 함수를 선언하면서 operator로 표시
* 단항 연산자를 오버로딩하기 위해 사용하는 함수는 인자를 취하지 않음

```kotlin
operator fun Point.unaryMinus(): Point {    // 단항 minus 함수는 파라미터가 없음
    return Point(-x, -y)    // 좌표에서 각 성분의 음수를 취한 새 점을 반환
}
```

* 오버로딩할 수 있는 단항 산술 연산자

| 식        | 함수 이름      |
|:---------|:-----------|
| +a       | unaryPlus  |
| -a       | unaryMinus |
| !a       | not        |
| ++a, a++ | inc        |
| --a, a-- | dec        |

## 비교 연산자 오버로딩

### 동등성 연산자: equals

* ==, != 연산자 호출을 equals 메소드 호출로 컴파일
* 인자가 널인지 검사 -> 널이 될 수 있는 값에서 적용 가능
    * a == b에서 a가 널이라면 b도 널일 때만 결과가 true
* 식별자 비교 연산자 ===는 오버로딩 불가

### 순서 연산자: compareTo

* a >= b -> a.compareTo(b) >= 0

```kotlin
class Person(
    val firstName: String,
    val lastName: String,
) : Comparable<Person> {
    override fun compareTo(other: Person): Int {
        return compareValuesBy(this, other, Person::lastName, Person::firstName)
    }
}

val p1 = Person("Alice", "Smith")
val p2 = Person("Bob", "Johnson")
println(p1 < p2)
```

## 컬렉션과 범위에 대해 쓸 수 있는 관례

### 인덱스로 원소에 접근: get과 set

* 인덱스 연산자를 사용해 원소를 읽는 연산은 get 연산자 메소드로 변환되고, 쓰는 연산은 set 으로 변환됨

```kotlin
// get
operator fun Point.get(index: Int): Int {
    return when (index) {
        0 -> x
        1 -> y
        else -> throw IndexOutOfBoundsException("Invalid coordinate $index")
    }
}

val p = Point(10, 20)
println(p[1])

// set
data class MutablePoint(var x: Int, var y: Int)

operator fun MutablePoint.set(index: Int, value: Int) {
    when (index) {
        0 -> x = value
        1 -> y = value
        else -> throw IndexOutOfBoundsException("Invalid coordinate $index")
    }
}

val p = MutablePoint(10, 20)
p[1] = 42
println(p)
```

### in 관례

* in 연산자와 대응하는 함수는 contains
* 열린 범위(until)는 끝 값을 포함하지 않음
    * 10 until 20 -> 10이상, 19이하로 20은 포함x

```kotlin
data class Rectangle(val upperLeft: Point, val lowerRight: Point)

operator fun Rectangle.contains(p: Point): Boolean {
    return p.x in upperLeft.x until lowerRight.x &&
            p.y in upperLeft.y until lowerRight.y
}

val rect = Rectangle(Point(10, 20), Point(50, 50))
println(Point(20, 20) in rect)   // true
println(Point(5, 5) in rect)   // flase
```

### rangeTo 관례

* .. 연산자는 rangeTo 함수 호출로 컴파일됨

```kotlin
val now = LocalDate.now()
val vacation = now()..now.plusDays(10)
println(now.plusWeeks(1) in vacation)   // 특정 날짜가 날짜 범위 안에 들어가는지 검사
```

### for 루프를 위한 iterator 관례

* 코틀린에서는 iterator 메소드를 확장 함수로 정의 할 수 있기 때문에 자바 문자열에 대한 for 루프가 가능

```kotlin

import java.time.LocalDate

operator fun CharSequence.iterator(): CharIterator
for (c in "abc") {
}

operator fun ClosedRange<LocalDate>.iterator(): Iterator<LocalDate> =
    object : Iterator<LocalDate> {  // 이 객체는 LocalDate원소에 대한 Iterator를 구현
        var current = start
        ovveride
        fun hashNext() = current <= endInclusive    // compareTo 관례를 사용해 날짜를 비교
        ovveride
        fun next() = current.apply {    // 현재 날짜를 저장한 다음에 날짜를 변경
            current = plusDays(1)   // 현재 날짜를 1일 뒤로 변경
        }
    }

val newYear = LocalDate.ofYearDay(2017, 1)
val daysOff = newYear.minusDays(1)..newYear
for (dayOff in daysOff) {
    println(dayOff)
}
```

## 구조 분해 선언과 component 함수

```kotlin
val p = Point(10, 20)
val (x, y) = p  // p의 컴포넌트로 초기화
println(x)  // 10
println(y)  // 20

data class NameComponents(
    val name: String,
    val extension: String
)

fun splitFilename(fullName: String): NameComponents {
    val result = fullName.split('.', limit = 2)
    return NameComponents(result[0], result[1])
}

val (name, ext) = splitFilename("example.kt")
println(name)
println(ext)
```

### 구조 분해 선언과 루프

```kotlin
fun printEntries(map: Map<String, String>) {
    for ((key, value) in map) {  // 루프 변수에 구조 분해 선언
        println("$key -> $value")
    }
}
val map = mapOf("Oracle" to "Java", "JetBrains" to "Kotlin")
printEntries(map)
```

## 프로퍼티 접근자 로직 재활용: 위임 프로퍼티

### 위임 프로퍼티 소개

* p 프로퍼티는 접근자 로직을 다른 객체에게 위임
* 프로퍼티 위임 관례를 따르는 Delegate 클래스는 getValue와 setValue 메소드를 제공해야 함

```kotlin
class Foo {
    private val delegate: Deletate()
    var p: Type
        set(value: Type) = detegate.setValue( /*..*/, value)
        get() = delegate.getValue(/*..*/)
}
```

### 위임 프로퍼티 사용: by lazy()를 사용한 프로퍼티 초기화 지연

* 지연 초기화는 객체의 일부분을 초기화하지 않고 남겨뒀다가 실제로 그 부분의 값이 필요할 경우 초기화할 때 흔히 쓰이는 패턴

```kotlin
class Person(val name: String) {
    private var _emails: List<Email>? = null    // 데이터를 저장하고, emails의 위임 객체 역할을 하는 _emails 프로퍼티
    val emails: List<Email>
        get() {
            if (_emails == null) {
                _emails = loadEmails(this)  // 최초 접근 시 이메일 가져옴
            }
            return _emails!!    // 저장해둔 데이터가 있으면 그 데이터를 반환
        }
}

val p = Person("Alice")
p.emails    // 최초로 읽을 때 단 한번만 이메일을 가져옴
p.emails
```

* 위임 객체를 반환하는 표준 라이브러리 함수 lazy
    * 기본적으로 스레드세이프

```kotlin
class Person(val name: String) {
    val emails by lazy { loadEmails(this) }
}
```