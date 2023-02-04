# Chapter06 코틀린 타입 시스템

## 6.1 널 가능성

널 가능성은 `NullPointerException`를 피할 수 있게 돕기 위한 코틀린 타입 시스템의 특성이다. 

코틀린을 비롯한 최신 언어에서 null에 대한 접근 방법은 가능한 이 문제를 실행시점에서 컴파일시점으로 옮기는 것이다. 

### 널이 될 수 있는 타입

코틀린과 자바의 첫 번째이자 가장 중요한 차이는 코틀린 타입 시스템이 널이 될 수 있는 타입을 명시적으로 지원하다는 점이다.

기본적으로 코틀린의 모든 타입은 널이 될 수 없는 타입이다. 뒤에 `?`가 붙어야 널이 될 수 있다.
`Type? = Type or null`

널이 될 수 있는 타입은 널이 될 수 없는 타입과 비교해 제약사항이 많다. 따라서 거의 null과 비교하는 작업이 중요하다.
```kotlin
fun strLenSafe(s: String?): Int = if (s != null) s.length else 0
```
- 문제점
  - 널이 될 수 있는 타입에는 항상 if 검사가 필요하다. (번잡함)

코틀린에서는 위 문제를 해결하기 위해 널이 될 수 있는 타입을 좀 더 편하게 사용할 수 있는 여러 도구를 제공해준다.

### 타입의 의미

타입은 분류로 타입은 어떤 값들이 가능한지와 그 타입에 대해 수행할 수 있는 연산의 종류를 결정한다.

자바의 경우 타입 분류를 잘 하고 있지 않다. 
```java
String nullStr = null
if (nullStr instanceof String) {
    System.out.println("nullStr is String type")
}
```
위 코드에서 instanceof 연산자는 nullStr이 String이 아니라고 한다. 즉, 자바의 타입 시스템이 널을 제대로 다루지 못하고 있다.

코틀린의 널이 될 수 있는 타입은 위 문제에 대한 종합적인 해법을 제공한다.

-Tip
  - 실행 시점에 널이 될 수 있는 타입이나 널이 될 수 없는 타입의 객체는 같다. (벼로의 실행 시점 부가 비용이 들지 않는다.)

### 안전한 호출 연산자: ?.

`?.`는 null 검사와 메소드 호출을 한 번의 연산으로 수행한다. 호출하려는 값이 null이 아니라면 ?.은 일반 메소드 호출처럼 작동한다. 하지만 호출하려는 값이
null이면 이 호출은 무시되고 null이 결과 값이 된다.
```kotlin
s?.toUpperCase() // 1번

if (s != null) s.toUpperCase() else null // 1번과 동일한 의미다.
```

### 엘비스 연산자: ?:

엘비스 연산자`?:`를 이용해 null 대신 사용할 디폴트 값을 지정을 편리하게 할 수 있다. 좌항 값이 널이 아니면 조항 값을 결과로 하고, 좌항 값이 널이면 우항 값을 결과로
한다.
```kotlin
fun strLenSafe(s: String?): Int = s?.length ?: 0
```

엘비스 연산자의 우항에 `return`, `throw`등의 연산을 넣을 수 있고, 이를 이용해 좌항이 널이면 즉시 어떤 값을 반환하거나 예외를 던질 수 있다.
이런 패턴은 함수의 전제 조건을 검사하는 경우 유용하다.
```kotlin
class Address(val streetAddress: String, val zipCode: Int, val city: String, val country: String)
class Company(val name: String, val address: Address?)
class Person(val name: String, val company: Company?)

fun printShippingLabel(person: Person) {
    val address = person.company?.address
      ?: throw IllegalArguementException("No address")
    with (address) {
        println(streetAddress)
        println("${zipCode} ${city} ${country}")
    }
}
```

### 안전한 케스트: as?

`as?` 연산자는 어떤 값을 지정한 타입으로 캐스트한다. `as?`는 값을 대상 타입으로 변환할 수 없으면 null을 반환한다.

안전한 케스트는 일반적으로 엘비스 연산자와 같이 사용된다.
```kotlin
class Person(val firstName: String, val lastName: String) {
    override fun equals(other: Any?): Boolean {
        val otherPerson = o as? Person ?: returen false
      
        return otherPerson.firstName == firstName && otherPerson.lastName == lastName
    }
  
    override fun hashCode(): Int{ ... }
}
```