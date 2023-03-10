# Chapter10 애노테이션과 리플렉션

annotation을 사용하면 라이브러리가 요구하는 의미를 클래스에 부여할 수 있고, reflection을 사용하면 실행 시점에
컴파일러 내부 구조를 분석할 수 있다.

## 10.1 애노테이션 선언과 적용

### 애노테이션 적용

애너테이션을 적용하는 방법은 자바와 비슷하다.
```kotlin
@Deprecated("Use removeAt(index) instead.", ReplaceWith("removeAt(index)"))
fun remove(index: Int) { /* no operation */ }
```

애너테이션 인자를 지정하는 문법은 약간 다르다.
- 클래스를 애노테이션 인자로 저장할 때는 `::class`를 클래스 이름 뒤에 넣어야 한다.
  - ex) `@MyAnnotation(MyClass::class)`
- 다른 애노테이션을 인자로 지정할 때는 인자로 들어가는 애노테이션의 이름 앞에 `@`를 넣지 않아야 한다.
- 배열을 인자로 지정하려면 `arrayOf` 함수를 사용한다.
  - ex) `RequestMapping(path=arrayOf("/foo", "/bar"))`

추가로 애노테이션 인자는 컴파일 시점에 알 수 있어야 한다. 따라서 임의의 프로퍼티를 인자로 지정할 수 없다.

### 애노테이션 대상

코틀린 소스코드에서 한 선언을 컴파일한 결과가 여러 자바 선언과 대응하는 경우가 자주 있다. 이럴때 사용 지점 대상 선언으로
애노테이션을 붙일 요소를 정할 수 있다.

```kotlin
@get:Rule
val folder = TemporaryFolder()
```

사용 지점 대상을 지원하는 대상 목록
- `property` : 프로퍼티 전체
- `field` : 프로퍼티에 의해 생성되는 필드
- `get` : 프로퍼티 게터
- `set` : 프로퍼티 세터
- `receiver` : 확장 함수나 프로퍼티의 수신 객체 파라미터
- `param` : 생성자 파라미터
- `setparam` : 세터 파라미터
- `delegate` : 위임 프로퍼티의 위임 인스턴스를 담아둔 필드
- `file` : 파일 안에 선언된 최상위 함수와 프로퍼티를 담아두는 클래스

- Tip
    - 자바에서 선언된 애노테이션을 사용해 프로퍼티에 붙이는 경우 기본적으로 프로퍼티 필드에 그 애너테이션이 붙는다.
    - 자바와 달리 코틀린에서는 애노테이션 인자로 임의의 식을 허용한다. (ref - https://www.baeldung.com/java-annotation-attribute-value-restrictions)
    - 코틀린은 코틀린으로 선언한 내용을 자바 바이트코드로 컴파일하는 방법과 코틀린 선언을 자바에 노출하는 방법을 제어하기 위한 애노테이션을 많이 제공한다.

### 애노테이션 선언

애노테이션 클래스는 오직 선언이나 식과 관련 있는 메타데이터의 구조를 정의하기 때문에 내부에 아무 코드도 들어있을 수 없다.
```kotlin
annotation class JsonExclude
```

파라미터가 있는 애노테이션은 애노테이션 클래스의 주 생성자에 파라미터를 선언해야 한다.
```kotlin
annotation class JsonName(val name: String)
```

자바에서 선언한 애노테이션을 코틀린 구성 요소에 적용할 때는 `value`를 제외한 모든 인자에 대해 이름 붙인 인자 구문을 사용해야만 한다.
코틀린도 자바 애노테이션에 정의된 `value`를 특별하게 취급한다. (ref - https://stackoverflow.com/questions/6571827/is-value-a-java-keyword)

### 메타애노테이션: 애노테이션을 처리하는 방법 제어

메타애노테이션은 애노테이션 클래스에 적용할 수 있는 애노테이션을 의미한다.메타애노테이션들은 컴파일러가 애노테이션을 처리하는 방법을 제어한다.
```kotlin
@Target(AnnotationTarget.PROPERTY)
annotation class JsonExclude
```

메타애노테이션을 직접 만들어야 한다면 `Target`을 `ANNOTATION_CLASS`로 지정하면된다.
```kotlin
@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class BindingAnnotation

@BindingAnnotation
annotation class MyBinding
```
### 애노테이션 파라미터로 클래스 사용

선언 예시
```kotlin
annotation class DeserializeInterface(val targetClass: KClass<out Any>)
```

사용 예시
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

선언 예시
```kotlin
annotation class CustomSerializer(
  val serializerClass: KClass<out ValueSerializer<*>>
)
```

사용 예시

```kotlin
class DataSerializer : ValueSerializer<Date> {
  // ...
}

data class Person(
  val name: String,
  @CustomSerializer(DataSerializer::class) val birthDate: Date
)
```

- Tip
  - 애노테이션이 클래스를 인자로 받을 때
    - 파라미터 타입에 `KClass<out 허용할 클래스 이름>`
  - 애노테이션이 제네릭 클래스를 인자로 받을 때
    - 파라미터 타입에 `KClass<out 허용할 클래스<*>>`

## 10.2 리플렉션: 실행 시점에 코틀린 객체 내부 관찰

리플렉션은 실행 시점에 객체의 프로퍼티와 메소드에 접근할 수 있게 해주는 방법이다.

코틀린에서 리플렉션을 사용하려면 자바 리플렉션 API, 코틀린 리플렉션 API를 다뤄야한다.

### 코틀린 리플렉션 API: KClass, KCallable, KFunction, KProperty

![이미지](./IMG_0474.jpg)

`KClass`는 클래스를 표현한다. `KClass`를 이용하면 클래스 안에 있는 모든 선언을 열거하고 각 선언에 접근하거나 클래스의 상위 클래스를 얻는 등의 작업이 가능하다.
```kotlin
val person = Person("Alice", 30)
val kClass = person.javaClass.kotlin
```

`KCallable`은 함수와 프로퍼티를 아우르는 공통 상위 인터페이스다. 그 안에 `call` 메소드를 사용하면 함수나 프로퍼티의 게터를 호출할 수 있다.
```kotlin
fun foo(x: Int) = println(x)
val kFunction = ::foo
kFunction.call(42)
```
더 구체적인 메소드를 사용해 함수를 호출할 수 있다. `KFunction1<Int, Unit>`은 파라미터와 반환 값 타입 정보가 들어가 있다.
- Tip
  - `KFunction`의 인자 타입과 반환 타입을 모두 다 안다면 `invoke` 메소드를 호출하는 게 낫다. `call` 메소드는 모든 타입의 함수에 적용할 수 있는 일반적인 메소드지만 타입 안전성을 보장하지 않는다.

`KProperty`를 이용해 프로퍼티 값을 얻을 수 있다.
```kotlin
var counter = 0
val kProperty = ::counter
kProperty.setter.class(21)
kProperty.get()
```
- 최상위 프로퍼티는 `KProperty0` 인터페이스의 인스턴스로 표현된다. `KProperty0` 안에는 인자가 없는 `get` 메소드가 있다.
- 맴버 프로퍼티는 `KProperty1` 인스턴스로 표현된다. 그 안에는 인자가 1개인 `get` 메소드가 들어있다. 인자로 프로퍼티를 얻고자 하는 객체 인스턴스를 넘겨야 한다.
