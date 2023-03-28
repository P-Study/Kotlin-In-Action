# DSL 만들기

## API에서 DSL로

- 깔끔한 API 특징
  - 코드를 읽는 독자들이 어떤 일이 벌어질지 명확하게 이해할 수 있어야 한다. → 적절한 이름과 개념을 사용하는 것
  - 코드가 간결해야 한다. → 불필요한 구문이나 번잡한 코드는 가능하면 적어야 한다
- 코틀린에서는 API를 깔끔하게 짤 수 있도록 돕는다.

  ![https://user-images.githubusercontent.com/58816862/228224561-b591e007-1abf-4712-920e-ffcfb56680fa.png](https://user-images.githubusercontent.com/58816862/228224561-b591e007-1abf-4712-920e-ffcfb56680fa.png)


### 영역 특화 언어라는 개념

- 범용적(general-purpose) 프로그래밍 언어
  - 컴퓨터가 퓨터가 발명된 초기부터 컴퓨터로 풀 수 있는 모든 문제를 충분히 풀 수 있는 기능을 제공한다.
  - 명령적 → 어떤 연산을 완수하기 위해 필요한 각 단계를 순서대로 정확히 기술한다.
- 영역 특화 언어 → DSL
  - 특정 과업 또는 영역에 초점을 맞추고 그 영역에 필요하지 않은 기능을 없앤 영역 특화 언어이다. 예를 들면, SQL, 정규식
  - 선언적 → 원하는 결과를 기술하기만 하고 그 결과를 달성하기 위해 필요한 세부 실행은 언어를 해석하는 엔젠에게 맡긴다.
  - 범용 언어로 만든 호스트 애플리케이션과 함께 조합하기가 어렵다.

### 내부 DSL

- 외부 DSL

    ```sql
    SELECT Country.name, COUNT(Customer.id)
    FROM Country JOIN Customer ON Country.id = Customer.country_id
    GROUP BY Country.name
    ORDER BY COUNT(Customer.id) DESC
    LIMIT 1
    ```

  - SQL이나 정규식 표현, HTML, CSS, LaTex 등과 같은 DSL을 외부 DSL이라고 합니다.
- 내부 DSL

    ```kotlin
    (Country join Customer)
        .slice(Country.name, Count(Customer.id))
        .selectAll()
        .groupBy(Country.name)
        .orderBy(Count(Customer.id), isAsc = false)
    ```


### DSL의 구조

- DSL과 일반 API 사이에 잘 정의된 일반적인 경계는 없다.
- DSL은 구조와 문법이 존재하지만 API는 이것들이 존재하지 않는다.
- DSL vs API
  - gradle

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

  - kotlin test

      ```kotlin
      // DSL
      str should startWith("kot")
      
      // API
      assertTrue(str.startsWith("kot"))
      ```


### 내부 DSL로  HTML 만들기

```kotlin
fun createSimpleTable() = creatHTML().
	table {
    tr {
      td { +"cell" }
    }
	}
```

```html
<table>
	<tr>
		<td>cell</td>
	</tr>
</table>
```

## 구조화된 API 구축: DSL에서 수신 객체 지정 DSL 사용

### 수신 객체 지정 람다와 확장 함수 타입

- 코틀린 수신 객체 지정 람다는 구조화된 API를 만드는데 도움이 된다.
- DSL은 수신 객체 지정 람다를 활용하여 표현할 수 있다.
- 문법

    ```kotlin
    // 수신 객체 타입.(파라미터 타입) -> 반환 타입
    String.(Int, Int) -> Unit
    ```

- 이처럼 수신 객체 지정 람다는 확장 함수 타입이다.
- 확장 함수 타입

    ```kotlin
    val appendExcel : StringBuilder.() -> Unit = { this.append("!") }
    ```


### 수신 객체 지정 람다를 HTML 빌더 안에서 사용

- HTML을 만들기 위한 코틀린 DSL을 HTML 빌더라고 한다.
- 이처럼 빌더는 객체 계층 구조를 선언적으로 정의할 수 있다.
- 코틀린 빌더는 타입 안전성을 보장한다.

```kotlin
fun createSimpleTable() = creatHTML().
	table {
    tr {
      td { +"cell" }
    }
	}
```

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

```kotlin
fun createSimpleTable() = creatHTML().
	table {
    (this@table).tr {
      (this@tr).td { 
				+"cell" 
			}
    }
	}
```

```kotlin
println(createTable())

>>> <table><tr><td></td></tr></table>
```

### 코틀린 빌더: 추상화 재사용을 가능하게 하는 도구

- 반복되는 코드를 새로운 함수로 묶어서 이해하기 쉬운 이름을 붙이는 것은 중요하다.
- 외부 DSL인 SQL, HTML 등은 별도 함수로 분리해 이름을 부여하기 어렵다.
- 반면, 내부 DSL을 사용하면 일반 코드처럼 DSL 조각을 묶어서 재사용할 수 있다.

## invoke 관례를 사용한 더 유연한 블록 중첩

- invoke 관례를 사용하면 객체를 함수처럼 호출할 수 있다.
- 하지만, 사용하라고 만든건 아니다. 남용하면 이해하기 어려운 코드가 발생한다.
- DSL에서는 invoke가 아주 유용하다.

### invoke 관례: 함수처럼 호출할 수 있는 객체

- 관례는 특별한 이름이 붙은 함수를 일반 메소드 호출 구문으로 호출하지 않고 더 간단한 다른 구문으로 호출할 수 있게 지원하는 기능이다.
  - 아주 간단한 예제로는 “foo.get(bar) → foo[bar]”가 있다.
- 아래처럼 invoke를 함수처럼 호출이 가능하다. (비추천 방법, 설명을 위해 구현)

    ```kotlin
    class Greeter(val greeting: String) { 
    	operator fun invoke (name : String) {
    		println ("$greeting, $name!")
    	}
    }
    
    val bavarianGreeter = Greeter("Servus")
    bavarianGreeter("Dmitry")
    >>> Servus, Dmitry!
    ```


### invoke 관례와 함수형 타입

- 일반적인 람다 호출 방식이 실제로는 invoke 관례를 적용한 것임을 알 수 있다.
- 인라인하는 람다를 제외한 모든 람다는 함수형 인터페이스를 구현하는 클래스로 컴파일된다.
- 각 함수형 인터페이스 안에는 그 인터페이스 이름이 가리키는 개수만큼 파라미터를 받는 invoke 메소드가 들어있다.

```kotlin
interface Function2<in P1, in P2, out R> {
	operator fun invoke(pl: Pl, p2: P2): R
}
```

### DSL의 invoke 관례: gradle에서 의존관계 정의

- 모듈 의존관계를 정의하는 gradle DSL에서 invoke 메서드를 사용하여 DSL API의 유연성을 키울 수 있다.

## 실전 코틀린 DSL

### 중위 호출 연쇄: 테스트 프레임워크의 should

- should 같은 테스트 프레임워크에서 사용하는 중위 호출이 DSL을 깔끔하게 만든다.

    ```kotlin
    // 구현
    infix fun <T> T.should(matcher: Matcher<T>) =matcher.test (this)
    
    // 사용
    s should startWith("kot")
    ```


### 원시 타입에 대한 확장 함수 정의: 날짜 처리

- 아무 타입이나 확장 함수의 수신 객체 타입이 될 수 있다.
- 편하게 원시 타입에 대한 확장 함수를 정의하고 원시 타입 상수에 대해 그 확장 함수를 호출할 수 있다.

    ```kotlin
    val yesterday = 1.days.ago
    val tomorrow = 1.days.fromNow
    ```


### 멤버 확장 함수: SQL을 위한 내부 DSL

- 클래스 안에서 확장 함수와 확장 프로퍼티를 선언하는 것을 멤버 확장이라고 한다.
- exposed 프레임 워크는 멤버 확장 함수를 통해 SQL을 위한 내부 DSL을 지원한다.

```kotlin
object Country : Table () (
	val id = integer("id").autoIncrement().primaryKey()
	val name = varchar("name", 50)
}
```

```sql
CREATE TABLE IF NOT EXISTS Country (
	id INT AUTO_INCREMENT NOT NULL,
	name VARCHAR(50) NOT NULL,
	CONSTRAINT pk_Country PRIMARY KEY (id)
)
```

### 안코: 안드로이드 UI를 동적으로 생성하기

```kotlin
fun Activity.showAreYouSureAlert(process: () -> Unit) {
  alert(title = "Are you sure",
    message = "Are you really sure?") {
    positiveButton("Yes") { process() }
    negativeButton("No") { cancel() }
  }
}
```