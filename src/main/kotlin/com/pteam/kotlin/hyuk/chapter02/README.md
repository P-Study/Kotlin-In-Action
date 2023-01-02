# Chapter02

## 2.1 함수와 변수

### 함수

코틀린에서는 2가지 방식으로 함수를 표현할 수 있다.

```kotlin
// 블록이 본문인 함수
fun max(a: Int, b: Int): Int {
    return if (a > b) a else b
}

// 식이 본문인 함수
fun max(a: Int, b: Int) = if (a > b) a else b
```

코틀린에서는 식이 본문인 함수가 자주 쓰인다.

식이 본문인 함수는 반환 타입을 적지 않아도 컴파일러가 타입추론이 가능하다.
하지만 블록이 본문인 함수는 만약 값을 반환한다면 반드시 반환 타입과 return문을 사용해야 한다. 이유는 실전에서는 이 방식이 가독성이 더 좋기 때문이다.

### 변수

선언 시 2가지 키워드를 사용한다.

val (value) : 변경 불가능한 참조를 저장하는 변수다.
var (variable) : 변경 가능한 참조를 저장한다.

### 문자열 템플릿

자바의 문자열 접합 연산과 동일한 기능을 하지만 좀 더 간결하다. 문자열 접합 연산을 사용한 식과 마찬가지로 효율적이다. (컴파일된 코드는 StringBuilder를 사용)

```kotlin
val name = "PTEAM"
println("Hello, {$name}")
```

Tip
템플릿 안에서 변수 이름만 사용하는 경우라도 ${name} 처럼 중괄호로 감싸는 습관을 들이면 더 좋다. (가독성 증가)

## 클래스와 프로퍼티

### 클래스

코틀린에서는 클래스를 간결하게 만들 수 있다.

Java Class
```java
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

Kotlin Class
```kotlin
class Person(val name: String)
```

코틀린의 기본 가시성은 public이다.

### 프로퍼티

프로퍼티는 자바에서 필드와 접근자를 묶은 개념이다. 코틀린은 프로파티를 언어 기본 기능으로 제공한다.

```kotlin
class Person(
    val name: String, // 읽기 전용 프로퍼티(필드, (공개)getter)
    var isMarried: Boolean // 쓸 수 있는 프로퍼티(필드, (공개)setter, (공개)getter)
)
```

대부분의 프로퍼티에는 값을 저장하기 위한 필드가 있고 이를 backing field라고 부른다.

### 커스텀 접근자

backing field대신 값을 그때그때 계산할 수 있다. 이는 커스텀 접근자를 이용해 구현할 수 있다.

```kotlin
class Rectangle(val height: Int, val width: Int) {
    val isSquare: Boolean
        get() {
            return height == width
        }
}
```

- 파라미터가 없는 함수를 정의하는 방식 vs 커스텀 게터 정의하는 방식 차이점
  - 구현이나 성능상 차이는 없다. 차이나는 부분은 가독성뿐이다.
  - 일반적으로 클래스의 특성을 정의하고 싶다면 커스텀 게터를 사용하자.

### 코틀린의 소스코드 구조
코틀린은 자바보다 훨씬 자유롭게 소스코드 구조를 설계할 수 있다. 하지만 자바와 같이 패키지별로 디렉터리를 구성하는게 좋다.

## enum과 when