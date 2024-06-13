package com.therxmv.ershu.data.reminders.event

import com.therxmv.ershu.data.models.LessonModel
import com.therxmv.ershu.data.reminders.event.model.EventModel

interface EventProviderApi {
    fun getEvent(item: LessonModel, faculty: String): EventModel
}