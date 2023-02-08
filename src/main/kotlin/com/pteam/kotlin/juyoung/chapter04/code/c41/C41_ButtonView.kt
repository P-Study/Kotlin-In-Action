package com.pteam.kotlin.juyoung.chapter04.code.c41

class C41_ButtonView: View {
    override fun getCurrentState(): State = ButtonState()
    override fun restoreState(state: State) { }
//    class ButtonState : State { }
    inner class ButtonState: State {
        fun getCurrentState(): C41_ButtonView = this@C41_ButtonView
    }
}