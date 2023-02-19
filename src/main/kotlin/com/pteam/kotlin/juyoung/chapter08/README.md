# 고차 함수: 파라미터와 반환 값으로 람다 사용

## 고차 함수 정의
* 람다나 함수 참조를 인자로 넘길 수 있거나 람다나 함수 참조를 반환하는 함수

### 함수 타입
* 함수 타입을 선언할 때는 반환 타입을 반드시 명시해야 함 (Unit도 명시)
* 파라미터 이름은 타입 검사시 무시
* 람다에서 호출하는 바라미터 이름이 꼭 함수 타입 선언의 파라미터 이름과 일치하지 않아도 됨
* 가독성과 IDE 사용을 위해 함수 타입에 이름을 추가하는 것을 권장

```kotlin
// 타입 추론
val sum = { x: Int, y: Int -> x + y }
val action = { println(42) }

// 함수 타입 명시
val sum: (Int, Int) -> Int = { x, y -> x + y }
val action: () -> Unit = { println(42) }

// 널이 될 수 있는 반환 타입
var canReturnNull: (Int, Int) -> Int? = { x, y -> null }
// 함수 타입 변수 자체가 널이 될 수 있음
var funOrNull: ((Int, Int) -> Int)? = null

// 파라미터 이름과 함수 타입
fun performRequest (
    url: String,
    callback: (code: Int, content: String) -> Unit
)
```

### 인자로 받은 함수 호출
* 인자로 받은 함수를 호출하는 구문은 일반 함수를 호출하는 구문과 같음
```kotlin
fun twoAndThree(operation: (Int, Int) -> Int) { // 함수 타입인 파라미터 선언
    val result = operation(2, 3)    // 호출
    println("The result is $result")
}

twoAndThree { a, b -> a + b }
twoAndThree() { a, b -> a * b}
```

### 자바에서 코틀린 함수 타입 사용
* 컴파일된 코드 안에서 함수 타입은 일반 인터페이스로 바뀜
* 함수 타입의 변수는 FunctionN 인터페이스를 구현하는 객체를 저장
* 각 인터페이스에는 invoke 메소드 정의가 하나 들어있음
  * 함수 타입인 변수는 FunctionN 인터페이스를 구현하는 클래스의 인스턴스를 저장
  * 그 클래스의 invoke 메소드 본문에는 람다를 저장
* 코틀린 Unit 타입에는 값이 존재하므로 자바에서는 그 값을 명시적으로 반환해야 함

```kotlin
fun processTheAnswer(f: (Int) -> Int) {
    println(f(42))
}
```
```java
// 자바 8 에서
processTheAnswer(number -> number + 1)
        
// 자바 8 이전에서는 invoke 메소드를 구현하는 무명클래스를 넘겨주어야 함
processTheAnswer(
    new Function1<Integer, Integer>() {
        @Override 
        public Integer invoke(Integer number) {
            System.out.println(number);
            return number + 1;
        }
    });

List<String> strings = new ArrayList<>();
strings.add("42");
CollectionsKt.forEach(strings, s -> { // strings는 확장 함수의 수신 객체
        System.out.println(s)
        return Unit.INSTANCE; // Unit 타입의 값을 명시적으로 반환해야 함
});
```

### 디폴트 값을 지정한 함수 타입 파라미터나 널이 될 수 있는 함수 타입 파라미터
* 파라미터를 함수 타입으로 선언할 때도 디폴트 값을 정할 수 있음
```kotlin
fun <T> Collection<T>.joinToString (
  seperator: String = ", ",
  prefix: String = "",
  postfix: String = "",
  transform: (T) -> String = { it.toString() }  // 함수 타입 파라미터를 선언하면서 람다를 디폴트 값으로 지정
): String {
    val result = StringBuilder(prefix)
    
    for ((index, element) in this.withIndex()) {
        if(index > 0) result.append(separator)
        result.append(transform(element))  // transform 파라미터로 받은 함수를 호출
    }
    result.append(postfix)
    return result.toString()
}

val letters = listOf("A", "B")
println(letters.joinToString()) // 디폴트 반환 함수 사용
println(letters.joinToString { 
  seperator = "! ", 
  postfix = "! ", 
  transform = { it.uppercase() } // 이름 붙인 인자 구문을 사용해 람다를 포함하는 여러 인자를 전달
})
```

