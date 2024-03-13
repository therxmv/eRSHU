package com.therxmv.ershu.ui.schedule.utils

import com.therxmv.ershu.data.models.ScheduleModel

sealed interface ScheduleUiState {
    data object Loading : ScheduleUiState
    data class Success(val schedule: ScheduleModel) : ScheduleUiState
}