# 4. 클래스, 객체, 인터페이스

## 4.1 클래스 계층 정의
### 4.1.1 코틀린 인터페이스
- 인터페이스에는 아무런 상태도 들어갈 수 없다
```kotlin
interface Clickable {
    fun click()
}
```
- override 변경자 필수!
- override 변경자는 상위 클래스나 상위 인터페이스에 있는 프로퍼티나 메소드를 오버라이드한다는 표시
- 상위 클래스에 있는 메소드와 동일한 시그니처를 가지는 메소드가 있을 경우 컴파일 시점에 오류가 발생하기 때문에 override를 표시해줘야함
```kotlin
class Button : Clickable, Focusable {
    override fun click() = println("I was clicked")
}
```
- Java 8의 default 키워드를 사용하는 것과 정의형태는 동일
- 하지만 Kotlin은 Java 6과 호환되게 설계되어 있어 정적 메소드가 들어있는 클래스를 조합해 구현함
```kotlin
interface Clickable {
    fun click()
    fun showOff() = println("I'm clickable!")
}
```
```kotlin
interface Focusable {
    fun setFocus(b: Boolean) = println("I ${if (b) "got" else "lost"} focus.")
    fun showOff() = println("I'm focusable!")
}
```
- 상속한 인터페이스의 메소드 구현
- Clickable, Focusable에 오버라이드를 해야하는 시그니처가 같은 메소드가 존재한다
- 이를 오버라이드 하기 위해 동일한 시그니처의 메소드를 하나만 선언하면 된다
```kotlin
class Button : Clickable, Focusable {
    override fun click() = println("I was clicked")
    override fun showOff() {
        super<Clickable>.showOff()
        super<Focusable>.showOff()
    }
}
```
### 4.1.2 open, final, abstract 변경자: 기본적으로 final
* 기반 클래스를 변경하는 경우 하위 클래스의 동작이 예기치 않게 바뀔 수도 있다는 면에서 기반 클래스는 취약하다
* 취약한 문제를 해결하기 위해 상속을 위한 설계와 문서를 갖추거나, 상속을 금지하라고 한다
* 코틀린은 기본적으로 클래스와 메소드는 final이다. (상속이 닫혀있다는 것이다)
*
* 상속을 열 수 있는 방법으로 open 키워드를 사용한다
```kotlin
open class RichButton : Clickable {
    // 기본 메소드이기 때문에 final 이고 하위 클래스에서 오버라이드 할 수 없다
    fun disable() {}
    // open 키워드를 사용하였기 때문에 하위 클래스에서 오버라이드 할 수 있다.
    open fun animate() {}
    // 오버라이드한 메소드는 기본적으로 하위 클래스에 열려있다.
    override fun click() {}
    // 오버라이드 금지하는 방법
    final override fun click() {}
}
```
- 추상 클래스 정의하기
* 추상 클래스에서 비추상 함수는 기본적으로 파이널이다
* open 키워드로 오버라이드 허용 가능하다
```kotlin
abstract class Animated {
    // 하위 클래스에서 반드시 오버라이드 해야 한다
    abstract fun animate()
    open fun stopAnimating() {}
    fun animateTwice() {}
}
```
### 4.1.3 가시성 변경자: 기본적으로 공개
* 클래스의 구현에 대한 접근을 제한함으로써 그 클래스에 의존하는 외부 코드를 깨지 않고도 클래스 내부 구현을 변경할 수 있다
* internal -> 모듈 내부에서만 볼 수 있는 특성을 가진 keyword : 모듈이란 한 번에 컴파일되는 코틀린 파일
* 자신보다 가시성이 낮은 타입을 참조하지 못하게 한다.
```kotlin
internal open class TalkativeButton : Focusable {
    private fun yell() = println("Hey!")
    protected fun whisper() = println("Let's talk!")
}

/**
 * 'public' member exposes its 'internal' receiver type TalkativeButton
 */
fun TalkativeButton.giveSpeech() {
    // Cannot access 'yell': it is private in 'TalkativeButton'
    yell()
    // Cannot access 'whisper': it is protected in 'TalkativeButton'
    whisper()
}
interface Focusable {
    fun setFocus(b: Boolean) = println("I ${if (b) "got" else "lost"} focus.")
    fun showOff() = println("I'm focusable!")
}
```
### 4.1.4 내부 클래스와 중첩된 클래스: 기본적으로 중첩 클래스
* 중첩 클래스는 명시적으로 요청하지 않는 한 바깥쪽 클래스 인스턴스에 대한 접근 권한이 없다
```kotlin
interface State: Serializable
interface Viewu {
    fun getCurrentSate(): State
    fun restoreState(state: State) {}
}
class Buttone : Viewu {
    override fun getCurrentSate(): State = ButtonState()
    override fun restoreState(state: State) {}
    // Java의 static 클래스와 동일하다
    class ButtonState : State {}
}
```
* 바깥쪽 클래스의 인스턴스를 가리키기 위해선 내부 클래스를 inner키워드를 통해 내부 클래스로 정의하면 된다
```kotlin
class Outer {
    inner class Inner {
        fun getOuterReference(): Outer = this@Outer
    }
}
```
### 4.1.5 봉인된 클래스: 클래스 계층 정의 시 계층 확장 제한
```kotlin
sealed class Expr {
    class Num(val value: Int) : Expr()
    class Sum(val left: Expr, val right: Expr) : Expr()
}
fun eval(e: Expr): Int =
    when (e) {
        is Expr.Num -> e.value
        is Expr.Sum -> eval(e.right) + eval(e.left)
    }
```
## 4.2 뻔하지 않은 생성자와 프로퍼티를 갖는 클래스 선언
### 4.2.1 클래스 초기화: 주 생성자와 초기화 블록
* 모든 생성자 파라미터에 디폴트 값을 지정하면 컴파일러가 자동으로 파라미터가 없는 생성자를 만들어준다.
* 기반 클래스의 이름 뒤에는 꼭 빈 괄호가 들어간다
* 인터페이스 이름 뒤에는 괄호가 없다 
```kotlin
class User(val nickname: String) // 주 생성자
class RadioButton: Buttone()
```
### 4.2.2 부 생성자: 상위 클래스를 다른 방식으로 초기화
* 인자에 대한 디폴트 값을 제공하기 위해 부 생성자를 여럿 만들지 말라.
* 대신 파라미터의 디폴트 값을 생성자 시그니처에 직접 명시하라
* 그래도 여러 생성자가 필요한 경우가 있다
```kotlin
open class View {
    // 생성자를 통해 상위 클래스에게 객체 생성을 위임한다
    constructor(ctx: Context) : super(ctx) {

    }
    constructor(ctx: Context, attr: AttributeSet) : super(ctx, attr) {

    }
}
```
### 4.2.3 인터페이스에 선언된 프로퍼티 구현
* 인터페이스 구현하는 방법
1. 생성자에 있는 프로퍼티
2. 커스텀 게터
3. 프로퍼티 초기화
```kotlin
// 인터페이스에선 아무 상태도 가질 수 엇다
interface Member {
    val nickname: String
}

class PrivateMember(override val nickname: String) : Member
class SubscribingMember(val email: String) : Member {
    override val nickname: String
        get() = email.substringBefore('@')
}
class Facebook(val accountId: Int) : Member {
    override val nickname: String = getFacebookName(accountId)
}
```
### 4.2.4 게터와 세터에서 뒷받침하는 필드에 접근
```kotlin
// Type mismatch: inferred type is () -> Unit but Unit was expected
class Usar(val name: String) {
    var address: String = "unspecified"
        set(value: String) = {
            println("""
                Address was changed for $name:
                "$field" -> "$value".""".trimIndent())
            field=value
        }
}
```
### 4.2.5 접근자의 가시성 변경
- 외부에서 set을 막는다 domain entity 사용할 때 필요할 것으로 보인다
```kotlin
class LengthCounter {
    var counter: Int = 0
        private set
    
    fun addWord(word: String) {
        counter += word.length
    }
}
```
## 4.3 컴파일러가 생성한 메소드: 데이터 클래스와 클래스 위임
### 4.3.1 모든 클래스가 정의해야 하는 메소드
- 아래 3가지 메소드를 오버라이딩할 때 기본적으로 정의된 시그니처에 맞게 재정의 해야한다
- toString()
- equals()
  - == 연산자가 두 객체를 비교하는 기본적인 방법
  - === 연산자는 두 객체의 참조 비교를 위해서 사용
