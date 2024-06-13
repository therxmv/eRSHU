package com.therxmv.ershu.data.source.local.mapper

import com.therxmv.ershu.data.models.FacultyModel
import com.therxmv.ershu.data.models.LessonModel
import com.therxmv.ershu.data.models.SpecialtyModel
import com.therxmv.ershu.data.reminders.event.model.ReminderModel
import com.therxmv.ershu.db.Faculty
import com.therxmv.ershu.db.Lesson
import com.therxmv.ershu.db.Reminder
import com.therxmv.ershu.db.Specialty

fun Specialty.toDomain() = SpecialtyModel(this.name.orEmpty())

fun Lesson.toDomain() = LessonModel(
    lessonId = this.lessonId,
    lessonName = this.name,
    lessonNumber = this.number.orEmpty(),
    link = this.link,
)

fun Faculty.toDomain() = FacultyModel(this.name.orEmpty(), this.folder.orEmpty())

fun Reminder.toDomain() = ReminderModel(
    startDate = this.startDate?.toLongOrNull() ?: 0L,
    reminderId = this.reminderId.orEmpty(),
    lessonId = this.lessonId.orEmpty(),
)