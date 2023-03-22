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

## 실행 시 제네릭스의 동작: 소거된 타입 파라미터와 실체화된 타입 파라미터

- JVM의 제네릭스는 타입 소거를 사용하여 구현된다.
- 이는 실행 시점에 제네릭 클래스의 인스턴스에 타입 인자 정보가 들어있지 않다는 것을 뜻한다.
- 코틀린에서는 inline으로 함수를 선언하여 타입 인자를 지워지지 않게 할 수 있다. → 실체화

### 실행 시점의 제네릭: 타입 검사화 캐스트

- 자바와 코틀린은 타입 인자 정보가 런타임에 지워진다.
- 제네릭 클래스 인스턴스가 생성될 때 쓰인 타입 인자에 대한 정보를 유지하지 않는다는 뜻이다.
- 이는 실행 시점에는 List 객체가 어떤 타입의 원소를 갖는지 모른다는 의미다.

    ```kotlin
    val list1: List<String> = listOf("a", "b")
    val list2: List<Int> = listOf(1, 2)
    
    // 실행 시점에는 둘이 같은 타입으로 인지
    ```

- 실행 시점에 List 객체의 원소 타입이 무엇인지는 스타 프로젝션을 사용하여 알 수 있다.

    ```kotlin
    fun printSum(c: Collection<Int>) {
        if (c is List<Int>) {
            println(c.sum)
        }
    }
    ```


### 실체화한 타입 파라미터를 사용한 함수 선언

- 인라인 함수의 타입 파라미터는 실체화되므로 실행 시점에 인라인 함수의 타입 인자를 알 수 있다.

    ```kotlin
    fun <T> isA(value: Any) = value is T
    >>> Error : Cannot check for instance of erased type : T
    
    inline fun <reified T> isA(value: Any) = value is T
    println(isA<String>("abc"))
    >>> true
    ```


### 실체화한 타입 파라미터로 클래스 참조 대신

- java.lang.Class 타입 인자를 파라미터로 받는 API에 대한 코틀린 어댑터를 구축하는 경우 실체화한 타입 파라미터를 자주 사용한다.

    ```kotlin
    val servicelmpl = ServiceLoader.load(Service::class.java)
    ```

  구체화한 타입 파라미터를 사용하여 다시 작성한다면?

    ```kotlin
    inline fun <reified T> loadService() {
        return ServiceLoader.load(T::class.java)
    }
    
    val serviceImple = loadService<Service>()
    ```


### 실체화한 타입 파라미터의 제약

- 실체화한 타입 파라미터를 사용할 수 있다.
  - 타입 검사와 캐스팅(is, !is, as, as?)
  - 코틀린 리플랙션 API(::class)
  - 코틀린 타입에 대응하는 java.lang.Class를 얻기(::Class.java)
  - 다른 함수를 호출할 때 타입 인자로 사용
- 반면, 아래와 같은 일은 할 수 없다.
  - 타입 파라미터 클래스의 인스턴스 생성하기
  - 타입 파마리터 클래스의 동반 객체 메소드 호출하기
  - 실체화한 타입 파라미터를 요구하는 함수를 호출하면서 실체화하지 않은 타입 파라미터로 받은 타입을 타입 인자로 넘기기
  - 클래스, 프로퍼티, 인라인 함수가 아닌 함수의 타입 파라미터를 reified로 지정하기

## 변성: 제네릭과 하위 타입

- 변성이란, List<String>와 List<Any>와 같이 기저 타입이 같고 타입 인자가 다른 여러 타입이 서로 어떤 관계가 있는지 설명하는 개념이다.

### 변성이 있는 이유: 인자를 함수에 넘기기

```kotlin
fun addAnswer(list: MutableList<Any>) {
    list.add(42)
}
addAnswer(mutableListOf("abc", "bac"))

>>> compile error
```

### 클래스, 타입, 하위 타입

- 클래스와 타입은 같지 않다.
  - 제네릭 클래스가 아닌 클래스에서는 클래스 이름을 바로 타입으로 쓸 수 있다.
    - var x: String or var x: String?
  - 반면, 제네릭 클래스에서는 타입이 많다.
    - List<Int>, List<String?>, List<List<String>>, …