- hashCode()
### 4.3.2 데이터 클래스: 모든 클래스가 정의해야 하는 메소드 자동 생성
- toString, equals, hashCode
- 주 생성자 밖에 정의된 프로퍼티는 equals나 hashCode를 계산할 때 고려 대상이 아니다
- 모든 프로퍼티를 읽기 전용으로 만들어서 데이터 클래스를 불변 클래스로 만드는 것을 권장한다
```kotlin
data class DataClass(
    val name: String,
    val email: String
)
```
- 데이터 클래스와 불변성: copy() 메소드
- 복사를 통해 일부 프로퍼티 변경을 가능하게 하기 위함이다
- 사용성이 어디서 있을지 감이 잡히지 않음
### 4.3.3 클래스 위임: by 키워드 사용
- open 변경자를 보고 해당 클래스를 다른 클래스가 상속하리라 예상할 수 있다.
- 변경 시 하위 클래스를 깨지 않기 위해 좀 더 조심할 수 있다.
```kotlin
// by 사용 전 모든 method를 override 해줘야한다
class DelegationCollection<T> : Collection<T> {
    private val innerList = arrayListOf<T>()
    override val size: Int
        get() = innerList.size

    override fun isEmpty(): Boolean = innerList.isEmpty()

    override fun iterator(): Iterator<T> = innerList.iterator()

    override fun containsAll(elements: Collection<T>): Boolean = innerList.containsAll(elements)

    override fun contains(element: T): Boolean = innerList.contains(element)
}

// by 사용 전 모든 method를 override 해줘야한다
class CountingSet<T>(
  val innerSet: MutableCollection<T> = HashSet<T>()
) : MutableCollection<T> by innerSet {
  var objectsAdd = 0
  override fun add(element: T): Boolean {
    objectsAdd++
    return innerSet.add(element)
  }

  override fun addAll(elements: Collection<T>): Boolean {
    objectsAdd += elements.size
    return innerSet.addAll(elements)
  }
}
```
## 4.4 object 키워드: 클래스 선언과 인스턴스 생성
- 모든 클래스를 정의하면서 동시에 인스턴스를 생성한다
  - 싱글톤
  - 동반 객체
  - 무명 내부 클래스
