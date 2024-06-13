package com.therxmv.ershu.ui.base

import cafe.adriel.voyager.core.model.ScreenModel
import com.therxmv.ershu.data.models.AllCallsScheduleModel
import com.therxmv.ershu.data.source.remote.ERSHUApi
import com.therxmv.ershu.data.source.remote.isFailure
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BaseViewModel(
    private val ershuApi: ERSHUApi,
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
            val callsSchedule = ershuApi.getCallSchedule()

            _callsState.update {
                it.copy(
                    callsModel = callsSchedule.value,
                )
            }

            _isOffline.update { callsSchedule.isFailure() }
        }
    }

    data class CallsState(
        val callsModel: AllCallsScheduleModel? = null,
        val isDialogVisible: Boolean = false,
    )
}