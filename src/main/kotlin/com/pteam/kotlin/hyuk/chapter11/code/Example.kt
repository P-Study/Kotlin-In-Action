package com.pteam.kotlin.hyuk.chapter11.code

import kotlinx.html.*
import kotlinx.html.stream.createHTML

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
    block: DIV.() -> Unit
): String = div(block = block)
