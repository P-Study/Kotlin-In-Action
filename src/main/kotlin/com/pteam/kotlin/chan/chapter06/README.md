# 코틀린 타입 시스템
## null 가능성

### ?

```kotlin
s: String?
```

코틀린에서의 모든 타입은 null이 될 수 없지만, 타입 이름 뒤에 물음표를 붙이면, null이 될 수 있는 타입이 됩니다.

```kotlin
fun strLenSafe(s: String?) = s.length()

>>> ERROR: only safe (?.) or non-null asserted (!!.) calls are allowed on a nullable receiverof type kotlin.String?
```

null이 가능한 변수는 변수.메소드() 할 수 없습니다.

```kotlin
val x: String? = null
val y: String = x

>>> ERROR: Type mismatch: inferred type is String? but String was expected
```

null이 될 수 있는 값을 null이 될 수 없는 변수에 대입할 수 없습니다.

```kotlin
fun strLenSafe(s: String?): Int = 
	if(s != null) s.length else 0

val x: String? = null
println(strLenSafe(x))

>>> 0
```

null과 비교하면, 컴파일러는 그 사실을 기억하고 null이 없는 타입의 값처럼 사용할 수 있습니다.

### ?.

```kotlin
s?.toUpperCase()

>>> if (s != null) s.toUpperCase() else null
```

.?은 null 검사와 메소드 호출을 한 번의 연산으로 수행합니다. 다시 말해서, 호출하려는 값이 null이 아니면 일반 메소드 호출처럼 작동하지만, 호출하려는 값이 null이면 이 호출은 무시되고 null이 반환됩니다.

### ?:

```kotlin
fun foo(s: String?) {
	val t: String = s ?: ""
}
```

위처럼 엘비스 연산자를 사용하면, null일 경우에 특정 값으로 바꿔줄 수 있습니다.

### as?

```kotlin
fun foo(s: Any?) {
	val s as? Person ?: return false
}
```

위처럼 안전한 연산자는 타입이 일치하지 않으면 null을 반환하기 때문에, “false”를 반환합니다.

### !!

이는 null이 아님을 알리고, null이 될 수 없는 타입으로 바꿀 수 있습니다.

### let 함수

```kotlin
fun sendEmailTo(email: String) {
	println("Sending email to $email")
}
var email: String? = "leeheefull@wemakeprice.com"

email?.let { sendEmailTo(it) }
>>> Sending email to sendEmailTo

email = null
email?.let { sendEmailTo(it) }
>>>   // 아무일도 일어나지 않음.
```

let 함수는 원하는 식을 평가해서 결과가 null인지 검사한 다음에 결과를 변수에 넣는 작업을 할 수 있습니다.

let 함수 안의 로직이 null일 경우에는 아무 일도 일어나지 않습니다.

### 널이 될 수 있는 타입 확장

isNullOrBlank()를 예시로 들면, isBacnk()는 null이 아닌 문자열 타입의 값에 대해서만 호출할 수 있습니다.

하지만, isNullOrBlank()는 null인 경우 true를 반환하고, null이 아닌 경우 isBlank()를 호출하여 null 값에 대한 적절한 처리를 할 수 있습니다.

### 타입 파라미터의 null 가능성

```kotlin
fun <T> printHashCode(t: T) {
	println(t?.hashCode())
}
```

위처럼 “T”의 타입은 Any?이기 때문에, 안전한 호출을 써야만 합니다.

## 원시 타입

### 코틀린 타입

```java
List<int> list1 = new ArrayList<>();
>>> X

List<Ineger> list2 = new ArrayList<>();
>>> O
```

```kotlin
val list: List<Int>
```

코틀린은 자바와 달리 참조 타입과 원시 타입을 구분하지 않고, 항상 같은 타입을 사용합니다.

또한, null 참조를 자바의 참조 타입의 변수에만 대입할 수 있기 때문에 null이 될 수 있는 코틀린 타입은 자바의 원시 타입으로 표현할 수 없습니다.

### Any

자바에서 Object가 클래스의 최상위 타입이듯, 코틀린에서는 Any 타입이 모든 null이 될 수 없는 타입의 조상 타입입니다.