* 널이 될 수 있는 함수 타입으로 함수를 받으면 그 함수를 직접 호출할 수 없다는 점의 유의할 것
* 널 여부를 명시적으로 검사하는 것도 해결방법 중 하나
```kotlin
fun foo(callback: (() -> Unit)?) {
    // TODO
    if (callback != null){
        callback()
    }
}
```

* 일반 메소드처럼 invoke도 안전 호출 구문으로 callback?.invoke() 처럼 호출 가능
```kotlin
fun <T> Collection<T>.joinToString(
  separator: String = ", ",
  prefix: String = "",
  postfix: String = "",
  transform: ((T) -> String)? = null // 널이 될 수 있는 함수 타입
): String {
  val result = StringBuilder(prefix)
  for ((index, element) in this.withIndex()) {
    if (index > 0) result.append(separator)
    val str = transform?.invoke(element) ?: element.toString()
    result.append(str)
  }
  result.append(postfix)
  return result.toString()
}
```
### 함수를 함수에서 반환
* 다른 함수를 반환하는 함수를 정의하려면 함수의 반환 타입으로 함수 타입을 지정해야 함
```kotlin
enum class Delivery { STANDARD, EXPEDITED }
class Order (val itemCount: Int)
fun getShippingCostCalculator(delivery: Delivery): (Order) -> Double {  // 함수를 반환하는 함수를 선언
      if (delivery == Delivery.EXPEDITED) {
        return (order -> 6 + 2.1 * order.itemCount)   // 함수에서 람다를 반환
      }
    return (order -> 1.2 * order.itemCount) 
}

val calculator = getShippingCostCalculator(Delivery.EXPEDITED)
println("Shipping costs ${calculator(Order(3))}")
```

### 람다를 활용한 중복 제거
* 확장 함수를 정의하여 가독성 개선
* 함수 타입은 중복을 줄일 때 상당한 도움이 됨

```kotlin
data class SiteVisit(
  val path: String, 
  val duration: Double, 
  val os: OS
)

enum class OS { WINDOWS, LINUX, MAC, IOS, ANDROID }

val log = listOf(
    SiteVisit("/", 34.0, OS.WINDOWS), 
    SiteVisit("/", 22.0, OS.MAC),
    SiteVisit("/login", 12.0, OS.WINDOWS),
    SiteVisit("/signup", 8.0, OS.IOS),
    SiteVisit("/", 16.3, OS.ANDROID))

// 윈도우 사용자의 평균 방문 시간 가져오기
val averageWindowsDuration = log
  .filter { it.os == OS.WINDOWS }
  .map(SiteVisit::duration)
  .average()
println(averageWindowsDuration)

// OS별 사용자 평균 방문 시간 가져오는 확장 함수 정의
fun List<SiteVisit>.averageDurationFor(os: OS) = 
    filter { it.os }.map(SiteVisit::duration).average()

println(log.averageDurationFor(OS.WINDOWS))
println(log.averageDurationFor(OS.MAC))

// 하드코딩한 필터를 사용해 방문 데이터 분석하기
val averageMobileDuration = log.filter { it.os in setOf(OS.IOS, OS.ANDROID) }
                                .map(SiteVisit::duration)
                                .average()
println(averageMobileDuration)

// 고차 함수를 사용해 중복 제거하기
fun List<SiteVisit>.averageMobileDurationFor(predicate: (SiteVisit) -> Boolean) =
    filter(predicate).map(SiteVisit::duration).average()
```

## 인라인 함수: 람다의 부가 비용 없애기

