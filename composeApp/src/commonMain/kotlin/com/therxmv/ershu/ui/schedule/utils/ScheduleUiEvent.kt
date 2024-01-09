package com.therxmv.ershu.ui.schedule.utils

sealed class ScheduleUiEvent {
    data class ExpandDay(val index: Int): ScheduleUiEvent()
}