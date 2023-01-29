import com.pteam.kotlin.juyoung.chapter05.code.Book
import com.pteam.kotlin.juyoung.chapter05.code.Person
import java.io.File
import java.time.LocalDateTime
import kotlin.system.measureTimeMillis

fun main() {
    println("[5.3.1] 시퀀스 연산 실행: 중간 연산과 최종 연산")
    // 최종 연산이 호출될 때 map과 filter가 적용 -> 최종 연산이 없으면 아무 내용도 출력되지 않음
    val intList = listOf(1, 2, 3, 4)
    val list = intList.asSequence()
        .map {
            print("map($it ")
            it * it
        }
        .filter {
            print("filter(${it}) ")
            it % 2 == 0
        }
        .toList()
    println(list)

    // 컬렉션에서 수행하면 map의 결과가 먼저 평가, 시퀀스를 사용하면 연산이 순차적으로 적용되어 일부의 계산은 이뤄지지 않음
    intList.map { it * it }.filter { it > 3 }
    intList.asSequence().map { it * it }.filter { it > 3 }

    // 연산 순서 고려하기
    val people = listOf(Person("Alice", 39), Person("Bob", 15))
    println(people.asSequence().map(Person::name).filter { it.length < 4 }.toList())
    println(people.asSequence().filter { it.name.length < 4 }.map(Person::name).toList())

    println("[5.3.2] 시퀀스 만들기")
    val naturalNumbers = generateSequence(0) { it + 1 }    // 시퀀스
    val numbersTo100 = naturalNumbers.takeWhile { it <= 100 }   // 시퀀스
    println(numbersTo100.sum()) // 모든 지연 연산은 sum의 결과를 계산할 때 수행

    fun File.isInsideHiddenDirectory() = generateSequence(this) { it.parentFile }.any { it.isHidden }
    val file = File("./.git/description")
    println(file.isInsideHiddenDirectory())
}

