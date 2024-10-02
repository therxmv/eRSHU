package com.therxmv.ershu.ui.base

import cafe.adriel.voyager.core.model.ScreenModel
import com.therxmv.ershu.analytics.AnalyticsApi
import com.therxmv.ershu.data.models.AllCallsScheduleModel
import com.therxmv.ershu.data.source.remote.ERSHUApi
import com.therxmv.ershu.data.source.remote.Result
import com.therxmv.ershu.utils.Analytics.CALLS_CLICK
import com.therxmv.ershu.utils.Analytics.CALLS_COPY_CLICK
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BaseViewModel(
    private val ershuApi: ERSHUApi,
    private val analyticsApi: AnalyticsApi,
) : ScreenModel {

    val callsState = _callsState.asStateFlow()
    val offlineState = _offlineState.asStateFlow()

    companion object {
        private val _offlineState = MutableStateFlow(OfflineState())
        private val _callsState = MutableStateFlow(CallsState())

        fun toggleDialog() {
            _callsState.update {
                it.copy(
                    isDialogVisible = it.isDialogVisible.not()
                )
            }
        }

        fun setChildIsOffline(isOffline: Boolean, isBadRequest: Boolean) {
            _offlineState.update {
                OfflineState(
                    isOffline = isOffline,
                    isBadRequest = isBadRequest,
                )
            }
        }
    }

    suspend fun loadCalls() {
        if (_callsState.value.callsModel == null) {
            analyticsApi.onClickEvent(CALLS_CLICK)
            val callsSchedule = ershuApi.getCallSchedule()

            _callsState.update {
                it.copy(
                    callsModel = callsSchedule.value,
                )
            }

            val failure = callsSchedule as? Result.Failure
            setChildIsOffline(
                isOffline = failure != null,
                isBadRequest = failure?.isBadRequest ?: false,
            )
        }
    }

    fun copyCallsAnalytics() {
        analyticsApi.onClickEvent(CALLS_COPY_CLICK)
    }

    data class CallsState(
        val callsModel: AllCallsScheduleModel? = null,
        val isDialogVisible: Boolean = false,
    )

    data class OfflineState(
        val isOffline: Boolean = false,
        val isBadRequest: Boolean = false,
    )
}