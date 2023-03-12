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
    - 자바와 달리 코틀린에서는 애노테이션 인자로 임의의 식을 허용한다. (?)
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
코틀린도 자바 애노테이션에 정의된 `value`를 특별하게 취급한다. (?)

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