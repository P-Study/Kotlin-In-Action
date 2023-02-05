## 5.1 람다 식과 멤버 참조

### 5.1.1 람다 소개
- 람다 식을 사용하면 함수를 선언할 필요가 없고 코드 블록을 직접 함수의 인자로 전달
### 5.1.3 람다 식의 문법
- 람다는 항상 중괄호로 둘러쌓여 있음
- 람다 식은 변수에 저장 가능하며 일반 함수와 동일하게 사용 가능
```kotlin
{ (x: Int,y: Int) -> x + y }
```
- 람다 실행 방법은 아래와 같이 2가지 존재
- 개인적으로 람다 식을 직접호출하는 것보다 run을 통해 호출하는게 직관적이라고 느껴짐
```kotlin
// 즉시 실행 방법
// 1. 식을 직접 호출
{ println(42) }()
// 2. run을 통한 본문 코드 실행
run { println(42) }
```
- 함수 호출 시 맨 뒤 인자가 람다 식이라면 괄호 밖으로 빼낼 수 있음
- 간결해져서 좋아 보임
```kotlin
people.maxBy() { p: Person -> p.age }

// 함수에 괄호는 생략할 수 있음
people.maxBy { p: Person -> p.age }
people.maxBy { Person::age }
```
- 이름 붙인 인자를 사용해서 람다의 용도를 명확하게 했음
```kotlin
people.joinToSting(separator = "", transform = { p: Person -> p.name })
```

- 람다를 괄호 밖으로 전달하여 간결하게 하였음
- 개인적인 생각으로는 괄호 밖으로 람다를 전달하여 간결하게 하는 것이 가독성이 올라간다고 생각함
- 다만, 람다를 알고 있다는 전제가 필요함
- 어떤 방식이 괜찮은지 논의를 해보는 것도 좋을 것 같음
```kotlin
people.joinToSting("") { p: Person -> p.name }
```

### 5.1.4 현재 영역에 있는 변수에 접근
- 람다를 함수안에서 정의하면 함수의 파라미터뿐 아니라 람다 정의의 앞에 선언된 로컬 변수까지 람다에서 모두 사용 가능
- 람다 안에서 사용하는 외부 변수를 포획한 변수라고 함
```kotlin
fun printMessages(messages: Collection<String>, prefix: String) {
    messages.forEach {
        println("$prefix $it")
    }
}
```

### 5.1.5 멤버 참조
- '::' 멤버 참조를 통해 프로퍼티나 메소드를 단 하나의 값으로 만들어줌
```kotlin
Person::age
```
- 최상위에 선언된 함수나 프로퍼티를 참조할 수 있음
```kotlin
fun salute() = println("Salute!")
run(::salute)
```

## 5.2 컬렉션 함수형 API
- 컬렉션을 다루는 코드를 작성할 경우에는 원하는 바를 어떻게 일반적인 변환을 사용해 표현할 수 있는지 생각하고 라이브러리 함수가 있는지 살펴봐라

### 5.2.1 필수적인 함수: filter와 map
- stream의 filter, map과 거의 동일하다고 생각함
- 주의사항은 필요하지 않은 경우 계산을 반복하지 않는 것
```kotlin
people.filter { it.age == people.maxBy(Person::age)!!.age }
```

### 5.2.2 all, any, count, find: 컬렉션에 술어 적용
- all 컬렉션의 원소가 조건에 모두 해당 되는지 여부
- any 컬렉션의 원소가 조건에 하나라도 해당 되는지 여부
- count와 size의 사용
  - count는 조건을 만족하는 원소의 개수만을 추적
  - size는 조건을 만족하는 원소를 저장
```kotlin
people.all { it.age > 5 }
people.any { it.age > 5 }
```

### 5.2.3 groupBy: 리스트를 여러 그룹으로 이뤄진 맵으로 변경
- stream의 groupBy와 동일한 것으로 보여지며, 원소를 기준으로 그룹핑을 하는 것
```kotlin
people.groupBy { it.age }
```

### 5.2.4 flatMap과 flattern: 중첩된 컬렉션 안의 원소 처리
- map으로 특정 형태로 변환
- flattern으로 하나로 모으는 것

## 5.3 지연 계산(lazy) 컬렉션 연산
- 시퀀스(sequnce)는 중간 임시 컬렉션을 사용하지 않고도 컬렉션 연산을 연쇄할 수 있는 것
- 원소가 수백만 개가 되면 중간 연산의 결과를 담는 임시 컬렉션을 만들지 않기 때문에 효율적
- 시퀀스 원소는 필요할 때 계산
```kotlin
private val people = listOf(Person("Yoo", 22), Person("Lee", 21))

// 시퀀스 없이 호출
people.map(Person::name).filter { it.startsWith("A") }

// 시퀀스를 사용한 호출
people.asSequence().map(Person::name).filter { it.startsWith("A") }.toList()
```

