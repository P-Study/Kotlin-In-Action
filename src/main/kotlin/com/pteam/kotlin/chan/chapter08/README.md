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