# 클래스, 객체, 인터페이스

### 코틀린 인터페이스

* 자바와 같이 추상 메소드, 구현 메소드 정의 가능
* 인터페이스 메소드 디폴트 구현 제공
* 콜론(:)으로 클래스 확장과 인터페이스 구현을 모두 처리
* override 변경자를 반드시 사용해야 함
* 상속한 구현 중 중복되는 메소드는 명시적으로 새로운 구현 제공
* super<T>.method() 로 타입 지정
  ```kotlin
  interface Clickable {
      fun click()
      fun showOff() = println("I'm clickable!")   // 디폴트로 구현 제공
  }

  interface Focusable {
      fun setFocus(b: Boolean) = println("I ${if (b) "got" else "lost"} focus.")
      fun showOff() = println("I'm focusable!")
  }
    
  class Button: Clickable, Focusable { 
      override fun click() = println("I was clicked")

      override fun showOff() {
          super<Clickable>.showOff()
          super<Focusable>.showOff()
      }

      // 상속한 구현 중 하나만 사용할 때는 아래처럼
      override fun showOff() = super<Clickable>.showOff()
  }
  ``` 

#### open, final, abstract 변경자: 기본적으로 final

* 클래스의 상속을 허용하려면 앞에 open 변경자를 붙여야 함
* 오버라이드 허용하고 싶은 메소드나 프로퍼티 앞에도 open 사용
* 인터페이스 멤버는 final, open, abstract 변경자를 사용하지 않음
* 인터페이스 멤버는 항상 열려있으며 final로 변경 불가
* 추상 멤버는 항상 열려있기 때문에 open 변경자를 명시할 필요 없음
* 비추상 함수는 기본적으로 final이지만 open으로 오버라이드 허용 가능
  ```kotlin
  open class RichButton: Clickable {
      fun disable() {}    // final 함수로 오버라이드 불가
      open fun animate() {}   // 오버라이드 가능
      final override fun click() {}   // final을 사용하여 오버라이드 금지
  }
  
  abstract class Animated {
      abstract fun animate()
  
      open fun stopAnimating() {
          println("stopAnimating")
      }

      fun animateTwice() {
          println("animateTwice")
      }
  }
  ```

#### 가시성 변경자

| **변경자**        | **클래스 멤버**         | **최상위 선언**        |
|:---------------|:-------------------|:------------------|
| public(기본 가시성) | 모든 곳에서 볼 수 있음      | 모든 곳에서 볼 수 있음     |
| internal       | 같은 모듈 안에서만 볼 수 있음  | 같은 모듈 안에서만 볼 수 있음 |
| protected      | 하위 클래스 안에서만 볼 수 있음 | 최상위 선언에 적용 불가     |
| private        | 같은 클래스 안에서만 볼 수 있음 | 같은 파일 안에서만 볼 수 있음 |

* internal 가시성 변경자는 모듈 내부에서만 볼 수 있음
* internal 변경자는 바이트코드상에서는 public이 됨
* 최상위 선언에 private 가시성을 허용
* 코틀린의 protected 멤버는 오직 어떤 클래스나 그 클래스를 상속한 클래스 안에서만 보임
  ```kotlin
  internal open class TalkativeButton: Focusable {
      private fun yell() = println("Hey!")
      protected fun whisper() = println("Let's talk!")
  }

  // 오류를 없애려면 giveSpeech를 internal로 바꾸거나 TalkativeButton의 가시성을 public으로 바꿔야 함
  //fun TalkativeButton.giveSpeech() {   // public 멤버가 internal TalkativeButton 노출
  //    yell()    // yell은 private멤버로 접근 불가
  //    whisper() // protected멤버로 접근 불가
  //}
  ```

#### 내부 클래스와 중첩된 클래스: 기본적으로 중첩 클래스

* 코틀린의 중첩 클래스는 명시적으로 요청하지 않는 한 바깥쪽 클래스 인스턴스에 대한 접근 권한이 없
* 코틀린 중첩 클래스에 아무런 변경자가 붙지 않으면 자바 static 중첩 클래스와 같음
* 내부 클래스로 변경해서 바깥쪽 클래스에 대한 참조를 포함하려면 inner 변경자 사용
* 내부 클래스에서 외부 클래스에 접근하려면 this@외부클래스
  ```kotlin
  class ButtonView: View {
      override fun getCurrentState(): State = ButtonState()
      override fun restoreState(state: State) { }
      // class ButtonState : State { }
      inner class ButtonState: State {
          fun getCurrentState(): ButtonView = this@ButtonView
      }
  }
  ```

