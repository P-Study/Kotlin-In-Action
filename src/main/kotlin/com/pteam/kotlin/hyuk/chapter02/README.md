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

2가지 키워드가 있다.

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

### Enum

Enum 선언 방법
```kotlin
enum class Color(
  val r: Int, val g: Int, val b: Int
) {
    RED(255, 0, 0), ORANGE(255, 165, 0),
    YELLOW(255, 255, 0), GREEN(0, 255, 0), BLUE(0, 0, 255),
    INDIGO(75, 0, 130), VIOLET(238, 130, 238);
  
    fun rgb() = (r * 256 + g) * 256 + b;
}
```

kotlin에서 enum은 soft keyword다. 따라서 enum이 class 앞에 있을 때는 특별한 의미를 갖지만 다른 곳에서는 이름에 사용할 수 있다.

### When

when 구문은 자바의 switch를 대신한다. 하지만 훨씬 더 강력하고 코틀린에서 자주 사용되는 프로그래밍 요소이다.

Case 1 : enum에서 when 사용
```kotlin

 fun getWarmth(color: Color) = when(color) {
    Color.RED, Color.ORANGE, Color.YELLOW -> "warm"
    Color.GREEN -> "neutral"
    Color.BLUE, Color.INDIGO, Color.VIOLET -> "cold"
}
```

when은 switch와 달리 분기조건에 임의의 객체도 사용할 수 있다.

Case 2 : 임의의 객체응 이용한 when
```kotlin
fun mix(c1: Color, c2: Color) =
    when (setOf(c1, c2)) {
        setOf(RED, YELLOW) -> ORANGE
        setOf(YELLOW, BLUE) -> GREEN
        setOf(BLUE, VIOLET) -> INDIGO
        else -> throw Exception("Dirty color")
    }
```
객체 사이를 매치할 때는 동등성(equality)를 사용한다.

추가로 when은 분기 조건 부분에 식을 넣을 수 있고, 인자가 없을 수도 있다. 덕분에 코드를 더욱 간결하고 아름답게 작성할 수 있다.

위에서 본 것 처럼 when은 switch와 다르게 훨씬 강력하다. 따라서 if식도 when과 비교해보고 대신할 수 있으면 리팩터링하자.

### 스마트 캐스트

kotlin에서는 변수가 어떤 타입인지 is로 검사하고 나면 굳이 변수를 원하는 타입으로 캐스팅하지 않아도 된다. 왜냐하면 컴파일러가 자동으로 캐스팅해준다. 이를 스마크 캐스트라고 한다.

```kotlin
if (e is Sum) {
    return eval(e.right) + eval(e.left) // e가 자동으로 Sum 타입으로 캐스팅된다.
}
```

## while과 for 루프

### while

java와 동일하다.

### for 루프

#### 범위

kotlin은 java의 for 루프(for (int i = 0; i < n; i++))에 해당하는 요소가 없다. 대신 '범위'라는 개념을 사용한다.

Case 1 : 범위를 사용한 기본적인 for loop
```kotlin
for (i in 1..100) {
    /* i : 1 -> 2 -> 3 -> ... -> 100 */
}
```
kotlin에서 '..'은 폐구간이다.

Case 2 : 증가값 커스텀화
```kotlin
for (i in 100 downTo 1 step 2) {
    /* i : 100 -> 98 -> 96 -> ... -> 2 */
}
```
step을 이용해 증가 값을 조절할 수 있다.

범위의 개념에서 아쉬운 부분이 폐구간이라는 점이다. 이는 until 함수를 이용해 반만 닫힌 범위를 만들 수 있다.

Case 3 : 반만 닫힌 범위
```kotlin
for(x in 0 until size) { // [0, size - 1]을 순회한다.
    /* Do Something */
}
```

kotlin 1.7.20 부터 '..<' 연산자가 나왔다. 이는 until 함수와 동일한 기능을한다.

#### collection iteration 구조 분해 구문

일반적으로 collection 순회는 for .. in 루프를 자주쓴다. 따라서 자세한 설명은 생략한다.
대신 구조 분해 구문에 대해 알아보자.

Case 1 : 구조 분해 구문을 이용해 Map 순회
```kotlin
for ((letter, binary) in binaryReps) {
    println("${letter} = ${binary}")
}
```

구조 분해 구문은 맵이 아닌 컬렉션에서도 사용가능하다.

Case 2 : 리스트에서 구조 분해 구문 사용
```kotlin
for ((index, element) in list.withIndex()) {
    println("${index} : ${element}")
}
```

#### 범위의 원소 검사

in 연산자를 사용해 어떤 값이 범위에 속하는지 검사할 수 있다.

```kotlin
fun recognize(c: Char) = when (c) {
    in '0'..'9' -> "It's a digit!"
    in 'a'..'z', in 'A'..'Z' -> "It's a letter!"
    else -> "I don't know.."
}
```

범위는 문자에 국한되지 않고, 비교 가능한 클래스로도 범위를 만들 수 있다. 
하지만 항상 이터레이션하지는 못한다. 그래도 in 연산자를 사용하면 범위 안에 속하는지는 항상 결정할 수 있다. 

Case 1 : 이터레이션 불가, 범위 결정 가능
```kotlin
val result = ("Kotlin" in "Java".."Scala")
```

## 코틀린 예외 처리

Kotlin 예외는 Java와 비슷하다. 하지만 몇 가지 다른 점이 있다.

- 체크 예외와 언체크 예외를 구별하지 않는다. -> 예외 처리를 강제하지 하지 않는다.
  - Why? -> 예외 처리를 강제해도 프로그래머들이 의미 없이 예외를 다시 던지거나, 예외를 무시하는 경우가 흔하다.
- try-with-resource 구문을 제공하지 않는다.
- try를 식으로 사용할 수 있다.