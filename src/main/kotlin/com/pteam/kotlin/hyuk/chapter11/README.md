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

## 11.2 구조화된 API 구축: DSL에서 수신 객체 지정 DSL 사용

### 수신 객체 지정 람다와 확장 함수 타입

수신 객체 지정 람다를 사용하면 구조화된 API를 만들 때 도움이 된다.

문법
```kotlin
String.(Int, Int) -> Unit
```
- `String` : 수신 객체 타입
- `(Int, Int)` : 파라미터 타입
- `Unit` : 반환 타입

수신 객체 지정 람다는 확장 함수 타입(extension function type)이다.
```kotlin
// 확장 함수 타입
val appendExcel : StringBuilder.() -> Unit = { this.append("!") }

// 일반 한수 타입
val excel : (StringBuilder) -> Unit = { it.append("!") }
```

### 수신 객체 지정 람다를 HTML 빌더 안에서 사용

- 빌더의 장점
  - 객체의 계층 구조를 선언적으로 정의할 수 있다. -> XML, UI 컴포넌트 레이아웃을 정의할 때 유용하다.
```kotlin
fun createSimpleTable() = creatHTML().
    table {
        tr {
            td { +"cell" }
        }
    }
```

빌더에 수신 객체 지정 람다를 사용하면 이름 결정 규칙을 결정할 수 있다. 
위 코드에서는 table 블록 안에서는 tr, tr 블록 안에서는 td를 사용할 수 있게 결정했다.

수신 객체 지정 람다를 이용해 이름 결정 규칙 정하기
```kotlin
open class Tag

class TABLE : Tag {
    fun tr(init : TR.() -> Unit)
}

class TR : Tag {
    fun td(init: TD.() -> Unit)
}

class TD : Tag
```

만약 빌더에 수신 객체 람다 대신 일반 람다를 사용하면 HTML 생성 코드 구문이 난잡해 질 것이다.
왜냐하면 생성 메소드를 호출할 때 HTML `it`를 붙이거나 적절히 파라미터 이름을 정의해야 하기 때문이다.

### 코틀린 빌더: 추상화와 재사용을 가능하게 하는 도구

내부 DSL을 사용하면 일반 코드와 마찬가지로 반복되는 내부 DSL 코드 조각을 새 함수로 묶어서 재사용할 수 있다.

## 11.3 invoke 관례를 사용한 더 유여한 블록 중첩

`invoke` 관례를 사용하면 객체를 함수처럼 호출할 수 있다.

단, 이 기능은 일상적으로 사용하려고 만든 기능이 아니다. 따라서 남용하면 이해하기 어려운 코드가 될 수 있다. 하지만 DSL에서는 아주 유용할 때가 자주 있다.

### invoke 관례: 함수처럼 호출할 수 있는 객체

관례 : 특별한 이름이 붙은 함수를 일반 메소드 호출 구문으로 호출하지 않고 더 간단한 다른 구문으로 호출할 수 있게 지원하는 기능.

`invoke 관례` 클래스 안 또는 확장 함수로 `operator` 변경자가 붙은 `invoke` 메소드를 정의하면 객체에 대해 괄호를 통해 `invoke` 함수를 호출할 수 있다.

### invoke 관례와 함수형 타입

일반적인 람다 호출 방식(람다 뒤에 괄호를 붙이는 방식)이 실제로는 `invoke` 관례를 적용한 것에 지나치지 않다.
따라서 이를 알면 복잡한 람다를 여러 메소드로 분리하면서도 여전히 분리 전의 람다처럼 외부에서 호출할 수 있는 객체를 만들 수 있다.
이런 접근 방법은 람다 본문에서 따로 분리해 낸 메소드가 영향을 끼치는 영역을 최소화할 수 있다는 장점이 있다.

### DSL의 invoke 관례: 그레이들에서 의존관계 정의

`invoke` 메서드를 사용하면 DSL API의 유연성을 훨씬 키울 수 있다.