### 인라이닝이 작동하는 방식
* 어떤 함수를 inline으로 선언하면 그 함수의 본문이 인라인됨
* 함수를 호출하는 코드를 함수를 호출하는 바이트코드 대신에 함수 본문을 번역한 바이트 코드로 컴파일한다는 뜻
* inline 변경자를 어떤 함수에 붙이면 컴파일러는 그 함수를 호출하는 모든 문장을 함수 본문에 해당하는 바이트코드로 바꿔치기 해줌
* 한 인라인 함수를 두 곳에서 각각 다른 람다를 사용해 호출하면 그 두 호출은 각각 따로 인라이닝 됨
* 인라인 함수의 본문 코드가 호출 지점에 복사되고 각 람다의 본문이 인라인 함수의 본문 코드에서 람다를 사용하는 위치에 복사됨
```kotlin
// 인라인 함수 정의하기
inline fun <T> synchronized(lock: Lock, action: () -> T): T {
    lock.lock()
    try {
        return action()
    } finally {
        lock.unlock()
    }
}
val l = Lock()
synchronized(l) {
    // TODO
}
```
### 인라인 함수의 한계
* 함수 본문에서 파라미터로 받은 람다를 호출한다면 그 호출을 쉽게 람다 본문으로 바꿀 수 있음
* 파라미터로 받은 람다를 다른 변수에 저장하고 나중에 그 변수를 사용한다면 람다를 표현하는 객체가 어딘가는 존재해야 하기 때문에 람다를 인라이닝할 수 없음
* 인라인 함수의 본분에서 람다 식을 바로 호출하거나 람다 식을 인자로 전달받아 바로 호출하는 경우에는 그 람다를 인라이닝할 수 있음
* 인라이닝하면 안 되는 람다를 파라미터로 받는다면 noinline 변경자를 파라미터 이름 앞에 붙여서 인라이닝을 금지할 수 있음
* 어떤 모듈이나 서드파티 라이브러리 안에서 인라인 함수를 정의하고 그 모듈이나 라이브러리 밖에서 해당 인라인 함수를 사용할 수 있음
```kotlin
inline fun foo(inline: () -> Unit, noinline notInlined: () -> Unit) { 
    // TODO
}
```

### 컬렉션 연산 인라이닝
* 시퀀스는 람다를 인라인하지 않음
* 지연 계산을 통해 성능을 향상시키려는 이유로 모든 컬렉션 연산에 asSequence 를 붙여서는 안 됨
* 작은 컬렉션은 오히려 일반 컬렌션 연산이 더 성능이 나을 수 있음
* 시퀀스를 통해 성능을 향상시킬 수 있는 경우는 컬렉션 크기가 큰 경우뿐임
```kotlin
data class Person(val name: String, val age: Int)
val people = listOf(Person("Alice", 33), Person("Bob", 20))
println(people.filter { it.age < 30 })

val result = mutableListOf<Person>()
for(person in people){
    if(person.age < 30) result.add(person)
}
println(result)
```

### 함수를 인라인으로 선언해야 하는 경우
* 일반 함수 호출의 경우 이미 강력하게 인라이닝을 지원함
* 코틀린 인라인 함수는 바이트코드에서 각 함수 호출 지점을 함수 본문으로 대치하기 때문에 코드 중복이 생김
* 함수를 직접 호출하면 스택 트레이스가 더 깔끔해짐
* 람다를 인자로 받는 함수를 인라이닝하면 이익이 더 많음
* 함수 호출 비용 감소, 람다를 표현하는 클래스와 람다 인스턴스에 해당하는 객체를 만들 필요가 없음
* 현재의 JVM은 함수 호출과 람다를 인라이닝해 줄 정도로 똑똑하지 못함
* 인라이닝을 사용하면 일반 람다에서는 사용할 수 없는 몇가지 기능을 사용할 수 있음 (non-local 반환)
* inline 변경자를 붙일 땐 주의해야 함
  * 바이트코드를 증가시키므로 inline 함수는 크기가 작아야 함

