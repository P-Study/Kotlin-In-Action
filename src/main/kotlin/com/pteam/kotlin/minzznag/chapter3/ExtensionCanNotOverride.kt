package com.pteam.kotlin.minzznag.chapter3

open class View {
    open fun click() = println("View clicked")
}

class Button: View() {
    override fun click() {
        println("Button clicked")
    }
}

fun View.show() = "Im a view"
fun Button.show() = "Im a button"
