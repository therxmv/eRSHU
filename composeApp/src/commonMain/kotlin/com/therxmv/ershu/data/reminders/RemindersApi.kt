package com.therxmv.ershu.data.reminders

import com.therxmv.ershu.data.reminders.event.model.EventModel
import com.therxmv.ershu.data.reminders.event.model.ReminderModel

expect class RemindersApi() {

    fun addNotification(eventModel: EventModel, onComplete: (String) -> Unit)
    fun deleteNotification(reminderModel: ReminderModel)
    fun requestNotificationPermission()
    fun isPermissionGranted(onComplete: (Boolean) -> Unit)
}