# 제네릭스

## 제네릭 타입 파라미터

- 제네릭스를 사용하면 타입 파라미터를 받는 타입을 정의할 수 있다.
- “이 변수는 리스트다” 대신 “이 변수는 문자열을 담는 리스트다”다 될 수 있다.

### 제네릭 함수와 프로퍼티

- 모든 타입의 리스트를 다룰 수 있는 함수를 하고 싶다면,

    ```kotlin
    fun <T> List<T>.slice(indices: IntRange): List<T>
    ```

  호출하면,

    ```kotlin
    val letters = ('a'..'z').toList()
    println(letters.slice<Char>(0..2))    // 명시
    >>> [a, b, c]
    
    println(lettets.slice(10..13))    // 추론
    >>> [k, l, m, n]
    ```

- 제네릭 확장 프로퍼티를 선언할 수 있다.

    ```kotlin
    val <T> List<T>.penultimate: T
    	get() = this[size - 2]
    
    println(listOf(1, 2, 3, 4).penultimate)
    >>> 3
    ```


### 제네릭 클래스 선언

- 자바처럼 클래스나 인터페이스 이름 뒤에 <>를 붙이면, 제네릭하게 만들 수 있다.

    ```kotlin
    interface List<T> {
        operator fun get(index: Int): T
    }
    ```

- 제네릭 클래스를 확장하는 클래스는 기반 타입의 제네릭 파라미터에 대해 타입 인자를 지정해야 한다.

    ```kotlin
    class StringList: List<String> {
        override fun get(index: Int): String = ...
    }
    
    class ArrayList<T>: List<T> {
        override fun get(index: Int): T = ...
    }
    ```


### 타입 파라미터 제약

- 타입 파라미터 제약이란, 클래스나 함수에 사용할 수 있는 타입 인자를 제한하는 기능이다.

    ```kotlin
    fun <T: Number> List<T>.sum(): T
    ```


### 타입 파라미터를 널이 될 수 없는 타입으로 확장

- 타입 파라미터에 상한을 정하지 않고 싶다면,"<T : Any?>" 사용하여 널 허용
- 널 가능성을 제한하고 싶다면,"<T: Any>" 사용
