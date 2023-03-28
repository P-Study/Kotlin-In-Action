package com.pteam.kotlin.hyuk.chapter11.code

import kotlinx.html.*
import kotlinx.html.stream.createHTML
import java.lang.AssertionError
import java.time.LocalDate
import java.time.Period

//  Chapter 11.2
fun buildStringV1(builderAction: (StringBuilder) -> Unit): String {
    val sb = StringBuilder()
    builderAction(sb)
    return sb.toString()
}

fun buildStringV2(builderAction: StringBuilder.() -> Unit): String {
    val sb = StringBuilder()
    sb.builderAction()
    return sb.toString()
}

fun buildStringV3(builderAction: StringBuilder.() -> Unit): String =
    StringBuilder().apply(builderAction).toString()

fun buildDropdownV1() = createHTML().div(classes = "dropdown") {
    button(classes = "btn dropdown-toggle") {
        +"Dropdown"
        span(classes = "caret")
    }
    ul(classes = "dropdown-menu") {
        li { a("#") { +"Action" } }
        li { a("#") { +"Another action" } }
        li { role = "separator"; classes = setOf("divider") }
        li { classes = setOf("dropdown-header"); +"Header" }
        li { a("#") { +"Separated link " } }
    }
}

fun buildDropDownV2() = createHTML().dropdown {
    dropdownButton { +"Dropdown" }
    dropdownMenu {
        item("#", "Action")
        item("#", "Another Action")
        divider()
        dropdownHeader("Header")
        item("#", "Separated link")
    }
}

fun UL.item(href: String, name: String) = li { a(href) { +name } }
fun UL.divider() = li { role = "separator"; classes = setOf("divider") }
fun UL.dropdownHeader(text: String) =
    li { classes = setOf("dropdown-header"); +text }

fun DIV.dropdownButton(block: BUTTON.() -> Unit) = button(classes = "btn dropdown-toggle", block = block)
fun DIV.dropdownMenu(block: UL.() -> Unit) = ul("dropdown-menu", block)

fun TagConsumer<String>.dropdown(
    block: DIV.() -> Unit,
): String = div(block = block)

// Chapter 11.3
class Greeter(val greeting: String) {
    operator fun invoke(name: String) = "$greeting, $name"
}

data class Issue(
    val id: String, val project: String, val type: String,
    val priority: String, val description: String
)

class ImportantIssuePredicate(val project: String) : (Issue) -> Boolean {
    override fun invoke(issue: Issue): Boolean {
        return issue.project == project && issue.isImportant()
    }

    private fun Issue.isImportant(): Boolean {
        return type == "Bug" && (priority == "Major" || priority == "Critical")
    }
}

class DependencyHandler {
    fun compile(coordinate: String) {
        println("Added dependency on $coordinate")
    }

    operator fun invoke(body: DependencyHandler.() -> Unit) {
        body()
    }
}

// Chapter 11.4
infix fun <T> T.myShould(matcher: MyMatcher<T>) = matcher.test(this)

interface MyMatcher<T> {
    fun test(value: T)
}

class MyStartWith(val prefix: String) : MyMatcher<String> {
    override fun test(value: String) {
        if (!value.startsWith(prefix))
            throw AssertionError("String $value does not start with $prefix")
    }
}

object myStart

infix fun String.myShould(x: myStart): MyStartWrapper = MyStartWrapper(this)

class MyStartWrapper(val value: String) {
    infix fun with(prefix: String) =
        if (!value.startsWith(prefix))
            throw AssertionError("String does not start with $prefix: $value")
        else
            Unit
}

val Int.days: Period
    get() = Period.ofDays(this)

val Period.ago: LocalDate
    get() = LocalDate.now() - this

val Period.fromNow: LocalDate
    get() = LocalDate.now() + this

