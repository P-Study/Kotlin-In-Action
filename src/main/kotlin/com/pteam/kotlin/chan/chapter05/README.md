# 람다 프로그래밍
## 람다 식과 멤버 참조

### 람다 소개: 코드 블록을 함수 인자로 넘기기

```java
button.setOnClickListener(new OnClickListener() {
	@Override
	public void onClick (View view) {
		/* 클릭 시 수행할 동작 */
	}
});
```

위는 무명 내부 클래스를 사용하여 작업을 처리하는 방법입니다. 코틀린은 자바 8과 마찬가지로 이를 람다로 개선할 수 있습니다.

```kotlin
button.setOnClickListener { /* 클릭 시 수행할 동작 */ }
```

위의 코드는 이전 코드와 같은 역할을 하지만, 굉장히 간단합니다.

### 람다와 컬렉션

람다가 없다면, 컬렉션 라이브러리를 쉽게 제공하기는 힘듭니다. 하지만, 람다를 사용할 수 있는 코틀린은 많은 기능을 직접 작성하지 않고 손 쉽게 사용할 수 있습니다.

```kotlin
data class Person(val name: String, val age: Int)
```

```kotlin
fun findTheOldest(people : List<Person>) {
	var max.Age = 0
	var theOldest : Person? = null
	for (person in people) {
	  if (person.age > maxAge) { 
			maxAge =person.age theOldest = person
	  }
	}
	println (theOldest)
}
```

```kotlin
val people = listOf(Person("Alice", 29), Person("Bob", 31))
findTheOldest(people)

>>> Person (name=Bob, age=31)
```

```kotlin
val people = listOf(Person( "Alice", 29) , Person( "Bob", 31))
println (people.maxBy { it .age })

>>> Person (name=Bob, age=31)
```

### 람다 식의 문법

```kotlin
val sum = { x: Int, y: Int -> x + y }
```

```kotlin
val sum = ( x: Int, y: Int ->
	println("Computing the sum of $x and $y...")
	x + y
}
```

람다식 안에는 한 줄이 아니어도 됩니다.

```kotlin
people.maxBy({ p: Person -> p.age })
```

```kotlin
people.maxBy { p: Person -> p.age }
```

```kotlin
people.maxBy { it.age }
```

```kotlin
val getAge = { p: Person -> p.age }
people.maxBy(getAge)
```

위는 모두 같은 결과입니다.

```kotlin
println(42)
```

```kotlin
{ println(42) } ()
```

```kotlin
run { println(42) )
```

당연하게도 인자가 없는 람다식도 있습니다. 이 또한, 같은 결과입니다.

### 현재 영역에 있는 변수에 접근

자바는 람다식 안에서 파이널 변수를 당연하게도 변경할 수 없지만, 참조할 수 있습니다. 그리고 파이널 변수가 아닌 변수는 참조도 변경도 할 수 없습니다.

하지만, 코틀린 람다는 파이널 변수가 아닌 변수에도 접근할 수 있습니다. 또한, 람다 안에서 바깥의 변수를 변경할 수 있습니다.

```kotlin
class Ref<T> (var value : T)
val counter = Ref(0)
val inc = { counter.value++ }
```

```kotlin
var counter = 0
val inc = { counter++ )
```

위의 방법으로 변경 가능하게 속임수를 썼습니다.

### 멤버 참조

```kotlin
val getAge = Person::age      // 클래스::멤버
```

위처럼 ::를 사용하는 식을 멤버 참조라고 합니다.

```kotlin
val getAge = { person: Person -> person.age )
```

원래는 위의 식과 동일했습니다.

```kotlin
fun salute() = println("Salute!")
run(::salute)

>>> Salute
```

그리고 이것도 됩니다.

## 컬렉션 함수형 API

### fillter

