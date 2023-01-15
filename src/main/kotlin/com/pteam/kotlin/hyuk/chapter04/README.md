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