# 3. 함수 정의와 호출
- 코틀린에서 모든 함수 정의와 호출 기능을 어떻게 개선했는지 알려준다

## 3.1 코틀린에서 컬렉션 만들기
- 자바 코드와의 상호작용이 쉽다
- 컬렉션 정의를 코틀린 만의 방식으로 만들기 쉽고 간결하게 개선했다
```kotlin
// set.javaClass = class java.util.HashSet
val set = hashSetOf(1, 2, 3, 4)
// list.javaClass = class java.util.ArrayList
val list = arrayListOf(1, 2, 3, 4, 5)
// map.javaClass = class java.util.HashMap
val map = hashMapOf(1 to "ONE", 2 to "TWO", 3 to "THREE")
```

## 3.2 함수를 호출하기 쉽게 만들기
```kotlin
// 리스트 3.1 joinToString() 함수의 초기 구현
fun <T> joinToString(
    collection: Collection<T>
    , separator: String
    , prefix: String
    , postfix: String
) : String {
    val result = StringBuilder(prefix)

    for ((index, element) in collection.withIndex()) {
        if (index > 0) {
            result.append(separator)
        }
        result.append(element)
    }

    result.append(postfix)
    return result.toString()
}
```
### 3.2.1 이름 붙인 인자
- 인자가 많을 때 혹은 같은 타입의 인자가 나열되어 있을 때 함수 시그니처를 보지 않고는 무엇인지 판단하기 어렵다
- 인자의 이름을 붙여 함수 시그니처를 보지 않아도 어떤 인자가 있는지 확인이 가능해진다. 
```kotlin
joinToString(list, separator = "; ", prefix = "(", postfix =  ")")
```

### 3.2.2 디폴트 파라미터 값
- 오버로딩은 하위 호환성을 유지하거나 API 사용자에게  편의를 더하는 등의 여러가지 이유로 만들어진다.
- 다만, 오버로딩은 코드 중복을 초래한다
- 오버로딩의 코드 중복을 파라미터의 디폴트를 설정하여 해결할 수 있다라고 내용에 나와있지만 사실 부가기능이 있기 때문에 해결이 가능하다고 생각한다
- 디폴트 파라미터의 부가 기능은 파라미터를 생략할 수 있는 것이다.
- 생략 규칙은 아래와 같다
  1. 이름 붙인 인자를 사용하는 경우에는 인자 목록의 중간에 인자를 생략
  2. 지정하고 싶은 인자를 이름을 붙여서 순서와 관계없이 지정(지정하지 않은 인자는 생략)
```kotlin
fun <T> joinToString(
    collection: Collection<T>
    , separator: String = ", "
    , prefix: String = ""
    , postfix: String = ""
) : String {
}
```

### 3.2.3 정적인 유틸리티 클래스 없애기: 최상위 함수와 프로퍼티
- 자바에서는 모든 코드를 클래스의 메소드로 작성했었다
- 하지만 클래스에 포함시키기 어려운 코드들이 많이 생긴다. (아직까지 경험해보진 못함)
- 코틀린에서는 의미 없는 클래스가 필요하지 않고 소스 파일 최상위 수준에 메소드를 정의할 수 있다
- 컴파일러는 최상위 메소드를 컴파일할 때 새로운 클래스에 정의해준다. (쓸데없는 생각 : 같은 패키지안에 새로운 클래스로 생길 클래스명으로 만든 클래스가 있다면?)
```kotlin
import java.lang.StringBuilder

fun String.lastChar() : Char = this.get(this.length - 1)
```
- 최상위 프로퍼티도 가능하다
- 주로 상수를 추가할 때 많이 사용될 것으로 보인다.
  - 변수 키워드에 const 키워드를 붙여 사용한다 (단, 원시타입과 String만 가능)
```kotlin
const val UNIX_LINE_SEPARATOR = "\n"
```

## 3.3 메소드를 다른 클래스에 추가: 확장 함수와 확장 프로퍼티
- 기존 코드와 코틀린 코드를 자연스럽게 통합하는 것은 코틀린의 핵심 목표 중 하나인데 그 역할을 해주는 것이 확장 함수이다.
- 확장 함수는 어떤 클래스의 멤버 메소드인 것처럼 호출할 수 있지만 그 클래스 밖에 선언된 함수이다.
- 확장 함수는 수신 객체 타입과 수신 객체로 구성된다.

### 3.3.1 임포트와 확장 함수
- 확장 함수를 사용하기 위해서는 사용하는 곳에서 임포트를 해야 한다
```kotlin
import string.lastChar

val c = "Kotlin".lastChar()
```
- (\*)를 통해 string 패키지에 포함되어 있는 클래스, 메소드 사용이 가능하다.
```kotlin
import string.*

val c = "Kotlin".lastChar()
```
- 이름이 같은 함수를 가져와서 사용할 경우 import 시 alias를 설정하여 충돌을 방지한다.
```kotlin
import string.lastChar as last

val c = "Kotlin".last()
```

### 3.3.2 자바에서 확장 함수 호출
- 내부적으로 확장 함수는 수신 객체를 첫 번째 인자로 받는 정적 메소드이다.
- 정적 메소드이기 때문에 함소 호출을 해도 실행 시점에 부가 비용이 들지 않는다.
  - 부가 비용이란 객체 생성 비용, 캡슐화된 객체 접근 비용 등이 있다.
