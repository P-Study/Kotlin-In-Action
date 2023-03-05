# 제네릭스

## 제네릭 타입 파라미터

### 제네릭 함수와 프로퍼티
* 제네릭 확장 프로퍼티를 선언할 수 있음
* 확장 프로퍼티만 제네릭하게 만들 수 있음
  * 일반 프로퍼티는 타입 파라미터를 가질 수 없음(클래스 프로퍼티에 여러 값을 저장할 수 없으므로)

```kotlin
val <T> List<T>.penultimate: T  // 모든 리스트 타입에 제네릭 확장 프로퍼티를 사용 가능
    get() = this[size-2]

println(listOf(1, 2, 3, 4).penultimate)     // 타입 파라미터 T는 Int로 추론
```

### 제네릭 클래스 선언
* 꺾쇠 기호<>를 클래스 이름 뒤에 붙이면 제네릭하게 만들 수 있음

```kotlin
interface List<T> {
    operator fun get(index: Int): T
}
```

### 타입 파라미터 제약
* 클래스나 함수에 사용할 수 있는 타입 인자를 제한하는 기능
* 어떤 타입을 제네릭 타입의 타입 파라미터에 대한 상한으로 지정하면 그 제네릭 타입을 인스턴스화할 때 사용하는 타입 인자는 반드시 그 상한 타입이거나 그 상한 타입의 하위 타입이어야 함
* 제약을 가하려면 타입 파라미터 뒤에 콜론(:)을 표시하고 그 뒤에 상한 타입을 적으면 됨
* 
```kotlin
fun <T: Number> List<T>.sum(): T
println(listOf(1, 2, 3).sum())  // Int가 Number를 확장하므로 합법적
```

* 타입 파라미터를 제약하는 함수 선언하기
```kotlin
fun <T: Comparable<T>> max(first: T, second: T): T {
    return if (first > second) first else second
}

println(max("kotlin", "java"))
println(max("kotlin", 42))  // 비교할 수 없으면 컴파일 오류
```

* 타입 파라미터에 여러 제약 가하기
```kotlin
fun <T> ensureTrailingPeriod(seq: T) where T: CharSequence, T: Appendable {  // 타입 파라미터 제약 목록 
    if(!seq.endsWith('.')) {
        seq.append('.') 
    }
}

val helloWorld = StringBuilder("Hello World")
ensureTrailingPeriod(helloWorld)
println(helloWorld)
```

### 타입 파라미터를 널이 될 수 없는 타입으로 한정
* 제네릭 클래스나 함수를 정의하고 그 타입을 인스턴스화할 때는 널이 될 수 있는 타입도 타입 파라미터를 치환 가능
* 아무런 상한을 정하지 않은 타입 파라미터는 결과적으로 Any?를 상한으로 정한 파라미터와 같음
* 널이 될 수 없는 타입으로 타입 파라미터를 제약하면 널이 될 수 있는 타입이 인자로 들어오는 일을 막을 수 있음

```kotlin
class Processor<T> {
    fun process(value: T) {
        value?.hashCode()
    }
}

val nullableStringProcessor = Processor<String?>()
nullableStringProcessor.process(null)   // null이 value인자로 지정

// 널 가능성을 제외한 아무런 제약도 필요없을 경우 Any를 상한으로 사용
class Processor<T: Any> {
  fun process(value: T) {
    value.hashCode()
  }
}
```

## 실행 시 제네릭스의 동작: 소거된 타입 파라미터와 실체화된 타입 파라미터
* 함수를 inline으로 만들면 타입 인자가 지워지지 않게 할 수 있음(실체화)

### 실행 시점의 제네릭: 타입 검사와 캐스트
* 자바와 마찬가지로 코틀린 제네릭 타입 인자 정보는 런타임에 지워짐

```kotlin
// 실행 시점에 두 리스트는 문자열이나 정수의 리스트로 선언 됐다는 사실을 알 수 없고, 각 객체는 단지 List일 뿐임
val list1: List<String> = listOf("a", "b")
val list2: List<Int> = listOf(1, 2, 3)
```

* 타입소거로 인해 생기는 한계
  * 타입 인자를 따로 저장하지 않기 때문에 실행 시점에 타입 인자를 검사할 수 없음
  * 실행 시점에 어떤 값이 List인지 여부는 알 수 있지만, String의 리스트인지 Person의 리스트인지는 알 수 없음
  * 스타 프로젝션(*)을 사용해서 리스트라는 사실을 확인할 수 있음
```kotlin
// is 검사에서 타입 인자로 지정한 타입을 검사할 수 없음
if (value is List<String>) { }

// 인자를 알 수 없는 제네릭 타입을 표현할 때 스타 프로젝션 사용
if (value is List<*>) { }
```

* as나 as? 캐스팅에도 제네릭 타입 사용 가능
* 기저클래스는 같지만 타입 인자가 다른 타입으로 캐스팅해도 성공한다는 점에 유의
  * 컴파일러가 unchecked cast라는 경고를 할 뿐 컴파일을 진행

```kotlin
fun printSum(c: Collection<*>) {
    val intList = c as? List<Int>   // unchecked cast 경고 발생
      ?: throw IllegalArgumentException("List is expected")
    
    println(inLint.sum())
}

// 정수 리스트에 대해선 합계를 출력
printSum(listOf(1, 2, 3))
// 정수 집합에 대해선 IllegalArgumentException 발생
printSum(setOf(1, 2, 3))
// 잘못된 타입의 원소가 있는 리스트는 실행 시점에 ClassCastException이 발생 (as? 캐스팅은 성공하지만 나중에 다른 예외가 발생)
printSum(listOf("a", "b", "c"))
```

* 코틀린 컴파일러는 컴파일 시점에 타입 정보가 주어진 경우에는 is 검사를 수행하게 허용
```kotlin
fun printSum(c: Collection<Int>) {
    if (c is List<Int>) {
        println(c.sum())
    }
}

printSum(listOf(1, 2, 3))
```