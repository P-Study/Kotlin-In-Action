package com.pteam.kotlin.juyoung.chapter04.code.c41

import java.io.Serializable

interface State: Serializable
interface View {
    fun getCurrentState(): State
    fun restoreState(state: State) {}
}