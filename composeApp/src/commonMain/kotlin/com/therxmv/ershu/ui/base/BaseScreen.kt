package com.therxmv.ershu.ui.base

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.therxmv.ershu.di.getScreenModel
import com.therxmv.ershu.ui.views.OfflineBanner
import com.therxmv.ershu.ui.views.calls.CallsDialog
import com.therxmv.ershu.ui.base.BaseViewModel.Companion.toggleDialog

abstract class BaseScreen : Screen {

    @Composable
    fun BaseContent(childContent: @Composable (Modifier) -> Unit) {
        val viewModel = getScreenModel<BaseViewModel>()

        val callsState by viewModel.callsState.collectAsState()
        val isOffline by viewModel.isOffline.collectAsState()

        if (callsState.isDialogVisible) {
            CallsDialog(callsState.callsModel) {
                toggleDialog()
            }
        }

        if (isOffline) {
            OfflineBanner()
        }

        LaunchedEffect(callsState.isDialogVisible) {
            if (callsState.isDialogVisible) {
                viewModel.loadCalls()
            }
        }

        childContent(Modifier.padding(top = if (isOffline) 48.dp else 0.dp))
    }
}