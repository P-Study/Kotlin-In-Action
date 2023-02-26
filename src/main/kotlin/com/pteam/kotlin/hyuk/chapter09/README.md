# Chapter09 제네릭스

## 9.1 제네릭 타입 파라미터

타입 파라미터를 사용하면 '이 변수는 리스트다.'라고 말하는 대신 정확하게 '이 변수는 문자열을 담는 리스트다'라고 말할 수 있다.

- 참고
  - 코틀린 제네릭 raw 타입을 허용하지 않는다.
    - 자바의 경우 하위 호환성을 위해 raw 타입을 지원한다. 하지만 코틀린은 처음부터 제네릭 개념이 있었기 때문에 raw 타입을 지원하지 않는다.

### 제네릭 함수와 프로퍼티

제네릭 함수를 정의할 때와 마찬가지 방법으로 제네릭 확장 프로퍼티를 선언할 수 있다.
```kotlin
val <T> List<T>.penultimate: T
    get() = this[size - 2]
```
- 주의
  - 일반 프로퍼티는 타입 파라미터를 가질 수 없다.
    - 클래스 프로퍼티에 여러 타입의 값을 저장할 수 없으므로 당연하다.

### 제네릭 클래스 선언

자바와 마찬가지로 코틀린에서도 타입 파라미터를 넣은 꺾쇠 기호`<>`를 클래스 이름 뒤에 붙이면 클래스를 제네릭하게 만들 수 있다.
```kotlin
interface List<T> {
    operator fun get(index: Int): T
}
```

제네릭 클래스를 확장하는 클래스는 기반 타입의 제네릭 파라미터에 대해 타입 인자를 지정해야 한다. 이때 구체적인 타입을 넘길 수 있고 타입 파라미터로 받은 타입을
넘길 수도 있다.
```kotlin
class StringList: List<String> {
    override fun get(index: Int): String = ...
}

class ArrayList<T>: List<T> {
    override fun get(index: Int): T = ...
}
```

### 타입 파라미터 제약

타입 파라미터 제약 : 클래스나 함수에 사용할 수 있는 타입 인자를 제한하는 기능
```kotlin
fun <T : Number> List<T>.sum(): T
```
`T`는 `Number`의 하위 타입이거나 `Number`여야 한다.

타입 파라미터에 둘 이상의 제약을 가할 수도 있다.
```kotlin
fun <T> ensureTrailingPeriod(seq: T)
    where T : CharSequence, T : Appendable { /* do something */ }
```
타입인자가 반드시 `CharSequence`와 `Appendable` 인터페이스를 구현해야 한다.

### 타입 파라미터를 널이 될 수 없는 타입으로 한정

타입 파라미터에 아무런 상한을 정하지 않으면 `<T : Any?>`와 같다. 즉, 널을 허용한다. 만약 널 가능성을 제한하고 싶다면 `<T : Any>`를
사용하면된다.