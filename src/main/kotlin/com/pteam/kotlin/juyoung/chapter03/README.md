# 함수 정의와 호출

### 코틀린에서 컬렉션 만들기
* 코틀린은 자바 컬렉션과 같은 클래스
* 자바보다 더 많은 기능 사용 가능(리스트의 마지막 원소, 최댓값 추출 등)
```kotlin
val set = hashSetOf(1, 2, 3)
val list = arrayListOf(1, 2, 3)
val map = hashMapOf(1 to "one", 2 to "two", 3 to "three")   // to 는 일반 함수

println(set.javaClass)  // javaClass는 JAVA의 getClass()
println(list.javaClass)
println(map.javaClass)

println(list.last())
println(set.max())
```

### 함수를 호출하기 쉽게 만들기
* 자바 컬렉션에는 디폴트 toString 구현이 있음
  ```kotlin
  val list = listOf(1, 2, 3)
  println(list)
  ```

* (1; 2; 3) 처럼 원소 사이를 세미콜론으로 구분하고 괄호로 리스트 둘러싸기
  ```kotlin
  fun<T> joinToString(
    collections: Collection<T>,
    // 디폴트 파라미터 개념이 자바에 없음 -> joinToString에 @JvmOverloads 애노테이션 사용하면 오버로딩 함수가 만들어 짐
    separator: String = ", ",
    prefix: String = "",
    postfix: String = ""
  ): String {
    val result = StringBuilder(prefix)

    for ((index, element) in collections.withIndex()) {
        if(index > 0) result.append(seperator)
        result.append(element)
    }
    
    result.append(postfix)

    return result.toString()
  }
  
  fun main() {
    val list = listOf(1, 2, 3)
    println(joinToString(list, ";", "(", ")"))
  }
  ```

### 메소드를 다른 클래스에 추가: 확장 함수와 확장 프로퍼티
* 확장 함수
  ```kotlin
  package strings
  
  // String: 수신 객체 타입, this: 수신 객체
  fun String.lastChar(): Char = this.get(this.length - 1)
  
  println("Kotlin".lastChar())  // n 출력
  ```
  
* 임포트와 확장 함수: 코틀린에서는 클래스를 임포트할 때와 동일한 구문을 사용해 개별 함수를 임포트할 수 있음
  ```kotlin
  import strings.lastChar
  // import strings.*
  // import strings.lastChar as last // 이름 충돌 해결
  
  val c = "Kotlin".lastChar()
  // val c = "Kotlin".last()
  ```

* 자바에서 확장 함수 호출
  ```java
  // StringUtil.kt 파일에 확장 함수를 정의했다면
  char c = StringUtilKt.lastChar("Java");
  ```

* 확장 함수로 유틸리티 함수 정의
  ```kotlin
  // Collection<T>에 대한 확장 함수 선언
  fun<T> Collection<T>.joinToString(
    separator: String = ", ",
    prefix: String = "",
    postfix: String = ""
  ): String {
    val result = StringBuilder(prefix)
  
    for((index, element) in this.withIndex()) { // this는 수신객체로 T타입의 원소로 이뤄진 컬렉화
        if(index > 0) result.append(separator)
            result.append(element)
        }

        result.append(postfix)
        return result.toString()
  }
  
  fun main() {
    val list = listOf(1, 2, 3)
    println(list.joinToString(separator = "; ", prefix = "(", postfix = ")"))
  }
  ```
  ```kotlin
  // 문자열의 컬렉션에 대해서만 호출할 수 있는 join 함수 정의
  fun Collection<String>.join(
    separator: String = ", ",
    prefix: String = "",
    postfix: String = ""
  ) = joinToString(separator, prefix, postfix)
  
  fun main() {
    listOf(1, 2, 3).join()
  }
  ```

* 확장 함수는 오버라이드할 수 없다
  * 코틀린은 확장 함수를 정적으로 결정하기 때문에 오버라이드할 수 없음
  * 어떤 클래스를 확장한 함수와 그 클래스의 멤버 함수의 이름과 시그니처가 같다면 확장 함수가 아니라 멤버 함수가 호출됨
    (멤버 함수의 우선순위가 더 높음)
* 확장 프로퍼티
  ```kotlin
  val String.lastChar: Char
    get() = get(length - 1) // 프로퍼티 게터
    set(value: Char) {  // 프로퍼티 세터
        this.setCharAt(length - 1, value)
    }
  
  fun main() {
    println("Kotlin".lastChar)  // n 출력
  
    val sb = StringBuilder("Kotlin?")
    sb.lastChar = '!'
    println(sb) // Kotlin! 출력
  }
  ```

### 컬렉션 처리: 가변 길이 인자, 중위 함수 호출, 라이브러리 지원
* 가변 인자 함수: 인자의 개수가 달라질 수 있는 함수 정의
  * 파라미터 앞에 vararg 변경자 사용
  * 스프레드 연산자(*)를 사용하여 배열의 내용을 펼쳐줌
* 값의 쌍 다루기: 중위 호출과 구조 분해 선언
  ```kotlin
  1.to("one")   // 일반적인 방식
  1 to "one"    // 중위 호출
  // to 함수 간략하게 줄인 코드
  // 중위 호출 허용하려면 infix 변경자를 사용
  infix fun Any.to(other: Any) = Pair(this, other)
  
  // to 함수는 Pair의 인스턴스를 반환
  // Pair의 내용으로 두 변수를 즉시 초기화 -> 구조 분해 선언
  val (number, name) = 1 to "one"
  ```

### 문자열과 정규식 다루기
* 문자열 나누기
  * 코틀린 정규식 문법은 자바와 동일
  * toRegex 확장 함수를 사용해 문자열을 정규식으로 변환
* 정규식과 3중 따옴표로 묶은 문자열
  ```kotlin
  fun parsePathRegex(path: String) {
    //    val regex = "(.+)/(.+)\\.(.+)".toRegex()
    val regex = """(.+)/(.+)\.(.+)""".toRegex()     // 3중 따옴표를 사용하면 이스케이프할 필요 없음
    val matchResult = regex.matchEntire(path)

    if(matchResult != null) {
        val (directory, fileName, extension) = matchResult.destructured
        println("Dir: $directory, name: $fileName, ext: $extension")
    }
  }
  ```

### 코드 다듬기: 로컬 함수와 확장
* DRY 원칙(반복하지 말라)