- 어떤 A의 값이 필요한 모든 장소에 어떤 타입 B의 값을 넣어도 아무 문제가 없다면 타입 B는 타입 A의 하위 타입이라고 한다.
  - Int는 Number의 하위 타입이다.
  - 반면, Int는 String의 하위 타입이 아니다.

### 공변성: 하위 타입 관계를 유지

- Poducer<T>에서 만약 A가 B의 하위 타입일 때 Producer<A> 가 Producer<B>의 하위 타입이면 Producer는 공변적이다.
- 코틀린에서 제네릭 클래스가 타입 파라미터에 대해 공변적임을 표시하려면 타입 파라미터 이름 앞에 out을 넣어야 한다.

    ```kotlin
    interface Producer<out T> {
        fun produce(): T
    }
    ```

- 클래스의 타입 파라미터를 공변적으로 만들면 함수 정의에 사용한 파라미터 타입과 타입인자의 타입이 정확히 일치하지 않더라도 그 클래스의 함수를 인자나 반환 값으로 사용할 수 있다.
- out을 사용하면 하위 타입 관계가 유지된다.

### 반공변성: 뒤집힌 하위 타입 관계

- 반공변 클래스의 하위 타입 관계는 공변 클래스와 반대다. Consumer<T>에서 만약 A가 B의 하위 타입일 때 Consumer<A>가 Consumer<B>의 상위 타입이면 Consumer는 반공변적이다.
- 코틀린에서 반공변을 표시하기 위해서는 타입 파라미터 앞에 in 붙이고, 타입 파라미터를 인 위치에서만 사용해야 한다.

    ```kotlin
    interface Comparator<in T> {
        fun compare(e1: T, e2:T): Int { ... }
    }
    ```


![https://user-images.githubusercontent.com/58816862/223568440-7f354d82-e0c5-456a-891d-e509e8347a77.png](https://user-images.githubusercontent.com/58816862/223568440-7f354d82-e0c5-456a-891d-e509e8347a77.png)

- 클래스나 인터페이스가 어떤 타입 파라미터에 대해서 공변적이면서 다른 타입 파라미터에 대해서는 반공변적일 수도 있다.

    ```kotlin
    interface Function1<in P, out R> {
        operator fun invoke(p: P): R
    }
    ```


### 사용 지점 변성: 타입이 언급되는 지점에서 변성 지정

- 선언 지점 변성이란, 클래스를 선언하면서 변성을 지정 또는 클래스를 사용하는 모든 장소에 변성 지정자가 영향을 끼치는 것을 말한다.
- 사용 지점 변성이란, 타입 파라미터가 있는 타입을 사용할 때마다 해당 타입 파라미터를 하위 타입이나 상위 타입 중 어떤 타입으로 대치할 수 있는지 명시하는 것을 말한다.
- 자바에서는 사용 지점 변성만 지원하지만, 코틀린은 둘 다 지원한다.
- 코틀린에서 사용 지점 변성

    ```kotlin
    fun <T> copyData(source: MutableList<out T>, destination: MutableList<T>) {
        for (item in source) {
            destination.add(item)
        }
    }
    ```

  - 타입 파라미터를 사용하는 위치라면 어디에나 변성 변경자를 붙일 수 있다. 파라미터 타입, 로컬 변수 타입, 함수 반환 타입 등등
  - 변성 변경자가 붙은 곳에 타입 프로젝션이 일어난다.
    - 타입 프로젝션이란, 일반 타입을 제약을 가한 타입으로 만드는 것을 뜻한다.

### 스타 프로젝션: 타입 인자 대신 * 사용

- 스타 프로젝션이란, 제네릭 타입 인자 정보가 없음을 표현한다.
  - MutableList<*>는 어떤 정해진 구체적인 타입의 원소만을 담는 리스트지만 그 원소의 타입은 정확히 모른다는 사실을 표현한다.
  - MutableList<*>와 MutableList<Any?>는 다르다.
    - MutableList<*>는 MutableList<out Any?>처럼 동작한다.
      - 타입을 모르는 리스트에 원소를 마음대로 널 수는 없기 때문이다.
- 따라서 스타 프로젝션을 사용할 때는 값을 만들어내는 메소드만 호출할 수 있고 그 값의 타입에는 신경쓰지 말아야 한다.
