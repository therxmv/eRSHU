package com.therxmv.ershu.ui.schedule.utils

import com.therxmv.ershu.data.models.LessonModel
import com.therxmv.ershu.data.reminders.event.model.ReminderModel

sealed interface ScheduleUiEvent {
    data class ExpandDay(val index: Int): ScheduleUiEvent
    data class SetNotification(val item: LessonModel, val faculty: String): ScheduleUiEvent
    data class DeleteNotification(val reminder: ReminderModel): ScheduleUiEvent
    data class PermissionDialogAction(val isDeny: Boolean = false): ScheduleUiEvent
}