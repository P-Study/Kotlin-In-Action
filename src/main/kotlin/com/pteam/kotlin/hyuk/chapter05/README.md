# Chapter05 람다로 프로그래밍

람다 식(lambda expression)는 기본적으로 다른 함수에 넘길 수 있는 작은 코드 조각을 의미한다. 람다를 사용하면 쉽게 공용 코드 구조를 라이브러리 함수로 뽑아낼 수 있다. 즉, 함수의 유연성이
올라간다.

## 5.1 람디 식과 맴버 참조

### 람다 소개

람다가 등장하기 전에 코드 블록을 함수에 넘기거나 변수에 저장하기 위해서는 무명 내부 클래스를 사용했다. 이는 상당히 번거롭다.

함수형 프로그래밍에서는 이런 문제를 함수를 값처럼 다루는 접근 방식으로 해결한다. 그리고 이때 람다를 활용한다. 람다를 활용하면 코드가 더욱 더 간결해진다.

무명 내부 객체 사용
```java
button.setOnClickListner(new OnClickListner){
    @Override
    public void onClick(View view){
	    /* operation */
	}
}
```

람다 활용
```kotlin
button.setOnClickListner { /* operation */ }
```

### 람다와 컬렉션

코드 중복을 제거하는 것은 프로그래밍 스타일을 개선하는 중요한 방법 중 하나다.
컬렉션을 다룰 때 수행하는 대부분의 작업은 몇 가지 일반적인 패턴에 속한다. 따라서 그런 패턴은 라이브러리
안에 있어야 한다.

이런걸 람다가 가능하게 해준다. 코틀린은 람다를 활용해 유연한 컬렉션 라이브러리를 제공한다.

컬력션 직접 검색
```kotlin
fun findTheOldest(people: List<Person>) {
    var maxAge = 0
    var theOldest: Person? = null
    
    for (person in people) {
        if (person.age > maxAge) {
            maxAge = person.age
            theOldest = person
        }
    }
    println(theOldest)
}
```
위 코드는 루프에 상당히 많은 코드가 들어있어 실수를 저지르기 쉽다.

람다 사용 컬렉션 검색
```kotlin
val people = listOf(Person("Alice", 29), Person("Bob", 31))
theOldest = people.maxBy { it.age }
```

### 람다 식의 문법

기본 문법
```kotlin
{ x: Int, y: Int -> x + y }
```

람다 표현 방법 히스토리 (모든 버전 사용 가능하다. 상황에 맞춰 사용하자)

ver1
```kotlin
people.maxBy({ p: Person -> p.age})
```
- 문제점
  - 구분자가 너무 많아 가독성이 떨어진다.
  - 컴파일러가 추론할 수 있는 타입은 굳이 명시할 필요 없다.
  - 인자가 단 하나뿐인 경우 굳이 인자에 이름을 붙이지 않아도 된다.

ver2
```kotlin
people.maxBy() { p: Person -> p.age }
```
코틀린에서 함수 호출 시 맨 뒤에 있는 인자가 람다 식이라면 그 람다를 괄호 밖으로 뺼 수 있다.

ver3
```kotlin
people.maxBy { p: Person -> p.age }
```
람다가 함수의 유일한 인자고 괄호 뒤에 람다를 썼다면 호출 시 빈 괄호를 없애도 된다.

ver4
```kotlin
people.maxBy { p -> p.age}
```
컴파일러가 타입추론할 수 있는경우 타입을 생략할 수 있다.
- Tip
  - 컴파일러가 파라미터 타입 중 일부를 추론하지 못하거나 타입 정보가 코드를 읽을 때 도움이 된다면 그렇게 일부 타입만 표시하면 편하다.

ver5
```kotlin
people.maxBy { it.age }
```
파라미터가 하나일 경우 이름을 따로 지정하지 않아도 된다. 그리고 이때는 `it`이름을 사용한다.
- Tip
  - it를 사용하는 관습은 코드를 아주 간단하게 만들어주지만 남용하면 안된다. 람다 안에 람다가 중첩되는 경우 각 파라미터를 명시하는 편이 낫다.

### 현재 영역에 있는 변수에 접근

람다를 함수 안에서 정의하면 파라미터뿐 아니라 람다 정의의 앞에 선언된 로컬 변수까지 람다에서 사용할 수 있다.
추가로 자바와 다르게 람다 안에서 파이널 변수가 아닌 변수에도 접근할 수 있다. (Q? 안티패턴 아님? 디버깅이 너무 힘들 것 같음..)

이처럼 람다 안에서 사용하는 외부 변수를 `람다가 포획한 변수`라고 부른다.

ex
```kotlin
fun printProblemCounts(response: Collection<String>) {
    var clientErrors = 0
    var serverErrors = 0
    response.forEach {
        if (it.startsWith("4")) {
            clientErrors++
        } else if (it.startsWith("5")) {
            serverErrors++
        }
    }
    println("${clientErrors} client errors, ${serverErrors} server errors")
}
```
- WARN!
  - 어떤 함수가 자신의 로컬 변수를 포획한 람다를 반환하거나 다른 변수에 저장한다면 로컬 변수의 생명주기와 함수의 생명주기가 달라질 수 있다.

### 맴버 참조

맴버 참조(member reference)는 프로퍼티나 메소드를 단 하나만 호출하는 함수 값을 만들어준다.

basic example
```kotlin
people.maxBy(Person::age)

```

최상위 함수 참조
```kotlin
fun salute() = println("Salute!")
run(::salute)
```
- WARN
  - REPL이나 스크립트에서는 ::salute를 사용할 수 없다.

인자가 여럿인 다른 함수
```kotlin
val action = { person: Person, message: String -> 
  sendEmail(person, message)
}
val nextAction = ::snedEmail // 람다 대신 맴버 참조 사용
```

생성자 참조
```kotlin
data class Person(val name: String, val age: Int)

val createPerson = ::Person
```
- 생성자 참조를 사용하면 생성 작업을 연기하거나 저장해둘 수 있다.

확장 함수 참조
```kotlin
fun Person.isAdult() = age >= 21
val predicate = Person::isAdult
```

바운드 맴버 참조
```kotlin
val p = Person("Dmitry", 34)
val dmitrysAgeFunction = p::age
```
- 맴버 참조를 생성할 때 클래스 인스턴스를 함께 저장한 다음 나중에 그 인스턴스에 대해 맴버를 호출해준다.