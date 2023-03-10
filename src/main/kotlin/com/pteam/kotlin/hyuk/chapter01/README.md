# Chapter01

Kotlin은 간결하고 실용적이며, 자바 코드와의 상호 운용성을 중시한다.

## Kotlin의 주요 특성

### 정적 타입 지정 언어
Kotlin은 Java와 동일하게 정적 타입 지정언이다. 즉, 프로그램의 구성 요소의 타입을 컴파일 시점에 알 수 있고 프로그램 안에서 객체의 필드나 메소드를 사용할 때마다 컴파일러가 타입을 검증해준다.

**동적 타입 vs 정적 타입**
- 동작 타입
  - 대표 언어 : JavaScript
  - 장점 : 코드가 더 짧아지고 데이터 구조를 더 유연하게 생성하고 사용할 수 있다.
  - 단점 : 이름을 잘못 입력하는 등의 실수도 컴파일 시 걸러내지 못하고 실행 시점에 오류가 발생한다.

- 정적 타입
  - 장정
    - 성능 : 실행 시점에 어떤 메소드를 호출할지 알아내는 과정이 필요 없어 메소드 호출이 더 빠르다.
    - 신뢰성 : 컴파일러가 프로그램의 정확성을 검증해준다.
    - 유지 보수성 : 코드에서 다루는 객체가 어떤 타입에 속하는지 알 수 있기 때문에 처음 보는 코드를 다룰 때도 더 쉽다.
    - 도구 지원 : 더 안전하게 리팩토링할 수 있고, 도구도 더 정확한 코드 완성 기능을 제공할 수 있다.

### 새로운 타입
Java와 비교했을때 Kotlin은 몇 가지 새로운 타입이 있다.
- nullable type
- function type

### 함수형 프로그래밍 지원

코틀린은 처음부터 함수형 프로그래밍을 풍부하게 지원해 왔다.

코틀린은 다음과 같은 방법으로 함수형 프로그래밍을 지원한다.
- 함수 타입을 지원함에 따라 어떤 함수가 다른 함수를 파라미터로 받거나 함수가 새로운 함수를 반환할 수 있다.
- 람다 식을 지원함에 따라 번거로운 준비 코드를 작성하지 않아도 코드 블록을 쉽게 정의하고 여기저기 전달할 수 있다.
- 데이터 클래스는 불변적인 값 객체를 간편하게 만들 수 있는 구문을 제공한다.
- 코틀린 표준 라이브러리는 객체와 컬렉션을 함수형 스타일로 다룰 수 있는 API를 제공한다.

**함수형 프로그래밍**
- 핵심 개념
  - 일급 시민인 함수 : 함수를 일반 값처럼 다룰 수 있다. 함수를 변수에 저장할 수 있고, 인자로 다른 함수에 전달할 수 있고 함수에 새로운 함수를 만들어 반환할 수 있다.
  - 불변성 : 함수형 프로그래밍에서는 일단 만들어지고 나면 내부 상태가 절대로 바뀌지 않는 불변 객체를 사용해 프로그램을 작성한다.
  - 부수 효과 없음 : 함수형 프로그래밍에서는 입력이 같으면 항상 같은 출력을 내놓고 다른 객체의 상태를 변경하지 않으며, 함수 외부나 다른 바깥 환경과 상호작용하지 않는 순수 함수를 사용한다.

- 장점
  - 간결성 : 함수를 값처럼 활용할 수 있으며 더 강력한 추상화를 할 수 있고 이를 이용해 코드 중복을 막을 수 있다.
  - 스레드 세이프 : 불변 데이터 구조를 사용하고 순수 함수를 그 데이터 구조에 적용한다면 다중 스레드 환경에서 같은 데이터를 여러 스레드가 변경할 수 없다.
  - 테스트 용이성 : 순수 함수는 테스트에 필요한 전체 환경을 구성하는 준비 코드가 따로 필요없다.

## 코틀린의 철학

### 실용성
코틀린은 실제 문제를 해결하기 위해 만들어진 실용적인 언어다.

추가로 코틀린을 도구의 활용을 염두에 두고 설계되었다. 실제로 IDE의 도구를 이용하면 더욱 더 코틀린스럽게 코드를 작성할 수 있다.

### 간결성
코틀린은 프로그래머가 작성하는 코드에서 의미가 없는 부분을 줄이고, 언어가 요구하는 구조를 만족시키기 위해 별 뜻은 없지만 프로그램에 꼭 넣어야 하는 부수적인 요소를 줄이기 위해 많은 노력을 기울였다.

주의할 부분이 있다. 간결하다와 소스코드를 가능한 짧게 만든다는 다른 의미다. 간결성은 코드를 읽을때 내용을 파악하기 쉬움을 의미한다.

### 안전성
코틀린은 JVM을 사용하면서 얻을 수 있는 메모리 안전성, 버퍼 오버플로우 방지, 동적으로 할당한 메모리 잘못사용함으로 생기는 문제 방지에 더해 추가적인 안전성을 지원한다.
코틀린은 nullable type을 통해 NullPointException을 없애기 위해 노력한다. 코틀린은 스마트 케스트를 통해 ClassCastException을 줄여준다.

### 상호운용성
코틀린에서는 자바로 만든 API를 호출할 수 있다. 그 역도 성립한다. 추가로 코틀린은 기존 자바 라이브러리를 가능하면 최대한 활용한다. 



