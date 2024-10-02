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
import com.therxmv.ershu.ui.base.BaseViewModel.Companion.toggleDialog
import com.therxmv.ershu.ui.base.views.OfflineBanner
import com.therxmv.ershu.ui.base.views.calls.CallsDialog

abstract class BaseScreen : Screen {

    @Composable
    fun BaseContent(childContent: @Composable (Modifier) -> Unit) {
        val viewModel = getScreenModel<BaseViewModel>()

        val callsState by viewModel.callsState.collectAsState()
        val offlineState by viewModel.offlineState.collectAsState()

        CallsDialog(
            isVisible = callsState.isDialogVisible,
            callsModel = callsState.callsModel,
            onDismiss = { toggleDialog() },
            onCopy = viewModel::copyCallsAnalytics
        )

        if (offlineState.isOffline) {
            OfflineBanner(isBadRequest = offlineState.isBadRequest)
        }

        LaunchedEffect(callsState.isDialogVisible) {
            if (callsState.isDialogVisible) {
                viewModel.loadCalls()
            }
        }

        childContent(Modifier.padding(top = if (offlineState.isOffline) 86.dp else 0.dp))
    }
}