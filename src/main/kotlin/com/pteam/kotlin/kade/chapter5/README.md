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