### 자원 관리를 위해 인라인된 람다 사용
* 자원 관리 패턴을 만들 때 보통 사용하는 방법은 try/finally문을 사용하되 try 블록을 시작하기 직전에 자원을 획득하고 finally 블록에서 자원을 해제하는 것
* 코틀린 라이브러리에는 withLock이라는 함수가 있음
* withLock 함수 정의를 보면 락을 획득한 후 작업하는 과정을 별도의 함수로 분리
```kotlin
val l:Lock = ..
l.withLock {    // 락을 잠근 다음에 동작을 수행함
    // 락에 의해 보호되는 자원을 사용
}
```
* 자바의 try-with-resource를 대체하는 코틀린의 use
* use는 닫을 수 있는 자원에 대한 확장 함수며, 람다를 인자로 받음
* use는 람다를 호출한 다음에 자원을 닫아줌
```kotlin
fun readFirstLineFromFile(path: String): String {
    BufferedReader(FileReader(path)).use { br -> // BufferedReader객체를 만들고 use함수를 호출하면서 파일에 대한 연산을 실행할 람다를 넘김
      return br.readLint()  // 자원에서 맨 처음 가져온 한 줄을 람다가 아닌 readFirstLineFromFile에서 반환
    }
}
```

## 고차 함수 안에서 흐름 제어

### 람다 안의 return문: 람다를 둘러싼 함수로부터 반환
* 자신을 둘러싸고 있는 블록보다 더 바깥에 있는 다른 블록을 반환하게 만드는 return 문을 넌로컬(non-local) return 이라고 함
* 인라이닝되지 않는 함수에 전달되는 람다 안에서 return을 사용할 수 없음
```kotlin
data class Person(val name: String, val age: Int)
val people = listOf(Person("Alice", 32), Person("Bob", 50))
fun lookForAlice(people: List<Person>) {
    for(person in people) {
        if(person.name == "Alice") {
            println("Found!")
            return
        }
    }
  println("Alice is not found")
}

fun lookForAlice(people: List<Person>) {
  people.forEach {
    if(person.name == "Alice") {
      println("Found!")
      return
    }
  }
  println("Alice is not found")
}
```

### 람다로부터 반환: 레이블을 사용한 return
* 람다식에서도 local return을 사용할 수 있음
* 람다 안에서 로컬 리턴은 for루프의 break와 비슷한 역할
* 로컬 리턴과 넌로컬 리턴을 구분하기 위해 레이블을 사용해야 함
* 람다 식의 레이블을 명시하면 함수 이름을 레이블로 사용할 수 없는 점에 유의
* 람다식에는 2개 이상의 레이블이 붙을 수 없음
* this 식의 레이블에도 동일한 규칙이 적용됨
```kotlin
fun lookForAlice(people: List<Person>) {
  people.forEach label@{    // 람다 식 앞에 레이블을 붙임
    if(it.name == "Alice") return@label // 앞에서 정의한 레이블을 참조
  }
  println("Alice might be somewhere")   // 항상 출력
}

fun lookForAlice(people: List<Person>) {
  people.forEach {
    if(it.name == "Alice") return@forEach // 람다 식으로부터 반환시킴
  }
  println("Alice might be somewhere")
}

println(StringBuilder().apply sb@{
  listOf(1, 2, 3).apply{
    this@sb.append(this.toString())
  }
})
```

### 무명 함수: 기본적으로 로컬 return
* 무명 함수는 코드 블록을 함수에 넘길 때 사용할 수 있는 다른 방법
* 무명 함수는 일반 함수와 비슷하지만 함수 이름이나 파라미터 타입을 생략 할 수 있다는 차이가 있음
```kotlin
fun lookForAlice(people: List<Person>) {
    people.forEach(fun (person) { // 람다식 대신 무명 함수를 사용
        if (person.name == "Alice") return // return은 가장 가까운 함수인 무명함수를 가리킴
        println("${person.name} is not Alice")
    })
}

// filter에 무명 함수 넣기
people.filter(fun (person): Boolean {
  return person.age < 30
})

// 식을 본문으로 하는 무명 함수의 반환 타입은 생략할 수 있음
people.filter(fun (person) = person.age < 30)
```
