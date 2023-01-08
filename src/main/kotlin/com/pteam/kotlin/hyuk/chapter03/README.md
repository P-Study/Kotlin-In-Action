# Chapter 03 함수의 정의와 호출

함수의 정의와 호출 기능을 코틀린이 어떻게 개선했는지 살펴본다.

## 3.1 코틀린에서 컬렉션 만들기

코틀린은 표준 자바 컬렉션을 활용한다. 즉, 자신만의 컬렉션을 제공하지 않는다. 하지만 코틀린에서는 자바보다 더 많은 기능을 사용할 수 있다.

- 코틀린이 자바 컬렉션을 활용하는 이유
    - 표준 자바 컬렉션을 활용하면 자바 코드와 상호작용하기 훨씬 쉽다.
- 더 많은 기능을 사용할 수 있는 방법
    - 확장 함수

```kotlin
val set = hashSetOf(1, 7, 53)

println(set.javaClass) // class java.util.HashSet
```

## 3.2 함수를 호출하기 쉽게 만들기

### 이름 붙인 인자

자바에서 함수의 인자가 많아지면 가독성이 급격하게 떨어진다. 코틀린에서는 '이름 붙인 인자' 기능을 통해 떨어지는 가독성을 개선했다.

```kotlin
/* Java */
JoinToString(list, " ", " ", ".")

/* Kotlin */
JoinToString(list, seperator = " ", prfix = " ", postfix = ".")
```

Tip : 호출 시 인자 중 어느 하나라도 이름을 명시하고 나면 혼동을 막기 위해 그 뒤에 오는 모든 인자는 이름을 꼭 명시해야 한다.

### 디폴트 파라미터 값

자바에서는 일부 클래스에서 overloading한 메서드가 너무 많아지는 문제가 종종 발생한다. 코틀린에서는 이런 문제를 해결하기 위해 디폴트 파라미터 값을 지원한다.

```kotlin
fun <T> joinToString(
    collection: Collection<T>,
    separator: String = ", ",
    prefix: String = "",
    postfix: String = ""
): String
```

일반 호출 문법을 사용하려면 함수를 선언할 때와 같은 순서로 인자를 지정해야 한다. 그런 경우 일부를 생략하면 뒷부분의 인자들이 생략된다.

이름 붙인 인자를 사용하는 경우에는 인자 목록의 중간에 있는 인자를 생략하고, 지정하고 싶은 인자를 이름을 붙여서 순서와 관계없이 지정할 수 있다.

```kotlin
joinToString(list) // separator, prefix, postfix 생략

joinToString(list, postfix = ";", prefix = "# ") // separator 생략
```

### 최상위 함수와 프로퍼티

#### 최상위 함수

자바에서는 모든 코드를 클래스 메서드로 작성해야 한다. 하지만 실전에서는 어느 한 클래스에 포함시키기 어려운 코드가 많이 생긴다. 
그 결과 다양한 정적 메소드를 모아두는 역할만 담당하며, 특별한 상태나 인스턴스 메소드가 없는 클래스가 생겨난다. (ex : Collections)

코틀린에서는 이런 무의미한 클래스가 필요없다. 함수를 직접 소스 파일의 최상위 수준에 위치시키면 된다.

```kotlin
package string

fun joinToString(...): String { ... }
```

위처럼 선언해도 JVM이 클래스 안에 들어있는 코드만을 실행할 수 있기 때문에 컴파일러는 이 파일을 컴파일할 떄 새로운 클래스를 정의해준다.

보통 코틀린 소스 파일의 이름과 대응하는 클래스를 선언한다. (ex: join.kt -> JoinKt.class) 하지만 @JvmName 애노테이션을 활용하면
최상위 함수가 포함되는 클래스 이름을 바꿀 수 있다. 

#### 최상위 프로퍼티

프로퍼티도 파일의 최상위 수준에 놓을 수 있다. 

```kotlin
val UNIX_LINE_SEPARATOR = "/n"
```

겉으로는 상수처럼 보이지만 실제로는 getter를 사용하는게 자연스럽지 않다. 이때는 const 변경자를 추가하면 public static final 필드로 컴파일하게 할 수 있다.

```kotlin
const val UNIX_LINE_SEPARATOR = "/n"
```

## 3.3 메소드를 다른 클래스에 추가: 확장 함수와 확장 프로퍼티

### 확장 함수

코틀린의 핵심 목표 중 하나는 기존 자바 코드와 코틀린 코드를 자연스럽게 통합하는 것이다. 이를 위해서는 기존 자바 API를 재작성하지 않고도
코틀린이 제공하는 여러 편리한 기능을 사용할 수 있는 방법이 필요하다. 이런 방법을 제공하는게 확장 함수다.

확장 함수 : 어떤 클래스의 맴버 메소드인 것처럼 호출할 수 있지만 그 클래스의 밖에 선언된 함수.

```kotlin
package strings

fun String.lastChar(): Char = this.get(this.length - 1) // String을 수신 객체 타입, this를 수신 객체라고 한다.

println("Kotlin".lastChar())
```

- 확장 함수가 캡슐화를 깨지 않을까?
  - 확장 함수는 비공개 맴버나 보호된 맴버를 사용할 수 없기 때문에 캡슐화를 깨지 않는다.

#### 임포트와 확장 함수

확장 함수를 사용하기 위해서는 당연히 임포트해야만 한다. 가끔씩 한 클래스에 같은 이름의 확장 함수가 둘 이상 있어 충돌하는 경우가 생길 수 있다.
이때는 as 키워드를 사용해 해결할 수 있다. 그리고 이것이 유일한 해결책이다..

```kotlin
import strings.lastChar as last

val c = "Kotlin".last()
```

#### 자바에서 확장 함수 호출

내부적으로 확장함수는 수신 객체를 첫 번째 인자로 받는 정적 메소드다.

```java
char c = StringUtilKt.lastChar("Java");
```

#### 확장 함수로 유틸리티 함수 정의

확장 함수는 단지 정적 메소드 호출에 대한 문법적 편의일 뿐이다. 따라서 더 구체적인 타입을 수신 객체 타입으로 지정할 수도 있다. 

```kotlin
fun Collection<String>.join(...) : String {...}
```

#### 확장 함수는 오버라이드 할 수 없다.

이름과 파라미터가 완전히 같은 확장 함수를 기반 클래스와 하위 클래스에 대해 정의해도 실제로는 *확장 함수를 호출할 때 수신 객체로 지정한 변수의 정적 타입에 의해
어떤 확장 함수가 호출될지 결정되지, 그 변수에 저장된 객체의 동적 타입에 의해 확장 함수가 결정되지 않는다.*

```kotlin
open class View
class Button: View()

fun View.showOff() = println("I'm a view!")
fun Button.showOff() = println("I'm a button!")

val view: View = Button()
view.showOff() // I'm a view (확장 함수는 정적으로 결정된다.)
```

#### 확장 함수 vs 맴버 함수

확장 함수와 맴버 함수의 시그니처가 같다면 확장 함수가 아니라 항상 맴버 함수가 호출된다.

### 확장 프로퍼티

확장 프로퍼티는 상태를 저장할 방법이 없기 때문에(기존 클래스의 인스턴스 객체에 필드를 추가할 방법은 없음) 실제로 확장 프로퍼티는 아무 상태도 가질 수 없다.
따라서 뒷바침하는 필드가 없어서 최소한 게터는 꼭 정의해야 한다.

```kotlin
val String.lastChar: Char
    get() = get(length - 1)
```

## 3.4 컬렉션 처리: 가변 길이 인자, 중위 함수 호출, 라이브러리 지원

