# 애노테이션과 리플렉션

- 어떤 함수를 호출하려면 그 함수가 정의된 클래스의 이름과 함수 이름, 파라미터 이름 등을 알아야만 했다.
- 애노테이션과 리플렉션을 사용하면 이런 제약을 벗어나서 미리 알지 못하는 임의의 클래스를 다룰 수 있다.
- 애노테이션을 사용하면 라이브럴리가 요구하는 의미를 클래스에게 부여할 수 있다.
- 리플렉션을 사용하면 실행 시점에 컴파일러 내부 구조를 분석할 수 있다.

## 애노테이션 선언과 적용

- 애노테이션에 메타데이터를 선언하면 애노테이션을 처리하는 도구가 컴파일 시점이나 실행 시점에 적절한 처리를 한다.

### 애노테이션 적용

- 애노테이션을 적요하려는 대상 앞에 붙이면 된다.
- “@ + 이름”으로 구성된다.

    ```kotlin
    import org.junit.*
    
    class MyTest {
    	@Test
    	fun testTrue() {
    		Assert.assertTrue(true)
    	}
    }
    ```

- 애노테이션에 인자를 지정하는 것은 자바와 약간 다르다.
    - 클래스를 인자로 지정할 떄는 “::class”를 클래스 이름 뒤에 넣어야 한다.
    - 인자로 들어가는 애노테이션의 이름 앞에 “@”를 넣지 않아야 한다.
    - 배열을 인자로 지정하려면 “arrayOf()” 함수를 사용한다.

### 애노테이션 대상

- 코틀린에서 한 선언의 결과가 자바 선언에 대응하는 경우가 있다.
- 이러한 경우 사용 지점 대상 선언으로 애노테이션을 붙일 요소를 정할 수 있다.

    ```kotlin
    @get: Rule
    val folder = TemporaryFolder()
    
    @Test
    fun testUsingTempFolder() {
    	val createdFile = folder.newFile("myfile.txt")
    	val createdFolder = folder.newFolder("subfolder")
    }
    ```

- 사용 지점 대상을 지정할 때 지원하는 대상 목록
    - property: 프로퍼티 전체 자바에서 선언된 애노테이션에는 이 시용 지점 대상 을 사용할수없다
    - field: 프로퍼티에 의해 생성되는 (뒷받침하는) 필드
    - get: 프로퍼티 게터
    - set: 프로퍼티 세터
    - receiver: 확장 함수나 프로퍼티의 수신 객체 파라미터
    - param: 생성자 파라미터
    - setparam: 세터 파라미터
    - delegate: 위임 프로퍼티의 위임 인스턴스를 담아둔 필드
    - file: 파일 안에 선언된 최상위 함수와 프로퍼티를 담아두는 클래스

### 애노테이션 선언

- 애노테이션 클래스는 오직 선언이나 식과 관련 있는 메타데이터의 구조를 정의하기 때문에 내부에 아무 코드도 들어있을 수 없다.

    ```kotlin
    annotation class JsonExclude
    ```

- 파라미터가 있는 애노테이션을 정의하려면 애노테이션 클래스의 주 생성자에 파라미터를 선언해야 한다.

    ```kotlin
    annotation class JsonName(val name: String)
    ```


### 메타애노테이션: 애노테이션을 처리하는 방법 제어

- 클래스에 적용할 수 있는 애노테이션을 메타애노테이션이라고 부른다.

    ```kotlin
    @Target(AnnotationTarget.ANNOTATION_CLASS)
    annotation class BindingAnnotation
    ```

    ```kotlin
    @BindingAnnotation
    annotation class MyBinding
    ```


### 애노테이션 파라미터로 클래스 사용

```kotlin
annotation class DeserializeInterface(val targetClass: KClass<out Any>)
```

```kotlin
interface Company {
    val name: String
}
data class CompanyImpl (override val name: String) : Company

data class Person(
  val name: String,
  @DeserializeInterface(CompanyImpl::class) val company: Company
)
```

### 애노테이션 파라미터로 제네릭 클래스 받기

```kotlin
annotation class CustomSerializer(
  val serializerClass: KClass<out ValueSerializer<*>>
)
```

```kotlin
interface ValueSerializer<T> {
	fun toJsonValue(value: T): Any?
	fun fromJsonValue(jsonValue: Any?): T
}

class DataSerializer : ValueSerializer<Date> {
}

data class Person(
  val name: String,
  @CustomSerializer(DataSerializer::class) val birthDate: Date
)
```

- 클래스를 인자로 받아야 하면 애노테이션 파라미터 타입에 `KClass<out 허용할 클래스 이름>`
- 제네릭 클래스를 인자로 받아야 하면 `KClass<out 허용할 클래스 이름<*>>`

## 리플렉션: 실행 시점에 코틀린 객체 내부 관찰

- 리플렉션은 실행 시점에 동적으로 객체의 프로퍼티와 메소드에 접근할 수 있게 해주는 방법이다.

### 코틀린 리플렉션 API: KClass, KCallable, KFunction, KProperty

- KClass를 사용하면 클래스 안에 있는 모든 선언을 열거하고 각 선언에 접근하거나 클래스의 상위 클래스를 얻는 등의 작업이 가능하다.

  사용하는 법

    ```kotlin
    class Person(val name: String, val age: Int)
    ```

    ```kotlin
    import kotlin.reflect.full.*
    
    val person = Person("Alice", 29)
    val kClass = person.javaClass.kotlin
    
    println(kClass.simpleName)
    >>> Person
    
    kClass.memberProperties.forEach { println(it.name) }
    >>> age
    >>> name
    ```

  정의

    ```kotlin
    interface KClass<T : Any> {
    	val simpleName: String?
    	val qualifiedName : String?
    	val members : Collection<KCallable<*>>
    	val constructors : Collection<KFunction<T>>
    	val nestedClasses : Collection<KClass<*>>
    	...
    }
    ```

- KCallable은 함수와 프로퍼티를 아우르는 공통 상위 인터페이스다.

    ```kotlin
    interface KCallable<out R> {
    	fun call(vararg args: Any?): R
    	...
    }
    
    fun foo(x: Int) = println(x)
    val kFunction = ::foo
    kFunction.call(42)
    >> 42
    ```

  - KFunction<Int, Unit>에는 파라미터와 반환 값 타입 정보가 들어있다.

    ```kotlin
    var counter = 0
    val kProperty = ::counter
    kProperty.setter.call(21)
    println(kProperty.get())
    >>> 21
    ```

  - KProperty를 통해서 프로퍼티 값을 얻을 수 있다.
- 코틀린 리플렉션 API 인터페이스 계층 구조

  ![https://user-images.githubusercontent.com/58816862/224944870-43c8a3d6-17f9-41ab-91a6-5665cd6b3205.png](https://user-images.githubusercontent.com/58816862/224944870-43c8a3d6-17f9-41ab-91a6-5665cd6b3205.png)