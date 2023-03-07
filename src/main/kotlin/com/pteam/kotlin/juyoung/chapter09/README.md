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

### 실체화한 타입 파라미터를 사용한 함수 선언
* 인라인 함수에서만 실체화한 타입 인자를 쓸 수 있는 이유
  * 컴파일러는 인라인 함수의 본문을 구현한 바이트코드를 그 함수가 호출되는 모든 지점에 삽입
  * 컴파일러는 실체화한 타입 인자를 사용해 인라인 함수를 호출하는 각 부분의 정확한 타입 인자를 알 수 있음
  * 따라서 컴파일러는 타입 인자로 쓰인 구체적인 클래스를 참조하는 바이트코드를 생성해 삽입할 수 있음
* 인라인 함수에는 실체화한 타입 파라미터가 여럿 있거나 실체화한 타입 파라미터와 셀체화하지 않은 타입 파라미터가 함께 있을 수 있음
* 여기서 인라인으로 함수를 만드는 이유는 성능 향상이 아닌 실체화한 타입 파라미터를 사용하기 위함임
```kotlin
inline fun <reified T> isA(value: Any) = value is T
println(isA<String>("abc"))
println(isA<String>(123))

// filterIsInstance 표준 라이브러리 함수 사용하기
val items = listOf("one", 2, "three")
println(items.filterIsInstance<String>())

// filterIsInstance를 간단하게 정리한 버전
inline fun <reified T> // reified 키워드는 이 타입 파라미터가 실행 시점에 지워지지 않음을 표시
        Iterable<*>.filterIsInstance(): List<T> {    
    val destination = mutableListOf<T>()
    for(element in this) {
        if(element is T) {  // 각 원소가 타입 인자로 지정한 클래스의 인스턴스인지 검사할 수 있음
            destination.add(element)
        }
    }
    return destination
}
```

### 실체화한 타입 파라미터로 클래스 참조 대신
* java.lang.Class타입 인자를 파라미터로 받는 API에 대한 코틀린 어댑터와 같은 경우 실체화한 타입 파라미터를 활용해 쉽게 호출
```kotlin
val serviceImpl = ServiceLoader.load(Service::class.java)

val serviceImpl2 = loadService<Service>()
inline fun <reified T> loadService() {
    return ServiceLoader.load(T::class.java)
}
```

### 실체화한 타입 파라미터의 제약
* 실체화의 개념으로 인해 생기는 제약과 코틀린이 실체화를 구현하는 방식에 의해 생기는 제약(향후 완화될 가능성 있음)
* 실체화한 티입 파라미터를 사용할 수 있는 경우
  * 타입 검사와 캐스팅(is, !is, as, as?)
  * 코틀린 리플렉션 API(::class)
  * 코틀린 타입에 대응하는 java.lang.Class를 얻기(::class.java)
  * 다른 함수를 호출할 때 타입 인자로 사용
* 사용할 수 없는 경우
  * 타입 파라미터 클래스의 인스턴스 생성
  * 타입 파라미터 클래스의 동반 객체 메소드 호출
  * 실체화한 타입 파라미터를 요구하는 함수를 호출하면서 실체화하지 않은 타입 파라미터로 받은 타입을 타입 인자로 넘기기
  * 클래스, 프로퍼티, 인라인 함수가 아닌 함수의 타입 파라미터를 reified로 지정
  * 실체화한 타입 파라미터를 인라인 함수에만 사용할 수 있으므로 실체화한 타입 파라미터를 사용하는 함수는 자신에게 전달되는 모든 람다와 함께 인라이닝됨

## 변성: 제네릭과 하위 타입
* 변성이란 기저 타입이 같고 타입 인자가 다른 여러 타입이 서로 어떤 관계가 있는지 설명하는 개념

### 변성이 있는 이유: 인자를 함수에 넘기기
* 원소 추가나 변경이 없는 경우에는 안전하지만, 추가/변경을 한다면 타입 불일치가 생겨 안전하지 않음

### 클래스, 타입, 하위 타입
* 타입과 클래스는 다름
* 코틀린 클래스가 적어도 둘 이상의 타입을 구성 (ex: var x:String, var s:String?)
* 제네릭 클래스는 무수히 많은 타입을 만들어 냄
  * List는 타입이 아니지만 List<Int>, List<String?> 등 모두 제대로 된 타입
* 타입 사이의 관계를 논하기 위해 하위 타입의 개념을 알아야 함
  * 하위 타입: A타입에 B타입의 값을 넣어도 문제가 없으면 타입B는 타입A의 하위 타입
    * Int는 Number의 하위 타입이지만 String의 하위 타입은 아님
  * 한 타입이 다른 타입의 하위 타입인지가 중요한 이유 -> 컴파일러는 변수 대입이나 함수 인자 전달 시 하위 타입 검사를 매번 수행
  * 어떤 인터페이스를 구현하는 클래스의 타입은 그 인터페이스의 하위 타입
  * 널이 될 수 있는 타입은 하위 타입과 하위 클래스가 같이 않은 경우를 보여주는 예
    * A는 A?의 하위 타입, Int는 Int?의 하위 타입. 그 반대는 하위 타입이 될 수 없음
  * 무공변: 제네릭 타입을 인스턴스화할 때 타입 인자로 서로 다른 타입이 들어가면 인스턴스 타입 사이의 하위 타입 관계가 성립하지 않음
  * 공변적: A가 B의 하위 타입이면 `List<A>`는 `List<B>`의 하위 타입. 이런 클래스나 인터페이스를 공변적이라 함