#### 봉인된 클래스: 클래스 계층 정의 시 계층 확장 제한

* sealed 변경자는 상위 클래스를 상속한 하위 클래스 정의를 제한할 수 있음
* sealed 클래스의 하위 클래스를 정의할 때는 반드시 상위 클래스 안에 중첩시켜야 함
* sealed 클래스는 자동으로 open이므로 open 변경자를 붙일 필요 없음
  ```kotlin
  sealed class Expr {
      class Num(val value:Int) : Expr()
      class Sum(val left: Expr, val right: Expr) : Expr()
  }

  fun eval(e: Expr): Int =
  when (e) {
      is Expr.Num -> e.value
      is Expr.Sum -> eval(e.right) + eval(e.left)
  }
  ```

### 뻔하지 않은 생성자와 프로퍼티를 갖는 클래스 선언

#### 클래스 초기화: 주 생성자와 초기화 블록

* 이름 뒤에 오는 괄호로 둘러싸인 코드가 주 생성자
* 주 생성자 목적: 파라미터 지정, 생성자 파라미터에 의해 초기화되는 퍼로퍼티를 정의
* constructor 키워드는 주 생성자나 부 생성자 정의를 시작할 때 사용
* init 키워드는 초기화 블록을 시작(클래스 객체가 만들어질 때 실행될 초기화 코드가 들어감)
* init 키워드는 주 생성자와 함께 사용
* 생성자 파라미터 앞에 밑줄(_)은 퍼로퍼티와 구분, this를 사용하여 모호성을 없애도 됨
* 주 생성자 앞에 별다른 애노테이션이나 가시성 변경자가 없으면 constructor 생략
* 클래스 인스턴스 생성시, new 키워드 없이 생성자 직접 호출
* 모든 생성자 파라미터에 디폴트 값을 지정하면 컴파일러가 자동으으로 인자 없는 생성자 만들어줌
* 클래스를 정의할 때 별도로 생성자를 정의하지 않으면 컴파일러가 자동으로 인자 없는 디폴트 생성자 만들어줌
* 클래스 외부에서 인스턴스화하지 못하게 막고 싶다면 모든 생성자를 private으로 만들면 됨
  ```kotlin
  class User(val nickname: String)
  
  class User constructor(_nickname: String) {
      val nickname: String
      init {
          nickname = _nickname
      }
  }
  
  class User(_nickname: String) { 
      val nickname = _nickname
  }

  // 생성자 파라미터에도 디폴트 값 정의 가능
  class User(val nickname: String, 
            val isSubscribed: Boolean = true)
  
  class Secretive private constructor() {}
  ```

#### 부 생성자: 상위 클래스를 다른 방식으로 초기화

* 인자에 대한 디폴트 값을 제공하기 위해 부 생성자를 여럿 만들지 말고, 파라미터의 디폴트 값을 생성자 시그니처에 직접 명시
* super()로 상위 클래스 생성자 호출
* this()로 자신의 다른 생성자 호출
* 클래스에 주 생성자가 없다면 모든 부 생성자는 반드시 상위 클래스를 초기화하거나 다른 생성자에게 생성을 위임해야 함
  ```kotlin
  // 주 생성자 없이 부 생성자 2개 선언
  class View {
      constructor(ctx: Context) {}
      constructor(ctx: Context, attr: AttributeSet) {}
  }
  
  class MyButton: View {
      // 상위 클래스의 생성자 호출
      constructor(ctx: Context): super(ctx) {}
      constructor(ctx: Context): this(ctx, MY_STYLE) {} // 이 클래스의 다른 생성자에게 위임
      constructor(ctx: Context, attr: AttributeSet): super(ctx, attr) {}
  }
  ```

#### 인터페이스에 선언된 프로퍼티 구현

* 코틀린은 인터페이스에 추상 프로퍼티 선언 가능
* 인터페이스에 있는 프로퍼티 선언에는 뒷받침하는 필드나 게터 등의 정보가 들어있지 않음
* 인터페이스에는 추상 프로퍼티뿐 아니라 게터와 세터가 있는 프로퍼티 선언 가능
  ```kotlin
  // 하위 클래스는 추상 프로퍼티인 email을 반드시 오버라이드 해야하지만 nickname은 오버라이드 하지 않고 상속 가능
  interface User {
      val email: String
      val nickname: String
        get() = email.substringBefore('@')
  }
  class PrivateUser(override val nickname: String): User
  class SubscribingUser(val email: String): User {
      override val nickname: String
        get() = email.substringBefore('@')  // 커스텀 게터
  }
  class FacebookUser(val accountId: Int): User {
      override val nickname = getFacebookName(accountId)    // 프로퍼티 초기화 식
  }
  ```