```java
char c = StringUtilKt.lastChar("Java");
```

### 3.3.3 확장 함수로 유틸리티 함수 정의
```kotlin
fun Collection<String>.join(
  separator: String = ", "
  , prefix: String = ""
  , postfix: String = ""
) : String = joinToString(separator, prefix, postfix)
```

### 3.3.4 확장 함수는 오버라이드할 수 없다
- 수신 객체로 지정한 변수의 정적 타입에 의해 어떤 확장 함수가 호출될지 결정되지, 그 변수에 저장된 객체이 동적인 타입에 의해 확장 함수가 결정되지 않는다.
- 확장 함수와 멤버 함수의 시그니처가 같다면 확장 함수가 아닌 멤버 함수가 호출된다
- 그렇기에, 확장 함수는 Util 클래스처럼 관련있는 소스 파일에 모아두고 사용하는 방향이 낫지 않을까 싶다.
```kotlin
open class View {
    open fun click() = println("View clicked")
    fun View.showOff() = println("I'm a view!")
}

class Button : View() {
    override fun click() = println("View clicked")
    fun Button.showOff() = println("I'm a button!")
}
```
### 3.3.5 확장 프로퍼티
- 기존 클래스 객체에 대한 프로퍼티 형식 구문으로 사용 가능하다
- 프로퍼티 문법으로 더 짧게 코드를 작성할 수 있다.
```kotlin
val String.lastChar: Char
    get() = get(length - 1)

var StringBuilder.lastChar: Char
    get() = get(length - 1)
    set(value: Char) {
        this.setCharAt(length -1, value)
    }
```

## 3.4 컬렉션 처리: 가변 길이 인자, 중위 함수 호출, 라이브러리 지원
### 3.4.1 자바 컬렉션 API 확장
- 코틀린은 확장 함수를 통해 자바의 컬렉션 API를 확장해서 사용한다.

### 3.4.2 가변 인자 함수: 인자의 개수가 달라질 수 있는 함수 정의
- vararg 키워드를 사용하면 호출 시 인자 개수가 달라질 수 있는 함수를 정의할 수 있다.
```kotlin
val list = listOf(2, 3, 4, 5, 6, 7, 8) // 가변이란 걸 인지를 못하고 있었음...
```

### 3.4.3 값의 쌍 다루기: 중위 호출과 구조 분해 선언
- 중위(infix) 함수 호출 구문을 사용하면 인자가 하나뿐인 메소드를 간편하게 호출할 수 있다.
- 구조 분해 선언을 사용하면 복합적인 값을 분해해서 여러 변수에 나눠 담을 수 있다.
```kotlin
val map = mapOf(1 to "one", 7 to "seven", 53 to "fifty-three")

fun destructuringDeclaration() {
  for ((index, element) in list.withIndex()) {
    println("$index, $element")
  }
}
```

## 3.5 문자열과 정규식 다루기
- 다양한 확장 함수를 제공함으로써 표준 자바 문자열을 즐겁게 다루게 해준다(즐겁다라... 확장 함수가 갓이네..)

### 3.5.1 문자열 나누기
```kotlin
println("12.345-6.A-바".split("\\.|-".toRegex()))

println("12.345-6.A-바".split(".", "-"))
```

### 3.5.2 정규식과 3중 따옴표로 묶음 문자열
- 정규식을 사용하지 않고도 문자열을 쉽게 파싱할 수 있다. 정규식은 강력하기는 하지만 나중에 알아보기 힘든 경우가 많다(극공감)
```kotlin
fun parsePath(path: String) : String {
    val directory = path.substringBeforeLast("/")
    val fullName = path.substringAfterLast("/")
    val fileName = fullName.substringBeforeLast(".")
    val extension = fullName.substringAfterLast(".")
    return "$directory/$fileName.$extension"
}
```
- 정규식 구현도 가능하긴 합니다
- 정규식 엔진은 각 패턴을 가능한 한 가장 긴 부분 문자열과 매칭시킨다
```kotlin
fun parsePathForRegex(path: String) : String? {
  val regex = """(.+)/(.+)\.(.+)""".toRegex()
  val matchResult = regex.matchEntire(path)
  if (matchResult != null) {
    val (directory, fileName, extension) = matchResult.destructured
    return "$directory/$fileName.$extension"
  }

  return null
}
```

## 3.6 코드 다듬기: 로컬 함수와 확장
```kotlin
class User(val id: Int, val name: String, val address: String)
fun saveUser(user: User) {
    user.validateBeforeSave()
}

fun saveUserRefactoring(user: User) {
    fun validate(value: String, fieldName: String) {
        if (value.isEmpty()) {
            throw IllegalArgumentException("Can't save user 쿄쿄쿄쿄쿄쿄쿄 $(user.id): $fieldName")
        }
    } 
    
    validate(user.name, "Name")
    validate(user.address, "Address")
}

fun User.validateBeforeSave() {
    fun validate(value: String, fieldName: String) {
        if (value.isEmpty()) {
            throw IllegalArgumentException("Can't save user 쿄쿄쿄쿄쿄쿄쿄 $(user.id): $fieldName")
        }
    }

    validate(name, "Name")
    validate(address, "Address")
}
```