### 5.3.1 시퀀스 연산 시행: 중간 연산과 최종 연산
- 중간 연산은 다른 시퀀스를 반환하며 최종 연산은 결과를 반환함
- 중간 연산의 순서를 활용하여 연산 효율을 높일 수 있음
- java stream의 병렬 처리와 기능이 동일함
- sequence 자주 사용하게 될 것 같음
  - 다만, Stream 병렬처리처럼 성능상 효율적인 곳에서만 사용
```kotlin
// 최종 연산이 없어 print 출력되지 않음
fun thirdExample() =
  listOf(1,2,3,4).asSequence()
    .map { print("map($it) "); it * it }
    .filter { print("filter($it) "); it % 2 == 0 }

// 최종 연산이 있기 때문에 print 출력됨(최종 연산이 호출될 때)
fun fourthExample() =
  listOf(1,2,3,4).asSequence()
    .map { print("map($it) "); it * it }
    .filter { print("filter($it) "); it % 2 == 0 }
    .toList()

// 중간 연산 수행 순서가 있음
map(1) -> filter(1) -> map(2) -> filter(4) -> map(3) -> filter(9) -> map(4) -> filter(16)
```

## 5.4 자바 함수형 인터페이스 활용
- Functional Interface or Single Abstract Method(SAM)

### 5.4.1 자바 메소드에 람다를 인자로 전달
- 함수형 인터페이스를 인자로 원하는 자바 메소드에 람다를 전달할 수 있음
- 컴파일러는 자동으로 무명 클래스와 인스턴스를 만들어줌
```kotlin
// 자바 메소드
void postponeComputation(int delay, Runnable computation);

postponeComputation(1000, object : Runnable {
  override fun run() {
    println(42)
  }
})
```
- 무명 객체는 호출을 할 때마다 새로운 객체가 생성됨
- 람다는 무명 객체를 메소드를 호출할 때마다 반복 사용
```kotlin
postponeComputation(1000) { println(42) }

val runnable = Runnable { println(42) }
fun handleComputation() {
  postponeComputation(1000, runnable)
}
```
- 람다가 변수를 포획한다면 매 호출마다 같은 인스턴스를 사용할 수 없음
```kotlin
fun secondHandleComputation(id: String) {
  postponeComputation(1000) { println(id) }
}
```

### 5.4.2 SAM 생성자: 람다를 함수형 인터페이스로 명시적으로 변경
```kotlin
fun createAllDoneRunnable(): Runnable {
  return Runnable { println("All done! 헀으면 좋겠다") }
}

createAllDoneRunnable().run()
```

## 5.5 수신 객체 지정 람다: with와 apply
- 수신 객체를 명시하지 않고 람다의 본문 안에서 다른 객체의 메소드를 호출할 수 있게 하는 것(확장 함수랑 비슷한 느낌?)

### 5.5.1 with 함수
- 객체를 반복하지 않고도 객체에 다양한 연산을 수행하여 결과로 만들 수 있음
```kotlin
fun alphabet(): String {
  val stringBuilder = StringBuilder()
  for (letter in 'A'..'Z') {
    stringBuilder.append(letter)
  }
  stringBuilder.append("\n알파베지터미네이터키아이스크림창정")
  return stringBuilder.toString()
}
```
```kotlin
fun firstAlphabetForWithFunction(): String {
  val stringBuilder = StringBuilder()
  return with(stringBuilder) {
    for (letter in 'A'..'Z') {
      this.append(letter)
    }
    append("\n알파베지터미네이터키아이스크림창정")
    toString()
  }
}
```
```kotlin
fun secondAlphabetForWithFunction() = with(StringBuilder()) {
  for (letter in 'A'..'Z') {
    append(letter)
  }
  append("\n알파베지터미네이터키아이스크림창정")
  toString()
}
```
```kotlin
// @OuterClass를 지정하여 호출하고 싶은 메소르를 명확히 알려줌
this@OuterClass.toString()
```

### 5.5.2 apply 함수
- 항상 자신에게 전달된 객체를 반환
```kotlin
fun firstAlphabet() = StringBuilder().apply {
  for (letter in 'A'..'Z') {
    append(letter)
  }
  append("\n알파베지터미네이터키아이스크림창정")
}.toString()
```
```kotlin
fun secondAlphabet() = buildString {
  for (letter in 'A'..'Z') {
    append(letter)
  }
  append("\n알파베지터미네이터키아이스크림창정")
}
```
- 객체 초기화 시 apply 함수 활용(객체 생성 시 파라미터명을 지정할 수 있어 간결한 면에서 파라미터 지정이 더 낫지 않나 싶음)
```kotlin
fun createViewWithCustomAttributes(context: Context) =
  TextView(context).apply {
    text = "Sample Text"
    textSize = 20.0
  }
```