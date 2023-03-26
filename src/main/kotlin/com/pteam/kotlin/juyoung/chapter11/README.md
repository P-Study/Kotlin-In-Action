# DSL 만들기

## API에서 DSL로

* 코드의 가독성과 유지보수성을 좋게 유지하는 게 궁극적 목표
* 깔끔한 API란?
    * 어떤 일이 벌어질지 명확하게 이해할 수 있어야 함
        * 이름과 개념을 적절하게 잘 선택하기
    * 불필요한 구문이나 번잡한 코드가 적은 간결한 코드
    * 코틀린이 지원하는 간결한 구문

| 일반 구문                       | 간결한 구문                                  | 사용한 언어 특성         |
|:----------------------------|:----------------------------------------|:------------------|
| StringUtil.capitalizes(s)   | s.capitalize()                          | 확장 함수             |
| 1.to(“one”)                 | 1 to “one”                              | 중위 호출             |
| set.add(2)                  | set += 2                                | 연산자 오버로딩          |
| map.get(“key”)              | map[“key”]                              | get 메소드에 대한 관례    |
| file.use({ f -> f.read() }) | file.use { it.read() }                  | 람다를 괄호 밖으로 빼내는 관례 |
| sb.append(“yes”)	           | with (sb) { append(“yes”) append(“no”)} | 수신 객체지정 람다        |

### 영역 특화 언어라는 개념

* 명령적인 범용 프로그래밍 언어와 달리 DSL은 더 선언적
* `명령적 언어` 어떤 연산을 완수하기 위해 필요한 각 단계를 순서대로 정확히 기술
* `선언적 언어` 원하는 결과를 기술하기만 하고 그 결과를 달성하기 위해 필요한 세부 실행은 언어를 해석하는 엔진에 맡김
* DSL을 범용 언어로 만든 호스트 애플리케이션과 함께 조합하기가 어렵다는 단점이 있음
    * 다른 언어에서 호출하려면 DSL 프로그램을 별도의 파일이나 문자열 리터럴로 저장해야 하는데
      이러면 컴파일 시점에 상호작용을 제대로 검증하거나, 디버깅하거나, IDE 기능을 제공하기 어려운 문제가 있음

### 내부 DSL

* 독립적인 문법 구조가 아닌 범용 언어로 작성된 프로그램의 일부로 범용 언어와 동일한 문법을 사용
* 내부 DSL은 SQL 질의가 돌려주는 결과 집합을 범용 언어의 객체로 변환하기 위해 노력할 필요가 없음

```sql
SELECT Country.name, COUNT(Customer.id)
FROM Country
         JOIN Customer
              ON Country.id = Customer.country_id
GROUP BY Country.name
ORDER BY COUNT(Customer.id) DESC
LIMIT 1
```

```kotlin
(Country join Customer)
    .slice(Country.name, Count(Customer.id)).selectAll()
    .groupBy(Country.name)
    .orderBy(Count(Customer.id), isAsc = false)
    .limit(1)
```

### DSL의 구조

* `구조 또는 문법` 다른 API에는 존재하지 않지만 DSL에만 존재
* 코틀린 DSL에서는 보통 람다를 중첩시키거나 메소드 호출을 연쇄시키는 방식으로 구조를 만듦
* 같은 문맥을 함수 호출 시마다 반복하지 않고도 재사용할 수 있다는 장점

```kotlin
// 람다 중첩을 통해 구조를 만든다
dependencies {
    compile("junit:junit:4.11")
    compile("com.google.inject:guice:4.1.0")
}

// 메소드 호출을 연쇄시켜 구조를 만든다
str should startWith("kot")
```

### 내부 DSL로 HTML 만들기

* 코틀린 코드로 HTML을 만드는 이유
    * 타입 안정성 보장
    * 일반 코틀린 코드이므로 코드를 원하는대로 사용 가능

```kotlin
fun createSimpleTable() = createHTML().table {
    val numbers = mapOf(1 to "one", 2 to "two")
    for ((num, string) in numbers) {
        tr {
            td { +"$num" }
            td { +string }
        }
    }
}
```

## 구조화된 API 구축: DSL에서 수신 객체 지정 DSL 사용

### 수신 객체 지정 람다와 확장 함수 타입

* 람다를 인자로 받는 buildString()

```kotlin
fun buildString(
    builderAction: (StringBuilder) -> Unit
): String {
    val sb = StringBuilder()
    builderAction(sb)
    return sb.toString()
}

val s = buildString {
    it.append("Hello, ")
    it.append("World!")
}
```

* 수신 개체 지정 람다를 사용한 buildString()

