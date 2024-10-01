package com.therxmv.ershu.ui.rating.viewmodel.utils

sealed class InputState {
    data object Error : InputState()
    data class Value(val value: String) : InputState()
}