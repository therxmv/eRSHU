package com.therxmv.ershu.ui.schedule.utils

import com.therxmv.ershu.data.models.AllCallsScheduleModel
import com.therxmv.ershu.data.models.ScheduleModel

data class ScheduleUiState(
    val schedule: ScheduleModel? = null,
    val callsSchedule: AllCallsScheduleModel? = null,
    val expandedList: List<Boolean> = emptyList(),
    val isDialogOpen: Boolean = false,
)