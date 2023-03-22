# 애노테이션과 리플렉션

## 애노테이션 선언과 적용

### 애노테이션 적용

* 자바와 동일한 방법으로 애노테이션 사용
* @Deprecated
    * 자바와 의미는 같음
    * 코틀린에서는 replceWith 파라미터를 통해 옛 버전을 대신할 수 있는 패턴을 제시할 수 있음
    * API 사용자는 그 패턴을 보고 지원이 종료될 API 기능을 더 쉽게 새 버전으로 포팅할 수 있음

```kotlin
@Deprecated("Use removeAt(index) instead.", ReplaceWith("removeAt(index)"))
fun remove(index: Int)
```

* 애노테이션 인자를 지정하는 문법(자바와 약간 다름)
    * 클래스를 애노테이션 인자로 지정 `::class`를 뒤에 넣어야 함: `@MyAnnotation(MyClass::class)`
    * 다은 애노테이션을 인자로 지정할 때는 인자로 들어가는 애노테이션의 이름 앞에 @를 넣지 않아야 함
        * ReplaceWith는 애노테이션이지만 @Deprecated의 인자로 들어가므로 @를 사용 안 함
    * 배열을 인자로 지정하려면 `arrayOf` 사용: @RequestMapping(path = arrayOf("/foo", "/bar"))
        * 자바에서 선언한 애노테이션 클래스를 사용하면 value라는 이름의 파라미터가 필요에 따라 자동으로 가변 길이 인자로 변환되기 때문에 arrayOf를 사용하지 않아도 됨
* 애노테이션 인자를 컴파일 시점에 알 수 있어야 하기 때문에 임의의 프로퍼티를 인자로 지정할 수 없음
* 프로퍼티를 애노테이션 인자로 사용하려면 그 앞에 const 변경자를 붙여야 함
    * 컴파일러는 const가 붙은 프로퍼티를 컴파일 시점 상수로 취급함

```kotlin
const val TEST_TIMEOUT = 100L

@Test(timeout = TEST_TIMEOUT)
fun testMethod() {
}
```

### 애노테이션 대상

* 애노테이션을 붙일 때 어떤 요소에 붙일지 표시할 필요가 있음
* `사용 지점 대상 선언` @ 기호와 애노테이션 이름 사이에 붙으며, 애노테이션 이름과는 콜론으로 분리됨
    * `@get:Rule` get은 @Rule 애노테이션을 프로퍼티 게터에 적용하라는 뜻
* 코틀린의 필드는 기본적으로 비공개

```kotlin
class HashTempFolder {
    @get:Rule
    val folder = TemporaryFolder()

    @Test
    fun testUsingTempFolder() {
        val createdFile = folder.newFile("file.txt")
        val createdFolder = folder.newFolder("subfolder")
    }
}
```

* 코틀린으로 애노테이션을 선언하면 프로퍼티에 직접 적용할 수 있는 애노테이션을 만들 수 있음
* 사용 지점 대상을 지정할 때 지원하는 대상 목록
    * `property` 프로퍼티 전체. 자바에서 선언된 애노테이션에는 이 사용 지점 대상을 사용할 수 없음
    * `field` 프로퍼티에 의해 생성되는(뒷받침 하는) 필드
    * `get` 프로퍼티 게터
    * `set` 프로퍼티 세터
    * `receiver` 확장 함수나 프로퍼티의 수신 객체 파라미터
    * `param` 생성자 파라미터
    * `setparam` 세터 파라미터
    * `delegate` 위임 프로퍼티의 위임 인스턴스를 담아둔 필드
    * `file` 파일 안에 선언된 최상위 함수와 프로퍼티를 담아두는 클래스

### 애노테이션을 활용한 JSON 직렬화 제어

* 제이키드 라이브러리는 기본적으로 모든 프로퍼티를 직렬화하며 프로퍼티 이름을 키로 사용함
* `@JsonExclude` 직렬화나 역직렬화 시 그 프로퍼티를 무시할 수 있음
* `@JsonName` 프로퍼티를 표현하는 키/값 쌍의 키로 프로퍼티 이름 대신 애노테이션이 지정한 이름을 쓰게 할 수 있음

