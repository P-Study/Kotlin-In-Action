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

## 7.3 컬렉션과 범위에 대해 쓸 수 있는 관례

코틀린에서는 인덱스 연산자(`a[b]`)를 사용해 원소를 설정하거나 가져올 수 있고, `in` 연산자를 사용해 원소나 컬렉션이 범위에 속하는지
검사하거나 컬렉션에 있는 원소를 이터레이션할 수 있다.

### 인덱스로 원소에 접근: get과 set

get 관례 구현
```kotlin
operator fun Point.get(index: Int): Int {
    return when(index) {
        0 -> x
        1 -> y
        else -> throw IndexOutOfBoundsException("Invalid coordinate $index")
    }
}

>>> val p = Point(10, 20)
>>> println(p[1])
20
```
- `x[a, b]`는 `x.get(a, b)`로 컴파일된다.
- `get` 메소드의 파라미터로 `Int`가 아닌 타입도 사용할 수 있다. 예를 들어 맵은 키 타입을 사용한다.

set 관례 구현
```kotlin
data class MutablePoint(var x: Int, var y: Int)

operator fun MutablePoint.set(index: Int, value: Int) {
    when(index) {
        0 -> x = value
        1 -> y = value
        else -> throw IndexOutOfBoundsException("Invalid coordinate $index")
    }
}

>>> val p = MutablePoint(10, 20)
>>> p[1] = 42
>>> print(p)
MutablePoint(x=10, y=42)
```
- 'x[a, b] = c'는 `x.set(a, b, c)`로 컴파일된다.

### in 관례

`a in c`는 `c.contains(a)`로 컴파일된다.

### rangeTo 관례

`start..end`는 `start.rangeTo(end)`로 컴파일된다.

- 어떤 클래스가 `Comparable` 인터페이스를 구현하면 `rangeTo`를 정의할 필요가 없다.
  - 코틀린 표준 라이브러리에 모든 `Comparable` 객체에 대해 적용 가능한 `rangTo` 함수가 들어있다.
- `rangeTo` 연산자는 다른 산술 연산자보다 우선순위가 낮다.

### for 루프를 위한 iterator 관례

`for (x in list) { ... }`는 `list.iterator()`를 호출해서 이터레이터를 얻은 다음, 이터레이터에 대해 `hasNext`와 `next`
호출을 반복한다. 코틀린에서는 이또한 관례이다.

## 7.4 구조 분해 선언과 component 함수

구조 분해를 사용하면 복합적인 값을 분해해서 여러 다른 변수를 한꺼번에 초기화할 수 있다.

`val (a, b) = p`는 `val a = p.component1()` `val b = p.component2()`로 컴파일된다.

- Tip
  - 배열이나 컬렉션에도 componentN 함수가 있다.
    - 단, 맨 앞의 다섯 원소에 대해서만 존재한다.

### 구조 분해 선언과 루프

맵의 원소에 대해 이터레이션할 때 구조 분해 선언이 유용한다.
```kotlin
fun printEntires(map: Map<String, String>) {
    for ((key, value) in map) {
        println("$key -> $value")
    }
}
```
- 두 가지 관례를 사용한다.
  - 이터레이션 관례 (코틀린 표준 라이브러리는 멥에 대한 확장 함수로 `iterator`가 들어있다.)
  - 구조 분해 선언

## 7.5 프로퍼티 접근자 로직 재활용: 위임 프로퍼티

위임 프로퍼티를 사용하면 값을 뒷받침하는 필드에 단순히 저장하는 것보다 더 복잡한 방식으로 작동하는 프로퍼티를 쉽게 구현할 수 있다.

### 위임 프로퍼티 소개

```kotlin
class Foo {
    var p: Type by Delegate()
}
```
- p 프로퍼티는 접근자 로직을 다른 객체에게 위임한다.
  - 컴파일러는 숨겨진 도우미 프로퍼티를 만들고 그 프로퍼티를 위임 객체의 인스턴스로 초기화한다.

```kotlin
/* compiled code */
class Foo {
    private val delegate = Delegate()
    var p : Type
    set(value: Type) = delegate.setValue(..., value)
    get() = delegate.getValue(...)        
}
```
- 프로퍼티 위임 관례를 따르는 `Delegate` 클래스는 `getValue`와 `setValue` 메소드를 제공해야 한다.

### 위임 프로퍼티 사용: by lazy()를 사용한 프로퍼티 초기화 지연

지연 초기화 : 객체의 일부분을 초기화 하지 않고 남겨뒀다가 실제로 그 부분의 값이 필요한 경우 초기화할 때 흔히 쓰이는 패턴이다.

지연 초기화 직접 구현
```kotlin
class Person(val name: String) {
    private var _emails: List<Email>? = null
    val email: List<Email>
        get() {
            if (_emails == null) {
                _emails = loadEmail(this)
            }
          return _emails!!
        }
}
```
- Backing Property 기법을 사용한다.
  - `_emails` 프로퍼티는 값을 저장하고 `emails`는 `_emails`라는 프로퍼티에 대한 읽기 연산을 제공한다.
- 스레드 안전하지 않다.

위임 프로퍼티 이용해 리펙토링
```kotlin
class Person(val name: String) {
    val emails by lazy { loadEmails(this) }
}
```
- `lazy` 함수는 코틀린 관례에 맞는 시그니처의 `getValue` 메소드가 들어있는 객체를 반환한다.
- `lazy` 함수는 기본적으로 스레드 안전하다.