#### 게터와 세터에서 뒷받침하는 필드에 접근

* 값을 저장하는 동시에 로직을 실행할 수 있게 하기 위해서는 접근자 안에서 프로퍼티를 뒷받침하는 필드에 접근할 수 있어야 함
* 접근자의 본문에서는 field라는 특별한 식별자를 통해 뒷받침하는 필드에 접근할 수 있음
* 게터에서는 field값을 읽을 수만 있고, 세터에서는 읽거나 쓸 수 있음
* field를 사용하지 않는 커스텀 접근자 구현을 정의한다면 뒷받침하는 필드는 존재하지 않음
  ```kotlin
  class User(val name: String) {
      var address: String = "unspecified"
          set(value: String) {
              println("""
                    Address was changed for $name:
                    "$field" -> "$value".""".trimIndent())
              field = value
          } 
  }
  ```

#### 접근자의 가시성 변경

* 접근자의 가시성은 기본적으로 프로퍼티의 가시성과 같음
* get이나 set 앞에 가시성 변경자를 추가해서 접근자의 가시성 변경 가능
  ```kotlin
  class LengthCounter {
      var counter: Int = 0
          private set   // 이 클래스 밖에서 이 프로퍼티의 값 변경 불가
      fun addWord(word: String) {
          counter += word.length
      }
  }
  ```

### 컴파일러라 생성한 메소드: 데이터 클래스와 클래스 위임

#### 모든 클래스가 정의해야 하는 메소드

* 문자열 표현: toString()
* 객체의 동등성: equals()
* 해시 컨테이너: hashCode()

#### 데이터 클래스: 모든 클래스가 정의해야 하는 메소드 자동 생성

* equals와 hashCode는 주 생성자에 나열된 모든 프로퍼티를 고려해 만들어짐
* 주 생성자 밖에 정의된 프로퍼티는 equals나 hashCode를 계산할 때 고려의 대상이 아님
  ```kotlin
  data class Client(val name: String, val postalCode: Int)
  ```

#### 데이터 클래스와 불변성: copy() 메소드

* 데이터 클래스의 프로퍼티는 val, var 둘 다 써도 되지만, 모든 프로퍼티를 읽기 전용으로 만들어서 불변 클래스로 만드는 것을 권장
* HashMap 등의 컨테이너에 데이터 클래스 객체를 담는 경우엔 불변성이 필수(다중스레드 프로그램의 경우 더 중요)
* copy() 메소드: 객체를 복사하면서 일부 프로퍼티 값을 바꾸거나 복사본을 제거해도 원본을 참조하는 다른 부분에 전혀 영향을 끼치지 않음
  ```kotlin
  data class Client(val name: String, val postalCode: Int) {
      // 직접 구현한다면..
      fun copy(name: String = this.name,
                postalCode: Int = this.postalCode) = Client(name, postalCode)
  }
  ```

#### 클래스 위임: by 키워드 사용

* by 키워드를 통해 그 인터페이스에 대한 구현을 다른 객체에 위임 중이라는 사실을 명시할 수 있음
* 메소드 중 일부 동작을 변경하고 싶은 경우 메소드를 오버라이드하면 컴파일러가 생성한 메소드 대신 오버라이드한 메소드가 쓰임
  ```kotlin
  class DelegatingCollection<T>(
      innerList: Collection<T> = ArrayList<T>()
  ): Collection<T> by innerList {}
  
  class CountingSet<T>(
      val innerSet: MutableCollection<T> = HashSet<T>()
  ): MutableCollection<T> by innerSet {
      var objectsAdded = 0
  
      override fun add(element: T): Boolean{
          objectsAdded++
          return innerSet.add(element)
      }
  
      override fun addAll(c: Collection<T>): Boolean{
          objectsAdded += c.size
          return innerSet.addAll(c)
      }
  }
  ```

### object 키워드: 클래스 선언과 인스턴스 생성

* 객체 선언은 싱글턴을 정의하는 방법 중 하나
* 동반 객체는 인스턴스 메소드는 아니지만 어떤 클래스와 관련 있는 메소드와 팩토리 메소드를 담을 때 쓰임. 동반 객체 메소드에 접근할 때는 동반 객체가 포함된 클래스의 이름을 사용할 수 있음
* 객체 식은 자바의 무명 내부 클래스 대신 쓰임

#### 객체 선언: 싱글턴을 쉽게 만들기