### Unit

자바에서의 void는 코틀린에서 Unit과 같은 기능을 담당합니다.

하지만 코틀린에서는 반환 값이 없을 경우, 반환 타입을 명시하지 않습니다. 그렇다면 언제 Unit을 명시할까요?

```kotlin
interface Processor<T> {
	fun process(): T
}

class NoResultProcessor : Processor<Unit> {
	override fun process() {
		// 내용
	}
}
```

위 와같이 제네릭 파라미터를 반환하는 함수를 오버라이드하면서 반환 타입으로 Unit을 쓸 때 유용합니다.

위 코드는 Unit을 반환하지만, 타입을 지정할 필요는 없습니다. 또한, 메서드 안에서 return을 명시하지 않아도 됩니다.

### Nothing

Nothing 반환 타입은 한 문장으로 “이 함수는 결코 정상적으로 끝나지 않는다”를 뜻합니다.

```kotlin
fun fail(message: String) : Nothing {
	throw IllegalStateException(message)
}
```

다시말해서, 위처럼 반환을 하지않고 예외를 던질 때 사용합니다.

## 컬렉션과 배열

### 컬렉션 null 가능성

컬렉션 자체가 null일 경우도 있지만, 컬렉션의 원소 중에 null이 있을 경우가 있습니다.

```kotlin
fun addValidNumbers(numbers: List<Int?>) {
	var sumOfValidNumbers = 0
	var invalidNumbers = 0
	for (number in numbers) {
		if (number != null) {
			sumOfValidNumbers += number
		} else {
			invalidNumbers++
		}
	}
}
```

```kotlin
fun addValidNumbers(numbers: List<Int?>) {
	val validNumbers = numbers.filterNotNull()
}
```

코틀린은 이를 위처럼 filterNotNull()을 사용하여 해결합니다.

### 읽기 전용과 변경 가능한 컬렉션

코틀린에서는 자바와 달리 컬렉션을 읽기 전용(Collection)과 변경 가능한(MutableCollection) 컬렉션으로 나누었습니다.

사용하는 컬렉션이 읽기만 할 것인지, 변경도 할 것인지를 알리기 위해 나눴다고 합니다.

```kotlin
fun <T> copyElements(source: Collection<T>, target: MutableCollection<T>)
```

변경할 일이 없다면 최대한 읽기 전용으로 쓰다가, 위의 메서드를 사용하여 변경 가능한 컬렉션으로 바꾸어 사용하면 됩니다.

![https://user-images.githubusercontent.com/58816862/217274674-be993ec0-d940-4163-b98d-5b395bcd7a4f.png](https://user-images.githubusercontent.com/58816862/217274674-be993ec0-d940-4163-b98d-5b395bcd7a4f.png)

![https://user-images.githubusercontent.com/58816862/217273926-8158d5ca-cb2f-48cb-8a1c-3873f293376d.png](https://user-images.githubusercontent.com/58816862/217273926-8158d5ca-cb2f-48cb-8a1c-3873f293376d.png)

위처럼 모든 코틀린 컬렉션은 그에 상응하는 자바 컬렉션 인터페이스의 인스턴스입니다.

### 객체 배열과 원시 타입 배열

자바에도 배열이 있듯이 코틀린도 배열이 있습니다. 객체 배열과 원시 타입 배열이 있습니다.

```kotlin
val letters = Array<String>(26) { i -> ('a' + i).toString() }
```

위는 Array 생성자를 사용하여 객체 배열 만드는 방법입니다. 이 외에도 arrayOf 메서드를 사용하거나 arrayOfNull 메서드를 사용하여 객체 배열을 만들 수 있습니다.

```kotlin
val squares = IntArray(5) { i -> (i+l) * (i+l) )
val fiveZerosToo = intArrayOf(0, 0, 0, 0, 0)
```

위는 원시 타입 배열을 생성하는 방법입니다. IntArray 타입 외에도 ByteArray, CharArray 등 여러가지가 있습니다. (컴파일 시에는 int[], byte[], char[], ..로 컴파일 됨)