### 4.4.1 객체 선언: 싱글턴을 쉽게 만들기
```kotlin
object Payroll {
  val allEmployees = arrayListOf<Person>()
  fun calculateSalary() {
    for (person in allEmployees) {

    }
  }
}

Payroll.allEmployees.add(Person())
Payroll.calculateSalary()
```

### 4.4.2 동반 객체: 팩토리 메소드와 정적 멤버가 들어갈 장소
- 코틀린 언어는 정적 멤버가 없고 static 키워드를 지원하지 않는다
- 동반 객체의 프로퍼티나 메소드에 접근하려면 그 동반 객체가 정의된 클래스 이름을 사용한다
- private 생성자를 호출하기 좋은 위치가 동반 객체이다. 동반 객체는 자신을 둘러싼 클래스의 모든 private 멤버에 접근할 수 있다.
```kotlin
class A {
    companion object {
        fun bar() {
            println("Companion object called")
        }
    }
}
```
- 부 생성자가 여럿 있는 클래스 정의하기
```kotlin
class User {
  val nickname: String
  constructor(email: String) {
      nickname = email.substringBefore('@')
  }
  constructor(facebookAccountId: Int) {
      nickname = getFacebookName(facebookAccountId)
  }
}
```
- 부 생성자를 팩토리 메소드로 대신하기
```kotlin
class User private constructor(val nickname: String) {
  companion object {
    fun newSubsribingUser(email: String) = User(email.substringBefore('@'))
    fun newFacebookUser(accountId: Int) = User(getFacebookName(accountId))
  }
  constructor(email: String) {
      nickname = email.substringBefore('@')
  }
  constructor(facebookAccountId: Int) {
      nickname = getFacebookName(facebookAccountId)
  }
}
```
### 동반 객체를 일반 객체처럼 사용
- 동반 객채에 이름을 부여할 수 있고 이름을 지정하지 않으면 Companion으로 자동 명명된다.
```kotlin
class Person(val name: String) {
    companion object Loader {
        fun fromJSON(jsonText: String): Person = ...
    }
}
```