```kotlin
fun test(i: Int) {
    val n: Number = i   // Int가 하위타입이라 컴파일됨
  
    fun f(s:String) {}
    f(i)    // String의 하위 타입이 아니므로 컴파일 안 됨
}
```

### 공변성: 하위 타입 관계를 유지
* 코틀린에서 제네릭 클래스가 타입 파라미터에 대해 공변적임을 표시하려면 타입 파라미터 이름 앞에 out을 넣어야 함

```kotlin
interface Producer<out T> { // 클래스가 T에 대해 공변적이라고 선언
    fun produce(): T
}
```

* 클래스의 타입 파라미터를 공변적으로 만들면 함수 정의에 사용한 파라미터 타입과 타입 인자의 타입이 정확히 일치하지 않더라도 그 클래스의 인스턴스를 함수 인자나 반환값으로 사용할 수 있음
```kotlin
open class Animal {
    fun feed() {}
}

class Herd<T: Animal> { // 이 타입의 파라미터를 무공변성으로 지정
    val size: Int get() = {}
    operator fun get(i: Int): T {}
}

fun feedAll(animals: Herd<Animal>) {
    for (i in 0 until animals.size) {
        animals[i].feed()
    }
}

class Cat: Animal() {
    fun cleanLitter() {}
}

fun takeCareOfCats(cats: Herd<Cat>) {
    for (i in 0 until cats.size) {
        cats[i].cleanLitter()
    }

    feedAll(cats)   // 타입 불일치 오류 발생
}
```
* 모든 클래스를 공변적으로 만들 수 없음
  * 타입 파라미터를 공변적으로 지정하면 클래스 내부에서 그 파라미터를 사용하는 방법을 제한함
* 타입 안전성을 보장하기 위해 공변적 파라미터는 항상 아웃 위치에만 있어야 함 -> 클래스가 T타입의 값을 생산할 수는 있지만 소비할 순 없다는 뜻
  * 공변성: 하위 타입 관계가 유지됨
  * 사용 제한: T를 아웃 위치에서만 사용할 수 있음
* T가 반환 타입이면 아웃 위치에 있고 값을 생산(produce)
* T가 파라미터 타입에 쓰인다면 인 위치에 있고 값을 소비(consume)
```kotlin
class Herd<out T: Animal> {} // T는 공변적

fun takeCareOfCats(cats: Herd<Cat>) {
  for (i in 0 until cats.size) {
    cats[i].cleanLitter()
  }

  feedAll(cats)   // 캐스팅 할 필요가 없음
}
```

### 반공변성: 뒤집힌 하위 타입 관계
* 공변성을 거울에 비친 상, 공변 클래스와 반대

| **공변성**                               | **반공변성**                                   | **무공변성**             |
|:--------------------------------------|:-------------------------------------------|----------------------|
| `Producer<out T>`                       | `Consumer<in T>`                           | `MutableList<T>`       |
| 타입 인자의 하위 타입 관게가 제네릭 타입에서도 유지         | 타입 인자의 하위 타입 관계가 제네릭 타입에서 뒤집힘              | 하위 타입 관계가 성립하지 않음    |
| `Producer<Cat>`은 `Producer<Animal>`의 하위타입 | `Consumer<Animal>`은 `Consumer<Cat>`의 하위 타입 ||
| T를 아웃 위치에서만 사용할 수 있음                  | T를 인 위치에서만 사용할 수 있음                        | T를 아무 위치에서나 사용할 수 있음 |

### 사용 지점 변성: 타입이 언급되는 지점에서 변성 지정
* 선언 지점 변성: 클래스를 선언하면서 변성을 지정하면 그 클래스를 사용하는 모든 장소에 변성 지정자가 영향을 끼치므로 편리
* 사용 지점 변성: 타입 파라미터가 있는 타입을 사용할때마다 해당 타입 파라미터를 하위 타입이나 상위 타입 중 어떤 타입으로 대치할 수 있는지 명시해야 함 

### 스타 프로젝션: 타입 인자 대신 * 사용
* `MutableList<*>`는 `MutableList<Any?>`와 같지 않음
* `MutableList<Any?>`: 모든 타입의 원소를 담을 수 있음
* `MutableList<*>`: 어떤 정해진 구체적인 타입의 원소만은 담는 리스트지만 그 원소의 타입을 정확이 모른다는 사실을 표현
  * 그렇다고 아무 원소나 담아도 되는 건 아님
* 타입 파라미터를 시그니처에서 전혀 언급하지 않거나 데이터를 읽기는 하지만 그 타입에는 관심이 없는 경우와 같이 타입 인자 정보가 중요하지 않을 때도 스타 프로젝션 구문을 사용할 수 있음