![Untitled](https://user-images.githubusercontent.com/58816862/215757162-c714051f-a6ab-4e14-9832-77bc064cf508.png)

```kotlin
val list = listOf(1, 2, 3, 4)
println(list.filter { it % 2 == 0 })

>>> [2, 4]
```

```kotlin
val people = listOf(Person("Alice", 29), Person("Bob", 31))
println(people.filter { it.age > 30 })

>>> [Person(name=Bob, age=31)]
```

필터는 이처럼 주어진 값들 중에서 필터링을 하는 역할을 합니다.

### map

![Untitled](https://user-images.githubusercontent.com/58816862/215757216-8bff6734-f5db-47f2-aa46-b94d682cbc97.png)

```kotlin
val list = listOf(1, 2, 3, 4)
println(list.map { it * it })

>>> [1, 4, 9, 16]
```

```kotlin
val people=listOf(Person("Alice", 29), Person("Bob", 31))
println(people.map { it.name })

>>> [Alice, Bob)
```

맵은 이처럼 주어진 값들을 변경할 수 있습니다.

### all

```kotlin
val people = listOf(Person("Alice", 27), Person("Bob", 31))
println(people.all {canBeinClub27})

>>> false
```

모든 원소가 술어를 만족하는지 확인하는 역할을 합니다.

### any

```kotlin
val list = listOf(1, 2, 3)
println(list.any { it == 3 })

>>> true
```

원소 중에 적어도 하나는 술어를 만족하지 확인하는 역할을 합니다.

(!all과 동일합니다.)

### count

원소의 개수를 반환하는 역할을 합니다.

### find

```kotlin
val people = listOf(Person("Alice", 27), Person("Bob", 31))
println(people.find(canBeInClub27))

>>> Person(name=Alice, age=27)
```

find는 자바 스트림에서의 firstOrNull이라고 생각하시면 됩니다.

### groupBy

![Untitled](https://user-images.githubusercontent.com/58816862/215757294-bf057803-6bba-469f-a158-42099ef57d42.png)

```kotlin
val people = listOf(
        Person("Alice", 31),
        ...,
        Person("Bob", 29),
        Person("Carol", 31)
)
println(people.groupBy { it.age })

>>> (29=[Person(name=Bob, age=29}], 31=[Person(name=Alice, age=31), Person(name=Carol, age=31)]}
```

groupBy는 술어를 key로 잡고 value에 객체 리스트를 넣는 구조입니다.

위 코드의 반환 타입으로는 Map<Int, List<Person>>입니다.

```kotlin
val list = listOf( "a ", "ab", "b")
println (list.groupBy {String::first})

>>> {a=[a, ab], b=[b]}
```

위와 같은 것도 가능합니다.

### flatMap, flatten

![Untitled](https://user-images.githubusercontent.com/58816862/215757389-1bd51eba-1a79-47ba-8a0e-a141e8b1fdd0.png)

```kotlin
val strings = listOf("abc", "def")
println(strings.flatMap { it.toList() })

>>> [a, b, c, d, e, f]
```

위는 map과 flatten을 한 것입니다.

## Lazy 컬렉션 연산

### Sequence

![Untitled](https://user-images.githubusercontent.com/58816862/215757519-7ce5e8f0-3c20-48cf-8f63-f0fcf4606eb3.png)

시퀀스는 위처럼 중간 연산과 최종연산이 있습니다. 그리고 이 둘을 통해 지연계산을 합니다.

중간 연산은 시퀀스를 반환하며, 아무런 계산을 하지않습니다. 최종 연산은 사용자가 원하는 값을 반환하며, 중간 연산때 미뤘던 연산을 수행하게 됩니다.

### 지연 계산의 장점

![Untitled](https://user-images.githubusercontent.com/58816862/215757585-ebac1eed-4d87-4d54-a083-2902f6279fbe.png)

1, 2, 3, 4를 map을 통해 제곱으로 변환하고, find로 4를 찾는다고 가정하겠습니다.

컬렉션은 즉시 계산을 하기 때문에 모든 원소를 제곱하고 값을 찾지만, 시퀀스는 원소 하나 씩 처리하기 때문에 효율이 보다 좋습니다.

## 자바 함수형 인터페이스 활용

```java
void postponeComputation (int delay, Runnable computation);
```

```kotlin
postponeComputation(1000, object: Runnable {
	override fun run() {
		println(42)
	}
})
```

```kotlin
postponeComputation(1000) ( println(42) }
```

위는 자바에서 함수형 인터페이스를 인자로 원하는 자바 메소드에 코틀린 람다를 전달할 수 있도록 과정을 보여드렸습니다.

## 수신 객체 지정 람다: with, apply

### with 함수

```kotlin
fun alphabet() : String {
	val result = StringBuilder()
	for (letterin 'A'..'Z') {
		result.append(letter)
	}
	result.append("\nNow I know the alphabet!")
	return result.toString ()
}
println(alphabet())

>>> ABCDEFGHIJKLMNOPQRSTUVWXYZ
>>> Now I know the alphabet!
```

```kotlin
fun alphabet() = with(StringBuilder()) { 
	for (letterin 'A'..'Z') {
		append(letter)
	}
	append("\nNow I know the alphabet ! ")
	toString()
}
```

이처럼 with 함수를 사용할 수 있습니다. (this 생략)

### apply 함수

```kotlin
fun alphabet() = StringBuilder().apply {
	for (letterin 'A'..'Z') {
		append(letter)
	}
	append("\nNow I know the alphabet!")
}.toString ()
```

위는 with 함수로 구현한 코드를 apply 함수로 리팩토링한 것입니다. with와 apply는 유사해 보이지만 차이가 있습니다.

apply는 자신에게 전달된 객체를 반환한다는 점에서 차이가 있습니다.
