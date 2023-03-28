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

```kotlin
class DependencyHandler {
    // 일반적인 명령형 API 정의
    fun compile(coordinate: String) {
        println("Added dependency on $coordinate")
    }

    // invoke를 정의해 DSL 스타일 API를 제공
    operator fun invoke(body: DependencyHandler.() -> Unit) { // invoke를 정의해 DSL 스타일 API를 제공
        body() // this.body()
    }
}

val dependencies = DependencyHandler()
dependencies.compile("org.jetbrains.kotlin:kotlin-stdlib:1.0.0")

dependencies {
    compile("org.jetbrains.kotlin:kotlin-stdlib:1.0.0")
}
```

* 두 번째 호출은 다음과 같이 변환됨

```kotlin
dependencies.invoke({
    this.compile("org.jetbrains.kotlin:kotlin-stdlib:1.0.0")
})
```

* dependencies를 함수처럼 호출하면 람다를 인자로 넘김
* 람다의 타입은 확장 함수 타입(수신 객체를 지정한 함수 타입)
* 지정한 수신 객체 타입은 DependencyHandler
* invoke 메소드는 이 수신 객체 지정 람다를 호출
* invoke 메소드가 DependencyHandler의 메소드이므로 이 메소드 내부에서 this는 DependencyHandler 객체
* 따라서 invoke 안에서 DependencyHandler 타입의 객체를 따로 명시하지 않고 compile()을 호출할 수 있음

## 실전 코틀린 DSL

### 중위 호출 연쇄: 테스트 프레임워크의 should

* 깔끔한 구문은 내부 DSL의 핵심 특징
* 깔끔하게 만드려면 코드에 쓰이는 기호의 수를 줄여야함

```kotlin
infix fun <T> .should(matcher: Matcher<T>) = matcher.test(this)

interface Matcher<T> {
    fun test(value: T)
}

class startWith(val prefix: String) : Matcher<String> { // 클래스의 첫 글자가 대문자가 아닌 케이스: DSL 에서는 종종 명명규칙을 벗어나야 할 때가 있음
    override fun test(value: String) {
        if (!value.startsWith(prefix)) {
            throw AssertionError("String $value does not start with $prefix")
        }
    }
}

"kotlin" should startWith("kot")
```

```kotlin
object start

infix fun String.should(x: start): StartWrapper = StartWrapper(this)

class StartWrapper(val value: String) {
    infix fun with(prefix: String) = if (!value.startsWith(prefix)) {
        throw AssertionError("String $value does not start with $prefix")
    } else {
        Unit
    }
}

"kotlin" should start with "kot"
```

* start 객체는 함수에 데이터를 넘기기 위해서가 아니라 DSL의 문법을 정의하기 위해 사용
* start를 인자로 넘겨서 적절한 함수를 선택하고, 그 함수를 호출한 결과로 startWrapper 인스턴스를 받을 수 있음
* startWrapper 클래스에는 단언문의 검사를 실행하기 위해 필요한 값을 인자로 받는 with라는 멤버가 있음
* 중위호출과 object로 정의한 싱글턴 객체 인스턴스를 조합하면 DSL에 복잡한 문법을 도입할 수 있음

### 원시 타입에 대한 확장 함수 정의: 날짜 처리

```kotlin
import java.time.Period
import java.time.LocalDate

val Int.days: Period
    get() = Period.ofDays(this) // this는 상수의 값을 가리킴

val Period.ago: LocalDate
    get() = LocalDate.now() - this  // 연산자 구문을 사용해 LocalDate.minus를 호출

val Period.fromNow: LocalDate
    get() = LocalDate.now() + this

println(1.days.ago)
println(1.days.fromNow)
```

* days는 Int 타입의 확장 프로퍼티
* LocalDate라는 JDK 클래스에는 코틀린의 -는 연산자 관례와 일치하는 인자가 하나뿐인 minus 메소드가 들어가 있음 (public LocalDate minus(TemporalAmount var1))
* 따라서 -, + 는 LocalDate 라는 JDK 클래스에 있는 관례에 의해 minus, plus 메소드가 호출되는 것(코틀린에서 제공하는 확장함수가 호출되는 것이 아님)

### 멤버 확장 함수: SQL을 위한 내부 DSL

* 클래스안에서 확장함수와 확장프로퍼티를 선언
* 정의한 확장 함수나 확장프로퍼티는 그들이 선언된 클래스의 멤버인 동시에 그들이 확장하는 다른 타입의 멤버이기도 함
* 이러한 함수나 프로퍼티를 `멤버 확장` 이라고 부름

```kotlin
// 익스포즈드에서 테이블 선언
object Country : Table() {
    val id = integer("id").autoIncrement().primaryKey()
    val name = varchar("name", 50)
}

// Table 밖에서는 이 함수들을 호출할 수 없음
class Table {
    fun integer(name: String): Column<Int>
    fun varchar(name: String, length: Int): Column<String>
    fun <T> Column<T>.primaryKey(): Column<T>    // 기본키
    fun Column<Int>.autoIncrement(): Column<Int> // 숫자 타입의 컬럼만 자동 증가 컬럼으로 설정
    //...
}

```

* 멤버확장으로 정의해야하는 이유는 메소드가 적용되는 범위를 제한하기 위함
* 대신 제한된 범위로 인해 멤버확장은 확장성이 떨어진다는 단점도 있음

### 안코: 안드로이드 UI를 동적으로 생성하기

```kotlin
// Anko를 사용해 안드로이드 경고창 표시하기
fun Activity.showAreYouSureAlert(process: () -> Unit) {
    alert(
        title = "Are you sure?",
        message = "Are you really sure?"
    ) {
        positiveButton("Yes") { process() } // this 생략 (AlertDialogBuilder)
        negativeButton("No") { cancel() }
    }
}

// alert API의 선언
fun Context.alert(
    message: String,
    title: String,
    init: AlertDialogBuilder.() -> Unit
)

class AlertDialogBuilder {
    fun positiveButton(text: String, callback: DialogInterface.() -> Unit)
    fun negativeButton(text: String, callback: DialogInterface.() -> Unit)
}
```