package com.therxmv.ershu.ui.views.calls

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

abstract class CallsScreen {

    val isDialogOpen = MutableStateFlow(false)

    fun toggleDialog() {
        isDialogOpen.update {
            it.not()
        }
    }
}