* 코틀린은 객체 선언 기능을 통해 싱글턴을 기본 지원
* 객체 선언은 object 키워드로 시작
* 클래스를 정의하고 그 클래스의 인스턴스를 만들어서 변수에 저장하는 모든 작업을 단 한 문장으로 처리
* 프로퍼티, 메소드, 초기화 블록 등이 들어갈 수 있지만 생성자는 객체 선언에 쓸 수 없음
* 클래스나 인터페이스 상속할 수 있음
  ```kotlin
  object Payroll {
      val allEmployees = arrayListOf<Person>()
  
      fun calculateSalary() {
          for (person in allEmployees) {
          }
      }
  }
  
  object CaseInsensitiveFileComparator: Comparator<File> {
      override fun compare(file1: File, file2: File): Int{
          return file1.path.compareTo(file2.path, ignoreCase = true)
      }
  }
  ```

#### 동반 객체: 팩토리 메소드와 정적 멤버가 들어갈 저장소

* 최상위 함수는 private으로 표시된 클래스 비공개 멤버에 접근 불가
* 중첩된 객체 선언의 멤버 함수로 정의하여 클래스 내부 정보에 접근
* 클래스 안에 정의된 객체 중 하나에 companion을 붙이면 그 클래스의 동반 객체로 만들 수 있음
* 동반 객체는 자신을 둘러싼 클래스의 모든 private 멤버에 접근 가능
  ```kotlin
  class User private constructor(val nickname: String) {    // 비공개 주 생성자
      companion object {    // 동반 객체 선언
          fun newSubscribingUser(email: String) = User(email.substringBefore('@'))
          fun newFacebookUser(accountId: Int) = User(getFacebookName(accountId))
      }
  }
  
  fun main() {
      val subscribingUser = User.newSubscribingUser("bob@gmail.com")
      val facebookUser = User.newFacebookUser(4)
      println(subscribingUser.nickname)
  }
  ```

#### 동반 객체를 일반 객체처럼 사용

동반 객체에 이름을 붙이거나, 동반 객체가 인터페이스를 상속하거나, 동반 객체 안에 확장 함수와 프로퍼티를 정의할 수 있음

* 동반 객체에 이름을 붙이기
  ```kotlin
  class Person(val name: String) {
      companion object Loader {    // 이름 붙인 동반 객체
          fun fromJson(jsonText: String): Person { //... }
      }
  }
  
  fun main() {
      val person = Person.Loader.fromJson("{name: 'Dmitry'}")
      println(person.name)
      val person2 = Person2.fromJson("{name: 'Brent'}")
      println(person.name)
  }
  ```

* 동반 객체에서 인터페이스 구현
  ```kotlin
  interface JSONFactory<T> {
      fun fromJSON(jsonText: String): T
  }
  
  class Person(val name: String) {
      companion object: JSONFactory<Person> {
          override fun fromJSON(jsonText: String): Person { TODO("Not yet implemented") }
      }
  }
  
  fun loadFromJSON<T>(factory: JSONFactory<T>): T { //... }
  fun main() {
      loadFromJSON(Person)
  }
  ```

* 동반 객체 확장
  ```
  - 클래스 밖에서 정의한 확장 함수를 동반 객체 안에서 함수를 정의한 것처럼 호출 가능
  - 동반 객체에 대한 확장 함수를 작성하려면 빈 객체라도 동반 객체를 꼭 선언해야 함
  ```
  ```kotlin
  // 비즈니스 로직 모듈
  class Person(val firstName: String, val lastName: String) {
      companion object {
          // 비어있는 동반 객체 선언
      }
  }
  
  // client/server 통신 모듈, 확장 함수 선언
  fun Person.Companion.fromJSON(json: String): Person { }
  
  fun main() {
      val p = Person.fromJSON(json)
  }
  ```

#### 객체 식: 무명 내부 클래스를 다른 방식으로 작성

* 무명 객체를 정의할 때도 object 키워드 사용
  ```kotlin
  window.addMouseListener(
      object: MouseAdapter() {  // MouseAdapter를 확장하는 무명 객체 선언
          // MouseAdapter의 메소드 오버라이드
          override fun mouseClicked(e: MouseEvent) { //... }
          override fun mouseEntered(e: MouseEvent) { //... }
      }
  )
  
  // 무명 객체 안에서 로컬 변수 사용하기
  fun countClicks(window: Window) {
      var clickCount = 0
      window.addMouseListener(object : MouseAdapter() {
          override fun mouseClicked(e: MouseEvent) {
              clickCount++
          }
      })
  }
  ```