# 고차 함수: 파라미터와 반환 값으로 람다 사용

## 고차 함수

- 다른 함수를 인자로 받거나 함수를 반환하는 함수
- 표준 라이브러리 함수인 filter는 술어 함수이자, 고차 함수
- 고차 함수를 정의하려면 함수 타입에 대해 알아야 함

### 함수 타입

```kotlin
val sum: (Int, Int) -> Int = { x, y -> x + y }
val action: () -> Unit = { println(42) }

/**
 * (파라미터 타입, 파라미터 타입, ...) -> 반환 타입
 */
```

- 함수 파라미터의 타입을 괄호 안에 넣고, 그 뒤에 화살표를 추가한 다음, 함수의 반환 타입을 지정하면 됨

### 인자로 받은 함수 호출

- 고차 함수는 아래 코드 처럼 구현하면 됨

    ```kotlin
    fun String.filter(predicate: (Char) -> Boolean): String {
    	val sb = StringBuilder()
    	for (index in O until length) {
    		val element = get (index)
    		if (predicate(element)) sb.append(element)
    	}
    	return sb .toString ()
    }
    ```

- 아래처럼 간단하게 호출하여 사용할 수 있음

    ```kotlin
    val tmp = "ab1c".filter { it in 'a'...'z' })
    println(tmp)
    
    >>> abc
    ```


### 자바에서 코틀린 함수 타입 사용

- 코틀린에서 함수 타입 선언

    ```kotlin
    fun processTheAnswer(f: (Int) -> Int) {
    	println(f(42))
    }
    ```

- 자바에서 코틀린에서 선언한 고차 함수 사용

    ```kotlin
    processTheAnswer(number -> number + 1);
    
    >>> 43
    ```

- 자바 8 이전에서는 아래처럼

    ```kotlin
    processTheAnswer(
    	new Function1<Integer, Integer>() {
    		@Ovveride
    		public Integer invoke(Integer number) {
    			System.out.println(number);
    			return number + 1;
    		}
    	}
    );
    
    >>> 43
    ```


### 디폴트 값을 지정한 함수 타입 파라미터

```kotlin
fun<T> Collection<T>.joinToString(
    separator: String = ", ",
    prefix: String = "",
    postfix: String = "",
    transform: (T) -> String = { it.toString() }
) : String {
    val result = StringBuilder(prefix)
    
    for ((index, element) in this.withIndex()) {
        if (index > 0) result.append(separator)
        result.append(transform(element))
    }
    result.append(postfix)
    return result.toString()
}

>>> val letters = listOf("Alpha", "Beta")
```

### 널이 될 수 있는 함수 타입 파라미터

```kotlin
fun<T> Collection<T>.joinToString(
    separator: String = ", ",
    prefix: String = "",
    postfix: String = "",
    transfor: ((T) -> String)? = null
): String {
    val result = StringBuilder(prefix)
    
    for ((index, element) in this.withIndex()) {
        if (index > 0) result.append(separator)
        val str = transform?.invoke(element)
            ?: element.toString()
        result.append(str)
    }
    result.append(postfix)
    return result.toString()
}
```

### 함수를 반환하는 함수

- 함수의 반환 타입으로 함수 타입을 지정해야 함

    ```kotlin
    enum class Delivery { STANDARD, EXPEDITED }
    
    class Order(val itemCount: Int)
    
    fun getShippingCostCalculator(
        delivery: Delivery)
    : (Order) -> Double {
        if (delivery == Delivery.EXPEDITED) { 
            return { order -> 6 + 2.1 * order.itemCount }
        }
        return { order -> 1.2 * order.itemCount }     
    }
    
    >>> val calculator = getShippingCostCalculator(Delivery.EXPEDITED)    // 반환받은 함수를 변수에 저장
    >>> println("Shipping costs ${calculator(Order(3))}")    // 반환받은 함수 호출
    
    Shipping costs 12.3
    ```
## 인라인 함수

- inline 변경자를 어떤 함수에 붙이면 컴파일러는 그 함수를 호출하는 모든 문장을 함수 본문에 해당하는 바이트코드로 바꿔치기 함

### 인라이님이 작동하는 방식

- 어떤 함수를 inline으로 선언하면 그 함수의 본문이 인라인이 됨
- 함수를 호출히는 코드를 힘수를 호출하는 바이트코드 대신에 함수 본문을 번역한 바이트 코드로 컴파일한다는 뜻임

```kotlin
inline fun<T> synchronized(
	lock: Lock, 
	action: () -> T,
): T {
    lock.lock()
    try {
        return action()
    }
    finally {
        lock.unlock()
    }
****}
```

- 자바의 synchronized 똑같아 보이지만, 아무 타입의 객체를 인자로 받을 수 있음

```kotlin
fun foo(l : Lock) {
    println("Before sync")
    synchronized(l) {
        println("Action")
    }
    println("After sync")
}
```

- 위처럼 사용 가능

### 인라인 함수의 한계

- 람다를 사용하는 모든 함수를 인라이닝할 수는 없음
- 함수 본문에서 파라미터로 받은 람다를 호출하는 경우는 가능함
- 파라미 터로 받은 람다를 다른 변수에 저장하고 나중에 그 변수를 시용하는 경우에는 불가능

### 컬렉션 연산 인라이닝

- 코틀린 표준 라이 브러리의 컬렉션 함수는 대부분 람다를 인자로 받음
- 시퀀스는 람다를 인라인 하지 않음

### 함수를 인라인으로 선언해야 하는 경우

- 인라인 남용 금지
- 인라인 키워드는 람다를 인자로 받는 함수에 한해서 성능이 좋음

### 자원 관리를 위해 인라인된 람다 사용

- 코틀린은 자바의 try-with-resource 구문을 제공하지 않음
- 대신, 함수 타입의 값을 파라미터로 받는 표준 라이브러리를 제공함

```kotlin
fun readFirstLineFormFile(
	path: String
): String {
    BufferReader(FileReader(path)).use { br -> 
      return br.readLine()
    }
}
```
### 람다 안의 return문: 람다를 둘러싼 함수로부터 반환

```kotlin
fun lookForAlice(people: List<Person>) {
	people.forEach {
		if (it.name == "Alice") {
			println ("Found1")
			return
		}
	}
	println ("Alice is not found")
}
```

- 람다 안에서 return을 사용하면 람다로부터만 반환되는게 아니라 그 람다를 호출하는 함수가 실행을 끝내고 반환됨
- 자신을 둘러싸고 있는 블록보다 더 바깥에 있는 다른 블록을 반환하게 만듦
- 다른 말로, non-local return이라 함

### 람다로부터 반환: 레이블을 사용한 return

- 람다에서도 local return을 할 수 있음

```kotlin
fun lookForAlice(people: List<Person>) {
    people.forEach label@{
        if (it.name == "Alice") return@label
    }
    println("Alice might be somewhere")
}
```

```kotlin
fun lookForAlice(people: List<Person>) {
    people.forEach {
        if (it.name == "Alice") return@forEach
    }
    println("Alice might be somewhere")
}
```

### 무명 함수: 기본적으로 로컬 return

- 무명 함수는 코드 블록을 함수에 넘길 때 사용할 수 있는 다른 방법임

```kotlin
fun lookForAlice(people: List<Person>) {
    people.forEach(fun (person) {
        if (person.name == "Alice") return
        println("${person.name} is not Alice")
    })
}
```
