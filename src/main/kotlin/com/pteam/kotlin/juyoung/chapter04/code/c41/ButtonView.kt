package com.pteam.kotlin.juyoung.chapter04.code.c41

class ButtonView: View {
    override fun getCurrentState(): State = ButtonState()
    override fun restoreState(state: State) { }
//    class ButtonState : State { }
    inner class ButtonState: State {
        fun getCurrentState(): ButtonView = this@ButtonView
    }
}