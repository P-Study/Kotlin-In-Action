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

## 5.2 컬렉션 함수형 API

함수형 프로그래밍 스타일은 컬렉션을 다룰 때 유용하다. 대부분의 작업에 라이브러리 함수를 활용할 수 있고 그로 인해 코드를
아주 간결하게 만들 수 있다.

### 필수적인 함수: filter와 map

filter와 map은 컬렉션을 활용할 때 기반이 되는 함수다. 대부분의 컬렉션 연산을 이 두 함수를 통해 표현할 수 있다.

filter : 컬렉션을 이터레이션하면서 주어진 람다에 각 원소를 넘겨 람다가 true를 반환하는 원소만 모은다.
map : 주어진 람다를 컬렉션의 각 원소에 적용한 결과를 모아서 새 컬렉션을 만든다.

**example : 가장 나이 많은 사람의 이름 구하기**

ver1
```kotlin
people.filter { it.age == people.maxBy(Person::age)!!.age}
```
- 문제점
  - 목록에서 최댓값을 구하는 작업이 계속 반된다. (성능 저하)

ver2 - 불필요한 연산 없애기
```kotlin
val maxAge = people.maxBy(Person::age)!!.age
people.filter { it.age == maxAge }
```
- Tip
  - 람다를 인자로 받는 함수에 람다를 넘기면 겉으로 볼 때는 단순해 보이는 식이 내부 로직의 복잡도로 인해 실제로는 엄청나게 불합리한 계산식이 될 때가 있다.

### all, any, count, find: 컬렉션에 술어 적용

all : 컬렉션의 모든 원소가 특정 조건을 만족하는지 판단
```kotlin
val canBeInClub27 = { p: Person -> p.age <= 27 }

peope.all(canBeInClub27)
```

any : 컬렉션의 특정 조건을 만족하는 원소가 하나라도 있는지 판단
```kotlin
people.any(canBeInClub27)
```
- Tip
  - 가독성을 높이려면 any와 all 앞에 !를 붙이지 않는 편이 낫다.

count : 술어를 만족하는 원소의 개수 구하기
```kotlin
people.filter(canBeInClub27).count
```
- Tip
  - count가 있다는 사실을 잊고 컬렉션을 필터링한 결과의 크기를 가져오면 중간 컬렉션이 생겨 성능이 불필요한 연산을 하게 된다.

find : 술어를 만족하는 원소를 하나 찾기
```kotlin
people.find(canBeInClub27)
```

### groupBy: 리스트를 여러 그룹으로 이뤄진 맵으로 변경

```kotlin
val list = listOf("a", "ab", "b")
println(list.groupBy(String::first))
// output = {a=[a, ab], b=[b]}
```
연산의 결과는 컬렉션의 원소를 구분하는 특성이 키이고, 키 값에 따른 각 그룹이 값인 맵이다.

### flatMap과 flatten: 중첩된 컬렉션 안의 원소 처리

flatMap : 인자로 주어진 람다를 컬렉션의 모든 객체에 적용하고 람다를 적용한 결과 얻어지는 여러 리스트를 한 리스트로 한데 모은다.
```kotlin
val strings = listOf("abc", "def")
println(strings.flatMap { it.toList() })
// output = [a, b, c, d, e, f]
```

특별히 반환해야 할 내용이 없다면 flatten을 사용할 수 있다.
```kotlin
listOfLists.flatten()
```

- Tip
  - 컬렉션을 다루는 코드를 작성할 경우에는 원하는 바를 어떻게 일반적인 변환을 사용해 표현할 수 있는지 생각해보고 그런 변환을 제공하는 라이브러리 함수가 있는지 살펴보라