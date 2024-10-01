package com.therxmv.ershu.ui.schedule.viewmodel.utils

import com.therxmv.ershu.data.models.ScheduleModel

sealed class ScheduleUiState {
    data object Loading : ScheduleUiState()
    data class Success(val schedule: ScheduleModel) : ScheduleUiState()
}