package com.pteam.kotlin.juyoung.chapter05.code

fun main() {
    println("[5.1.2] 람다와 컬렉션")
    val people = listOf(Person("Alice", 20), Person("Bob", 20))
    println(people.maxBy { it.age }.age)
    println(people.maxBy(Person::age).age)

    println("[5.1.3] 람다 식의 문법")
    val sum = { x:Int, y:Int ->
        println("여러 줄로 이뤄진 본문은 마지막에 있는 식이 람다 값의 결과")
        x + y }
    println(sum(3,9))

    // run: 람다 본문에 있는 코드를 실행
    run { println(43) }

    var names = people.joinToString(separator = " ", transform = Person::name)
    println(names)
    names = people.joinToString(" ") { p -> p.name }
    println(names)
    // it은 남용하면 안 됨
    names = people.joinToString(" ") { it.name }
    println(names)

    // 람다를 변수에 저장할 땐 타입추론 안되므로 타입 명시
    val getAge = { p:Person -> p.age }

    println("[5.1.4] 현재 영역에 있는 변수에 접근")
    val errors = listOf("403 Forbidden", "404 Not Found")
    println(printMsgWithPrefix(errors, "Error:"))

    println("[5.1.5] 멤버 참조")
    val getName = Person::name
    // 최상위에 선언된 함수나 프로퍼티 참조 가능
    run(::salute)

    // 인자가 여럿인 다른 함수한테 작업을 위임, 생성자나 확장함수에서도 가능
    val action = { message:String -> sendEmail(message) }   // 람다 사용
    val nextAction = ::sendEmail    // 멤버 참조 사용

    println(action("action"))
    println(nextAction("next action"))

    // 바운드 멤버 참조
    val p = Person("Emma", 11)
    val ageFunction = p::age
    println(ageFunction())
}

fun printMsgWithPrefix(messages: Collection<String>, prefix: String){
    var errorCount = 0  // 람다가 포획한 변수
    messages.forEach {
        // 람다 안에서 final이 아닌 변수에 접근/변경이 가능
        errorCount ++

        // 람다 안에서 함수의 prefix 파라미터를 사용
        println("$prefix $it, errorCount : $errorCount")
    }
}

fun salute() = println("Salute!")

fun sendEmail(message:String) = println("Send $message")