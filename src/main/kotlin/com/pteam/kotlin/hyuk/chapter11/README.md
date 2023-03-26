# Chapter10 DSL 만들기

## 11.1 API에서 DSL로

프로젝트를 계속 유지 보수하기 위해서는 클래스간 상호 작용(API)를 이해하기 쉽고 명확하게
표현할 수 있게 만들어야 한다. 

이해하기 쉽고 명확한 API의 특징
- 코드를 읽는 독자가 어떤 일이 벌어질지 명확하게 이해할 수 있어야 한다.
  - 좋은 이름과 개념의 선택이 이를 가능하게 만든다.
- 코드가 간결해야 한다.
  - 불필요한 구문이나 번잡한 준비 코드가 가능한 적어야 한다.

### 영역 특화 언어라는 개념

프로그래밍 언어는 크게 2가지로 구분할 수 있다.
- 범용 프로그래밍 언어 (general-purpose programming language)
  - 컴퓨터로 풀 수 있는 모든 문제를 충분히 풀 수 있는 기능을 제공하는 언어
- 영역 특화 언어 (domain-specific language)
  - 특정 과업 또는 영역에 초점을 맞추고 그 영역에 필요하지 않은 기능을 없언 언어
    - ex) SQL, 정규식

DSL의 장단점
- 장점
  - 더 효율적으로 목표를 달성할 수 있다. 
  - 범용 프로그래밍 언어와 달리 더 선언적이다.
    - 원하는 결과를 기술하기만 하고 그 결과를 달성하기 위해 필요한 세부 실행은 언어를 해석하는 엔진에 맡긴다.
- 단점
  - 범용 언어로 만든 호스트 애플리케이션과 함께 조합하기 어렵다.

요즘엔 DSL의 단점을 극복할 수 있는 개념인 Internal DSL 개념이 유명해지고 있다.

### 내부(Internal) DSL

- 외부(external) DSL
  - 독립적인 문법 구조를 갖는다.
- 내부(internal) DSL
  - 범용 언어로 작성된 프로그램의 일부며, 범용 언어와 동일한 문법을 사용한다.

external DSL example
```sql
SELECT Country.name, COUNT(Customer.id)
FROM Country JOIN Customer ON Country.id = Customer.country_id
GROUP BY Country.name
ORDER BY COUNT(Customer.id) DESC 
LIMIT 1
```

internal DSL example
```kotlin
(Country join Customer)
    .slice(Country.name, Count(Customer.id))
    .selectAll()
    .groupBy(Country.name)
    .orderBy(Count(Customer.id), isAsc = false)
```

### DSL 구조

DSL과 일반 API 사이에 잘 정의된 일반적인 경계는 없다.
하지만 DSL에는 구조 또는 문법이 존재하지만 API에는 존재하지 않는다.

DSL vs API in gradle
```kotlin
// DSL
dependencies {
    compile("junit:junit:4.11")
    compile("com.google.inject:guice:4.1.0")
}

// API
project.dependencies.add("compile", "junit:junit:4.11")
project.dependencies.add("compile", "com.google.inject:guice:4.1.0")
```

DSL vs API in assertion
```kotlin
// DSL
str should startWith("kot")

// API
assertTrue(str.startsWith("kot"))
```
