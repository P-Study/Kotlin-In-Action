package com.pteam.kotlin.juyoung.chapter05.code

fun main() {
    println("[5.2.1] filter와 map")
    val people = listOf(Person("Alice", 39), Person("Bob", 15))
    val maxAge = people.maxBy(Person::age)!!.age
    println(people.filter { it.age == maxAge }[0].age)

    println("[5.2.2] 컬렉션에 술어 적용")
    val canBeInClub27 = { p:Person -> p.age <= 27}  // 술어 함수
    println(people.all(canBeInClub27))  // 모든 원소가 만족하는지 확인 all = !any
    println(people.any(canBeInClub27))  // 하나라도 만족하는지 확인 any = !all 가독성을 위해 !는 붙이지 않는걸로..
    println(people.count(canBeInClub27))
    println(people.find(canBeInClub27)) // 가장 먼저 만족하는 원소 반환, 없으면 null 반환 firstOrNull과 같음

    println("[5.2.3] groupBy")
    println(people.groupBy { it.age })
    println(people.groupBy(Person::age))
    val list = listOf("a", "ab", "b")
    println(list.groupBy(String::first))

    println("[5.2.4] flatmap과 faltten: 중첩된 컬렉션 안의 원소 처리")
    val books = listOf(
        Book("book1", listOf("a1", "a2")),
        Book("book2", listOf("a1", "a3"))
    )
    println(books.flatMap { it.authors }.toSet())

    val strings = listOf("abc", "def")
    println(strings.flatMap { it.toList() })
}