```kotlin
fun buildString(
    builderAction: StringBuilder.() -> Unit
): String {
    val sb = StringBuilder()
    sb.builderAction()
    return sb.toString()
}

val s = buildString {
    this.append("Hello, ")
    append("World!")    // this 생략 가능
}

// 수신 객체 지정람다를 변수에 저장하기
val appendExcl: StringBuilder.() -> Unit = { this.append("!") }
val stringBuilder = StringBuilder("Hi")

// appendExcl을 확장 함수처럼 사용
stringBuilder.appendExcl()
// appendExcl을 인자로 넘길 수 있음
buildString(appendExcl)
```

### 수신 객체 지정 람다를 HTML 빌더 안에서 사용

* `HTML 빌더` HTML을 만들기 위한 코틀린 DSL로 타입 안전한 빌더
* 수신 객체 지정 람다가 이름 결정 규칙을 바꿈
* `@DslMarker` 애노테이션을 사용해 중첩된 람다에서 외부 람다에 정의된 수신 객체를 접근하지 못하게 제한할 수 있음
* 수신 객체 지정 람다를 사용하면 코드 블록 내부에서 이름 결정 규칙을 바꿀 수 있으므로 이를 이용해 API에 구조를 추가할 수 있음

### 코틀린 빌더: 추상화와 재샤용을 가능하게 하는 도구

* 프로그램에서 일반 코드를 작성하는 경우 중복을 피하고 코드를 더 멋지게 만들기위해 반복되는 코드를 새로운 함수로 묶어서 이해하기 쉬운 이름을 붙일 수 있음
* 외부 DSL인 SQL이나 HTML을 별도 함수로 분리해 이름을 부여하기는 어렵지만, 내부 DSL을 사용하면 일반 코드와 마찬가지로 반복되는 내부 DSL 코드 조각을 새 함수로 묶어서 재사용 가능

## invoke 관례를 사용한 더 유연한 블록 중첩

* invoke 관례를 사용하면 객체를 함수처럼 호출 가능
    * 일상적으로 사용하라고 만든 기능은 아니라는 점에 유의
    * DSL에서는 invoke 관례가 아주 유용할 때가 자주 있음

### invoke 관례: 함수처럼 호출할 수 있는 객체

* 관례는 특별한 이름이 붙은 함수를 일반 메소드 호출 구문으로 호출하지 않고 더 간단한 다른 구문으로 호출할 수 있게 지원하는 기능
* invoke 메소드의 시그니처에 대한 요구사항은 없기 때문에 원하는 대로 파라미터 개수나 타입을 지정할 수 있음
* 여러 파라미터 타입을 지원하기 위해 오버로딩할 수 있음

```kotlin
class Greeter(val greeting: String) {
    operator fun invoke(name: String) {
        println("$greeting, $name!")
    }
}

val bavarianGreeter = Greeter("Servus")
barvarianGreeter("Dmitry")  // 내부적으로 bavarianGreeter.invoke("Dmitry")로 컴파일됨
```

### invoke 관례와 함수형 타입

* 일반적인 람다 호출 방식이 실제로는 invoke 관례를 적용한 것임을 알 수 있음
* 인라인하는 람다를 제외한 모든 람다는 함수형 인터페이스를 구현하는 클래스로 컴파일됨
* 각 함수형 인터페이스 안에는 그 인터페이스 이름이 가리키는 개수만큼 파라미터를 받는 invoke 메소드가 들어있음

```kotlin
// 함수 타입을 확장하면서 invoke() 오버라이딩하기
data class Issue(
    val id: String,
    val project: String,
    val type: String,
    val priority: String,
    val description: String
)

class ImportantIssuesPredicate(val project: String) : (Issue) -> Boolean {
    override fun invoke(issue: Issue): Boolean {
        return issue.project == project && issue.isImportant()
    }

    private fun Issue.isImportant(): Boolean {
        return type = "Bug" && (priority == "Major" || priority == "Critical")
    }
}

val i1 = Issue("IDEA-154445", "IDEA", "Bug", "Major", "Save settings failed")
val i2 = Issue(
    "KT- 12183",
    "Kotlin",
    "Feature",
    "Normal",
    "Intention: convert several calls on the same receiver to with/apply"
)

val predicate = ImportantIssuesPredicate("IDEA")
for (issue in listOf(i1, i2).filter(predicate)) {
    println(issue.id)
}
```

* 람다를 함수 타입 인터페이스를 구현하는 클래스로 변환하고 그 클래스의 invoke 메소드를 오버라이드하면 복잡한 람다가 필요한 구문을 리팩토링 할 수 있음
* 람다 본문에서 따로 분리해 낸 메소드가 영향을 끼치는 영역을 최소화할 수 있음

### DSL의 invoke 관례: 그레이들에서 의존관계 정의
