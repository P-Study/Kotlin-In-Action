# Chapter07 연산자 오버로딩과 기타 관례

**관례**

어떤 언어 기능과 미리 정해진 이름의 함수를 연결해주는 기법

**관례 도입 이유**

기존 자바 클래스를 코틀린 언어에 적용하기 위해서. 기존 자바 클래스에 확장 함수를 구현하면서 관례에 따라 이름을 붙이면 자바 코드를
바꾸지 않아도 새로운 기능을 쉽게 부여할 수 있다.

## 7.1 산술 연산자 오버로딩

자바는 산술 연산자를 원시 타입, `String`에 대해서만 적용할 수 있다. 하지만 다른 클래스에서도 산술 연산자가 유용한 경우가 있다.
코틀린에서는 이를 관례를 이용해 해결했다.

### 이항 산술 연산 오버로딩

```kotlin
data class Point(val x: int, val y: Int) {
    operator fun plus(other: Point) : Point {
        return Point(x + other.x, y + other.y)
    }
}

>>> val p1 = Point(10, 20)
>>> val p2 = Point(30, 40)
>>> print(p1 + p2)
Point(x=40, y=60)
```
- 연산자를 오버로딩하는 함수 앞에는 꼭 `operator`가 있어야 한다.
- p1 + p2 는 실제로 p1.plus(p2)로 컴파일된다.
- 외부 함수의 클래스에 대한 연산자를 정의할 때는 관례를 따르는 이름의 확장 함수로 구현하는 게 일반적인 패턴이다.
- 연산자 우선순위는 표준 숫자 타입에 대한 연산자 우선순위와 같다.
- 교환 법칙을 지원하지 않는다.
- 두 피연산자가 같은 타입일 필요가 없다.
- 반환 타입이 두 피연산자 타입과 일치하지 않아도 된다.
- operator 함수도 오버로딩할 수 있다.

오버로딩 가능한 이항 산술 연산자

| 식 | 함수 이름 |
|--------|----------|
| a * b | times |
| a / b | div |
| a % b | mod(1.1부터 rem) |
| a + b | plus |
| a - b | minus |

### 복합 대입 연산자 오버로딩

복합 대입 연산자 : `+=`, `-=` 등의 연산자

- `a += b`는 이론적으로 `a = a.plus(b)` 또는 `a.plusAssign(b)` 양쪽으로 컴파일 할 수 있다. (나머지 연산자도 동일하다.)
  - `plus`와 `plusAssign` 연산을 동시에 정의하지 마라
- 컬렉션에서 `+`, `-`는 항상 새로운 컬렉션을 반환하며, `+=`, `-=`는 항상 변경 가능한 컬렉션에 적용해 메모리에 있는 객체 상태를 변화시킨다.
  - 읽기 전용 컬렉션에서는 `+=`, `-=`는 변경을 적용한 복사본을 반환한다.

### 단항 연산자 오버로딩

단항 연산자
```kotlin
operator fun Point.unaryMinus(): Point {
    return Point(-x, -y)
}

>>> val p = Point(10, 20)
>>> print(-p)
Point(x=-10, y=-20)
```

오버로딩 할 수 있는 단항 산술 연산자

| 식 | 함수 이름 |
|---| ---|
| +a | unaryPlus |
| -a | uanryMinus |
| !a | not |
| ++a, a++ | inc |
|--a, a-- | dec |

- Tip
  - 증가/감소 연산자를 오버로딩하는 경우 컴파일러는 일반적인 값에 대한 전위와 후위 증가/감소 연산자와 같은 의미를 제공한다.

## 7.2 비교 연산자 오버로딩

`equals`나 `compareTo`를 호출해야 하는 자바와 달리 코틀린에서는 `==` 비교 연산자를 직접 사용할 수 있다.

### 동등성 연산자: equals

- `a == b`는 `a?.equals(b) ?: (b == null)`로 컴파일된다.
  - 내부에서 인자가 널인지 검사하므로 다른 연산과 달리 널이 될 수 있는 값에도 적용할 수 있다.
- `===`는 오버로딩할 수 없다.
- `Any`에서 상속 받은 `equals`가 확장 함수보다 우선순위가 높기 때문에 `equals`를 확장 함수로 정의할 수 없다.

### 순서 연산자: compareTo

compareTo 메소드 구현
```kotlin
class Person(
    val firstName: String, val lastName: String
) : Comparable<Person> {
    override fun compareTo(other: Person): Int {
        return compareValueBy(this, other, Person::lastName, Person::firstName)
    }
}

>>> val p1 = Person("Alice", "Smith")
>>> val p2 = Person("Bob", "Johnson")
>>> println(p1 < p2)
false
```
- `a >= b`는 `a.compareTo(b) >= 0`으로 컴파일된다.
- 코틀린도 자바와 같은 `Comparable` 인터페이스를 지원한다.
  - 자바의 컬렉션 정렬 메소드 등에서 사용할 수 있다.
- 코틀린 표준 라이브러리 `compareValuesBy` 함수를 사용해 `compareTo`를 쉽고 간결하게 정의할 수 있다.
  - 필드를 직접 비교하면 코드는 조금 더 복잡해지지만 비교 속도는 훨씬 더 빨라진다. (처음에는 쉽고 간결한 코드를 작성하고 후에 성능을 개선하는게 좋다.)

    
## Sample Code Subject
- 컬렉션 `+`,`-`와 `+=`, `-=` 차이 확인
- `compareValuesBy` 함수와 필드 직접 비교 성능차이 테스트

