# 연산 오버로딩과 기타 관례

**관례**

어떤 언어의 기능과 미리 정해진 이름의 함수를 연결해주는 기법을 코틀린에서는 관례(convention)라고 부릅니다.

기존 자바 클래스에 대해 확장함수를 구현하면서 관례에 따라 이름을 붙이면, 기존 자바 코드를 바꾸지 않아도 새로운 기능을 쉽게 부여할 수 있습니다.

**operator**

키워드를 붙임으로써 어떤 함수가 관례를 따르는 함수임을 명확히 할 수 있습니다.

## 산술 연산자 오버로딩

### 이항 연산자 오버로딩

![https://user-images.githubusercontent.com/58816862/218765306-d48e55bb-53b3-40d2-ae30-478779e4c97f.png](https://user-images.githubusercontent.com/58816862/218765306-d48e55bb-53b3-40d2-ae30-478779e4c97f.png)

두 좌표의 합을 위처럼 구현할 수 있지만,

![https://user-images.githubusercontent.com/58816862/218765167-6ba769da-b935-45ae-af01-9834975ded11.png](https://user-images.githubusercontent.com/58816862/218765167-6ba769da-b935-45ae-af01-9834975ded11.png)

이렇게도 구현할 수 있습니다. 그리고 이처럼 외부 함수의 클래스에 대한 연산자를 정의할 때는 관례를 따르는 이름의 확장 함수로 구현하는 게 일반적인 패턴입니다.

![https://user-images.githubusercontent.com/58816862/218765027-3d08b76b-75d5-48c4-a7ce-a3a6f5ef12d9.png](https://user-images.githubusercontent.com/58816862/218765027-3d08b76b-75d5-48c4-a7ce-a3a6f5ef12d9.png)

위는 오버로딩이 가능한 이항 산술 연산자입니다.

### 복합 대입 연산자 오버로딩

![https://user-images.githubusercontent.com/58816862/218764526-7b0b08d6-25ee-428c-8707-7dd05fd385e2.png](https://user-images.githubusercontent.com/58816862/218764526-7b0b08d6-25ee-428c-8707-7dd05fd385e2.png)

+=, -= 와 같은 것을 복합 대입 연산자라고 부릅니다.

### 단항 연산자 오버로딩

![https://user-images.githubusercontent.com/58816862/218765989-6ff93057-2cb2-4067-a372-96726aca9986.png](https://user-images.githubusercontent.com/58816862/218765989-6ff93057-2cb2-4067-a372-96726aca9986.png)

위의 결과는 Point(x=-10, y=-20)가 됩니다.

![https://user-images.githubusercontent.com/58816862/218766337-a288c997-8588-43bd-9043-8d2a4f1f07a1.png](https://user-images.githubusercontent.com/58816862/218766337-a288c997-8588-43bd-9043-8d2a4f1f07a1.png)

위는 오버로딩 할 수 있는 단항 산술 연산자입니다.

## 비교 연산자 오버로딩

### 동등성 연산자: equals

![https://user-images.githubusercontent.com/58816862/218766578-bd65f878-0b51-4875-b157-1908e70ade61.png](https://user-images.githubusercontent.com/58816862/218766578-bd65f878-0b51-4875-b157-1908e70ade61.png)

코틀린은 == 연산자 호출을 equals 메소드 초출로 컴파일합니다.

![https://user-images.githubusercontent.com/58816862/218767542-88db3cd3-ea3f-4ba3-86c8-b8c434b06a27.png](https://user-images.githubusercontent.com/58816862/218767542-88db3cd3-ea3f-4ba3-86c8-b8c434b06a27.png)

직접 구현하게 된다면, 위와 같은 결과가 나오게 됩니다.

### 순서 연산자: compareTo

![https://user-images.githubusercontent.com/58816862/218768295-cb69d17b-f775-4b4e-825c-d00b842ead0a.png](https://user-images.githubusercontent.com/58816862/218768295-cb69d17b-f775-4b4e-825c-d00b842ead0a.png)

위처럼 방정식 넘겨주는 느낌으로 컴파일 된다는 것을 알 수 있습니다.

![https://user-images.githubusercontent.com/58816862/218768682-4182c33f-da33-44b8-8ce7-7c1d19d4b720.png](https://user-images.githubusercontent.com/58816862/218768682-4182c33f-da33-44b8-8ce7-7c1d19d4b720.png)

직접 구현하게 된다면, 위와 같은 결과가 나오게 됩니다.

## 컬렉션과 범위에 대해 쓸 수 있는 관례

![https://user-images.githubusercontent.com/58816862/218771959-043f1fba-d21f-46c7-a554-0d77791b1e02.png](https://user-images.githubusercontent.com/58816862/218771959-043f1fba-d21f-46c7-a554-0d77791b1e02.png)

![https://user-images.githubusercontent.com/58816862/218770921-d5d2ede8-057a-4f4a-87f6-38daf07fe14e.png](https://user-images.githubusercontent.com/58816862/218770921-d5d2ede8-057a-4f4a-87f6-38daf07fe14e.png)

코틀린은 위와 같은 방법으로 Get 관례를 구현할 수 있습니다.

![https://user-images.githubusercontent.com/58816862/218772086-dca7fee3-87c3-4cae-9004-eb156c388c74.png](https://user-images.githubusercontent.com/58816862/218772086-dca7fee3-87c3-4cae-9004-eb156c388c74.png)

![https://user-images.githubusercontent.com/58816862/218771774-fc8085d3-c972-4da9-b894-2fa771ef8185.png](https://user-images.githubusercontent.com/58816862/218771774-fc8085d3-c972-4da9-b894-2fa771ef8185.png)

Set 관례는 위와 같이 구현할 수 있습니다.

![https://user-images.githubusercontent.com/58816862/218772157-8ea54c76-d4d6-445d-b29b-c66b9795d4d3.png](https://user-images.githubusercontent.com/58816862/218772157-8ea54c76-d4d6-445d-b29b-c66b9795d4d3.png)

![https://user-images.githubusercontent.com/58816862/218772367-b665f13c-3113-4ec0-8a3a-32fcf0b75a5d.png](https://user-images.githubusercontent.com/58816862/218772367-b665f13c-3113-4ec0-8a3a-32fcf0b75a5d.png)

위는 In 관례를 구현한 것입니다.

![https://user-images.githubusercontent.com/58816862/218772530-5e11388e-f836-4133-9584-0ccfeafeb44f.png](https://user-images.githubusercontent.com/58816862/218772530-5e11388e-f836-4133-9584-0ccfeafeb44f.png)

위는 rangeTo의 관례인데, start ~ end를 뜻합니다.

## 구조 분해 선언과 component 함수

구조 분해를 사용하면 복합적인 값을 분해해서 여러 다른 변수를 한꺼번에 초기화할 수 있습니다.

![https://user-images.githubusercontent.com/58816862/218773772-b6981ff6-b265-4743-920a-296ff0e96db8.png](https://user-images.githubusercontent.com/58816862/218773772-b6981ff6-b265-4743-920a-296ff0e96db8.png)

![https://user-images.githubusercontent.com/58816862/218773540-b6cb3a19-2df7-40b1-9b87-b218953eafd6.png](https://user-images.githubusercontent.com/58816862/218773540-b6cb3a19-2df7-40b1-9b87-b218953eafd6.png)

위와 같이 구조 분해를 사용하면, 매개 변수를 꺼낼 수 있습니다.

데이터 클래스의 경우에는 컴파일러가 자동으로 componentN 함수를 만들어 주지만, 데이터 클래스가 아닌 경우에는 아래와 같이 구현해주어야합니다.

![https://user-images.githubusercontent.com/58816862/218774290-c49fc1ab-1e8c-4833-bc2f-3e232cc2d2a7.png](https://user-images.githubusercontent.com/58816862/218774290-c49fc1ab-1e8c-4833-bc2f-3e232cc2d2a7.png)

참고로 배열이나, 컬렉션에도 componentN 함수가 있습니다.

![https://user-images.githubusercontent.com/58816862/218774641-a8e24669-b98a-44e7-863e-08dda82007c7.png](https://user-images.githubusercontent.com/58816862/218774641-a8e24669-b98a-44e7-863e-08dda82007c7.png)

이런식으로 맵의 원소에 대해 이터레이션할 때도 유용하게 쓸 수 있습니다.

## 프로퍼티 접근자 로직 재활용: 위임 프로퍼티

![https://user-images.githubusercontent.com/58816862/218990987-9eca9335-ad01-4538-b64e-fcbbd8c736c5.png](https://user-images.githubusercontent.com/58816862/218990987-9eca9335-ad01-4538-b64e-fcbbd8c736c5.png)

위임 프로퍼티는 일반적으로 위와 같은 문법으로 사용됩니다.

![https://user-images.githubusercontent.com/58816862/218991408-93be5e0f-7bb0-4e1d-aa5a-28b48e4e4f53.png](https://user-images.githubusercontent.com/58816862/218991408-93be5e0f-7bb0-4e1d-aa5a-28b48e4e4f53.png)

그리고 위와 같이 사용할 수 있습니다.

간단한 예시를 통해서 위임 프로퍼티의 기능인 지연 초기화를 해보도록 하겠습니다.

![https://user-images.githubusercontent.com/58816862/218990223-14fe0335-2d67-4037-b18f-c24525546d08.png](https://user-images.githubusercontent.com/58816862/218990223-14fe0335-2d67-4037-b18f-c24525546d08.png)

위임 프로퍼티는 위와 같이 지연 초기화를 할 수 있습니다.

![https://user-images.githubusercontent.com/58816862/218990343-647c010e-d6ea-49fc-b900-6051eac3c3f7.png](https://user-images.githubusercontent.com/58816862/218990343-647c010e-d6ea-49fc-b900-6051eac3c3f7.png)

그리고 by lazy()를 사용하여 위와 같이 간소화할 수 있습니다.
