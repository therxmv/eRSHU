package com.therxmv.ershu.ui.schedule.utils

sealed interface ScheduleUiEvent {
    data class ExpandDay(val index: Int): ScheduleUiEvent
}