### 애노테이션 선언

* 자바에서는 value 메소드를 사용하며, 어떤 애노테이션을 적용할 때 value를 제외한 모든 애트리뷰트에는 이름을 명시해야 함
* 자바에서 선언한 애노테이션을 코틀린의 구성 요소에 적용할 때는 value를 제외한 모든 인자에 대해 이름 붙인 인자 구문을 사용해야 함

```kotlin
annotation class JsonExclude

// 주 생성자에 파라미터를 선언해야 함
annotation class JsonName(val name: String)
```

### 메타애노테이션: 애노테이션을 처리하는 방법 제어

* 애노테이션 클래스에 적용할 수 있는 애노테이션을 메타애노테이션이라고 함
* `@Target` 표준 라이브러리의 메타애노테이션 중 가장 흔히 쓰임
* 대상을 property로 지정한 애노테이션은 자바에서 사용 불가
    * field를 두번째 대상으로 추가하면 코틀린 프로퍼티와 자바 필드에 적용할 수 있음

```kotlin
@Target(AnnotationTarget.PROPERTY)
annotation class JsonExclude
```

* 메타애노테이션을 직접 만들어야 한다면 `ANNOTATION_CLASS`를 대상으로 지정

```kotlin
@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class BindingAnnotation

@BindingAnnotation
annotation class MyBinding
```

* `@Retention` 자바 컴파일러는 기본적으로 애노테이션을 .class 파일에는 저장하지만 런타임에는 사용할 수 없게 함
* 대부분의 애노테이션은 런타임에서도 사용할 수 있어야 하므로 코틀린에서는 기본적으로 애노테이션의 @Retention을 런타임으로 지정
    * 따라서 제이키드 애노테이션에 별도로 @Retention 메타 애노테이션을 붙일 필요가 없음

### 애노테이션 파라미터로 클래스 사용

* 어떤 클래스를 선언 메타데이터로 참조할 수 있는 기능이 필요할 때 클래스 참조를 파라미터로 하는 애노테이션 클래스를 선언하면 됨
* `@DeserializeInterface` 인터페이스 타입인 프로퍼티에 대한 역직렬화 제어할 때 쓰는 애노테이션
* 참조 인자를 받는 애노테이션 정의하기
    * 코틀린 클래스에 대한 참조를 저장할 때 KClass 타입을 사용
    * out 변경자 없이 KClass<Any>라고 쓰면 오직 Any::class만 넘길 수 있음
    * out을 붙이면 모든 코틀린 타입 T에 대해 KClass<T>가 KClass<out Any>의 하위 타입이 됨(공변성)

```kotlin
interface Company {
    val neme: String
}

data class CompanyImpl(override val name: String) : Company
data calss Person(
        val name : String,
@DeserializeInterface(CompanyImpl::class)
val company: Company
)

annotation class DeserializeInterface(val targetClass: KClass<out Any>)
```

### 애노테이션 파라미터로 제네릭 클래스 받기

* 기본 동작을 변경하고 싶으면 값을 직렬화하는 로직을 직접 제공하면 됨
* `@CustomSerializer` 애노테이션은 커스텀 직렬화 클래스에 대한 참조를 인자로 받음

```kotlin
interface ValueSerializer<T> {
    fun toJsonValue(value: T): Any?
    fun fromJsonValue(jsonValue: Any?): T
}

data class Person(
        val name: String,
        @CustomSerializer(DateSerializer::class) val birthDate: Date
)

annotation class CustomSerializer(
        // 어떤 타입에 대해 쓰일지 알 수 없으므로 스타 프로젝션 사용
        val serializerClass: KClass<out ValueSerializer<*>>
)
```

## 리플렉션: 실행 시점에 코틀린 객체 내부 관찰