### 위임 프로퍼티 구현

위임 프로퍼티 구현 방식을 예제를 통해 확인해보자. 

상황 : 객체의 프로퍼티가 바뀔 때마다 리스너에게 변경 통지를 보낸다.

PropertyChangeSupport를 사용하기 위한 도우미 클래스
```kotlin
open class PropertyChangeAware {
  protected val changeSupport = PropertyChangeSupport(this)
  
  fun addPropertyChangeListener(listener: PropertyChangeListener) {
      changeSupport.addPropertyChangeListener(listener)
  }
  
  fun removePropertyChangeListener(listener: PropertyChangeListener) {
      changeSupport.removePropertyChangeListener(listener)
  }
}
```

프로퍼티 위임을 사용하는 Person 구현 (ver1)
```kotlin
class Person(
  val name: String, age: Int, salary: Int
) : PropertyChangeAware() {
    var age: Int = age
        set(newValue) {
            val oldValue = field
            field = newValue
            changeSupport.fireProperyChange(
              "age", oldValue, newValue
            )
        }
    
    var salary: Int = salary
        set(newValue) {
            val oldValue = field
            field = newValue
            changeSupport.firePropertyChange(
              "salary", oldValue, newValue
            )
        }
}
```
- 개선점
  - 세터에 코드 중복이 많다.

ver2 : 프로퍼티 값을 저장하고 필요에 따라 통지를 보내주는 클래스 추출
```kotlin
class ObservableProperty(
  val propName: String, var propValue: Int,
  val changeSupport: PropertyChangeSupport
) {
    fun getValue(): Int = propValue
    fun setValue(newValue: Int) {
        val oldValue = propValue
        propValue = newValue
        changeSupport.firePropertyChange(propName, oldValue, newValue)
    }
}

class Person(
  val name: String, age: Int, salary: Int
) : PropertyChangeAware() {
    val _age = ObservableProperty("age", age, changeSupport)
    var age: Int
        get() = _age.getValue()
        set(value) { _age.setValue(value) }
  
    val _salary = ObservableProperty("salary", salary, changeSupport)
    var salary: Int
        get() = _salary.getValue()
        set(value) { _salary.setValue(value) }
}
```
- 코틀린의 위임이 실제로 동작하는 방식과 비슷하다.
- 개선점
  - 각각의 프로퍼티마다 `ObservableProperty`를 만들고 게터와 세터에서 `ObservableProperty`에 작업을 위임하는 준비 코드가 필요하다.

ver3 : 코틀린 위임 프로퍼티 관례 사용
```kotlin
/* 관례에 맞게 수정 */
class ObservableProperty(
  val propValue: Int, val changeSupport: PropertyChangeSupport
) {
    operator fun getValue(p: Person, prop: KProperty<*>): Int = propValue
    operator fun setValue(p: Person, prop: KProperty<*>, newValue: Int) {
        val oldValue = propValue
        propValue = newValue
        changeSupport.firePropertyChange(prop.name, oldValue, newValue)
    }
}

class Person(
  val name: String, age: Int, salary: Int
) : PropertyChangeAware() {
    var age: Int by ObservableProperty(age, changeSupport)
    var salary: Int by ObservableProperty(salary, changeSupport)
}
```

### 위임 프로퍼티 컴파일 규칙

```kotlin
class C {
    var prop: Type by MyDelegate()
}

/* compiled code */
class C {
    private val <delegate> = MyDelegate()
    var prop: Type
    get() = <delegate>.getValue(this, <property>)
    set(value: Type) = <delegate>.setValue(this, <property>, value)        
}
```

### 프로퍼티 값을 맵에 저장

자신의 프로퍼티를 동적으로 정의할 수 있는 객체를 만들 때 위임 프로퍼티를 활용하는 경우가 자주 있다.

```kotlin
class Person {
    private val _attributes = hashMapOf<String, String>()
    fun setAttribute(attrName: String, value: String) {
        _attributes[attrName] = value
    }
    
    val name: String by _attributes // 표준 라이브러리가 Map과 MutableMap 인터페이스에 대해 getValue, setValue 확장함수를 제공한다. 
}
```

### 프레임워크에서 위임 프로퍼티 사용

위임 프로퍼티를 사용하면 객체 프로퍼티를 저장하거나 변경하는 방법을 바꿀 수 있고, 이는 프레임워크를 개발할 때 유용하다.

```kotlin
object Users : IdTable() { // 데이터베이스 테이블에 해당한다.
    val name = varchar("name", length=50).index() // 칼럼에 해당한다.
    val age = integer("age") //칼럼에 해당하낟.
}

operator fun<T> Column<T>.getValue(o: Entity, desc: KProperty<*>): T { ... }
operator fun<T> Column<T>.setValue(o: Entity, desc: KProperty<*>, value: T) { ... }

class User(id: EntityID) : Entity(id) {
    var name: String by Users.name
    var age: Int by Users.age
}
```

## Sample Code Subject
- ~~컬렉션 `+`,`-`와 `+=`, `-=` 차이 확인~~
- `compareValuesBy` 함수와 필드 직접 비교 성능차이 테스트

