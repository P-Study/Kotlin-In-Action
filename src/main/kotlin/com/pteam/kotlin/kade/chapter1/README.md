# 1장

## Kotlin Goal

- 간결한 개발로 생산성 향상
- 안정성 증대
- 기존 자바와의 호환성

## Kotlin Specification

정적 타입 지정

- 컴파일 시점에 타입 추론
    - 성능, 신뢰성, 유지 보수성, 도구 지원 가능

안정성

- null이 가능한 타입 지원

함수형 프로그래밍

- 함수를 값처럼 다룰 수 있음 : 일급 시민 함수(간결함)
- 내부 상태가 바뀌지 않는 불변성을 가짐(다중 스레드 시 안정함)
- 부수 효과가 있음(테스트가 용이함)
    - 무슨 말인지 모르겠음??

자바 코드와의 호환

- 기존 자바와의 호환으로 자바에서 사용하는 모든 라이브러리를 사용 가능
- 자바 소스와 코틀린 소스를 같이 개발 가능(컴파일 정상 동작)

도구 강조

- 좋은 언어만큼이나 편리한 개발 환경도 생산성 향상이 된다는 것

## Kotlin Philosophy

실용성

- 언어를 개발하는 도구를 강조하여 생산성 향상을 한다는 관점

간결성

- 코드를 새로 작성하는 시간보다 기존 코드를 읽는 시간이 더 길어 작성하는 코드에서 의미없는 부분을 줄임
- 자바에 존재하는 번거로운 코드를 묵시적으로 제공하여 간결하게 함

안정성

- 프로그램에서 발생할 수 있는 오류 중 일부를 언어에서 예방해주는 것

상호운용성

- 기존 자바 라이브러리를 최대한 활용