* `리플렉션` 실행시점에 동적으로 객체의 프로퍼티와 메소드에 접근할 수 있게 해주는 방법
* 코틀린에서 리플렉션을 사용하려면 두 가지 서로 다른 리플렉션 API를 다뤄야 함
    * 자바가 java.lang.reflect 패키지를 통해 제공하는 표준 리플렉션
        * 리플렉션을 사용하는 자바 라이브러리와 코틀린 코드가 완전히 호환됨
    * 코틀린이 kotlin.reflect 패키지를 통해 제공하는 코틀린 리플렉션 API
        * 자바에 없는 프로퍼티나 널이 될 수 있는 타입과 같은 코틀린 고유 개념에 대한 리플렉션을 제공

### 코틀린 리플렉션 API: KClass, KCallable, KFunction, KProperty

* KClass
    * ::class를 쓰면 KClass의 인스턴스를 얻음
    * 실행 시점의 클래스를 얻으려면 먼저 객체의 javaClass 프로퍼티를 사용해 객체의 자바 클래스를 얻어야 함
    * .kotlin 확장 프로퍼티를 통해 자바에서 코틀린 리플렉션 API로 옮길 수 있음 (.javaClass.kotlin)
* KCallable
    * 클래스의 모든 멤버의 목록이 KCallable 인스턴스의 컬렉션
    * `call`을 사용해 함수나 프로퍼티의 게터를 호출(vararg 리스트로 함수 인자를 전달)
* KFunction

```kotlin
fun foo(x: Int) = println(x)
val kFunction = ::foo
kFunction.call(42)
```

* ::foo 식의 값 타입이 KFunction 클래스의 인스턴스
* 이 함수 참조가 가리키는 함수를 호출하려면 KCallable.call 메소드를 호출
* call에 넘긴 인자 개수와 원래 함수에 정의된 파라미터 개수가 일치해야 함
* KFunction1<Int, Unit>
    * 1은 이 함수의 파라미터가 1개라는 의미
    * invoke 메소드 사용해서 호출, kFunction을 직접 호출할 수도 있음
    * 인자 타입과 반환 타입을 모두 다 안다면 invoke 메소드를 호출하는 게 나음
* call 메소드는 모든 타입의 함수에 적용할 수 있지만 타입 안전성을 보장해주지 않음

* KProperty
    * 프로퍼티의 게터를 호출하지만 프로퍼티 인터페이스는 프로퍼티 값을 얻는 더 좋은 방법으로 get 메소드 제공
    * 최상위 프로퍼티는 KProperty0 인터페이스의 인스턴스로 표현, 인자가 없는 get 메소드가 있음
    * 멤버 프로퍼티는 KProperty1 인스턴스로 표

### 리플렉션을 사용한 객체 직렬화 구현

* 기본적으로 직렬화 함수는 객체의 모든 프로퍼티를 직렬화함
* 원시타입, 문자열 > Json 수, 불리언, 문자열 값 등으로 변환
* 컬렉션은 Json 배열로 직렬화
* 그 외 다른 타입인 프로퍼티는 중첩된 Json 객체로 직렬화

```kotlin
private fun StringBuilder.serializeObject(obj: Any) {
    val kClass = obj.javaClass.kotlin // 객체의 KClass를 얻음         
    val properties = kClass.memberProperties // 클래스의 모든 프로퍼티를 얻음       
    properties.joinToStringBuilder(
            this, prefix = "{", postfix = "}") { prop ->
        serializeString(prop.name) // 프로퍼티 이름                     
        append(": ")
        serializePropertyValue(prop.get(obj)) // 프로퍼티 값       
    }
}
```

### 애노테이션을 활용한 직렬화 제어

* @JsonExclude로 애노테이션한 프로퍼티를 제외

```kotlin
private fun StringBuilder.serializeObject(obj: Any) {
    obj.javaClass.kotlin.memberProperties
            .filter { it.findAnnotation<JsonExclude>() == null }
            .joinToStringBuilder(this, prefix = "{", postfix = "}") {
                serializeProperty(it, obj)
            }
}
```