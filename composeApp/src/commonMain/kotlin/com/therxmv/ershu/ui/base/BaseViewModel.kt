package com.therxmv.ershu.ui.base

import cafe.adriel.voyager.core.model.ScreenModel
import com.therxmv.ershu.analytics.AnalyticsApi
import com.therxmv.ershu.data.models.AllCallsScheduleModel
import com.therxmv.ershu.data.source.remote.ERSHUApi
import com.therxmv.ershu.data.source.remote.isFailure
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
    val isOffline = _isOffline.asStateFlow()

    companion object {
        private val _isOffline = MutableStateFlow(false)
        private val _callsState = MutableStateFlow(CallsState())

        fun toggleDialog() {
            _callsState.update {
                it.copy(
                    isDialogVisible = it.isDialogVisible.not()
                )
            }
        }

        fun setChildIsOffline(value: Boolean) {
            _isOffline.update { value }
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

            _isOffline.update { callsSchedule.isFailure() }
        }
    }

    fun copyCallsAnalytics() {
        analyticsApi.onClickEvent(CALLS_COPY_CLICK)
    }

    data class CallsState(
        val callsModel: AllCallsScheduleModel? = null,
        val isDialogVisible: Boolean = false,
    )
}