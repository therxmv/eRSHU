package com.therxmv.ershu.data.reminders.event

import com.therxmv.ershu.Res
import com.therxmv.ershu.data.models.LessonModel
import com.therxmv.ershu.data.reminders.event.model.EventModel
import com.therxmv.ershu.data.reminders.event.model.ReminderModel
import com.therxmv.ershu.utils.Faculty.PPF
import io.ktor.util.date.GMTDate
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atDate
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class EventProvider : EventProviderApi {

    companion object {
        const val TIME_BEFORE = 1800000 // TODO 2 remind 30min before lesson
    }

    override fun getEvent(item: LessonModel, faculty: String) = EventModel(
        title = Res.string.reminder_title,
        description = item.lessonName.orEmpty(),
        reminderModel = ReminderModel(
            startDate = getStartDate(item, faculty),
            reminderId = GMTDate().timestamp.toString(),
            lessonId = item.lessonId.orEmpty(),
        )
    )

    private fun getStartDate(item: LessonModel, faculty: String): Long {
        return if (item.lessonId != null) {
            val data = item.lessonId.split("-")

            val day = data[0].toInt()
            val lessonNumber = data[1].toInt()

            val time = getTargetTime(faculty, item.link.orEmpty(), lessonNumber)

            getTargetDateTime(day, time)
                .toInstant(TimeZone.currentSystemDefault())
                .toEpochMilliseconds() - TIME_BEFORE

//            Clock.System.now().toEpochMilliseconds() + 60000
        } else {
            0L
        }
    }

    private fun getTargetDateTime(targetDay: Int, targetTime: LocalTime): LocalDateTime {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val time = now.time
        val today = now.date

        val weekSize = DayOfWeek.values().size
        val daysToAdd = (targetDay - today.dayOfWeek.ordinal + weekSize) % weekSize

        val daysUntilTarget = if (daysToAdd == 0) {
            if (time.toMillisecondOfDay() < targetTime.toMillisecondOfDay() - TIME_BEFORE) {
                daysToAdd
            } else {
                weekSize
            }
        } else {
            daysToAdd
        }

        val day = today.plus(DatePeriod(days = daysUntilTarget))

        return targetTime.atDate(day)
    }

    private fun getTargetTime(faculty: String, link: String, lessonNumber: Int) = try {
        when (faculty) {
            PPF -> {
                val time = link.split(":").map { it.toInt() }

                LocalTime(time[0], time[1])
            }

            else -> {
                lessonNumber.getStartTime()
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        lessonNumber.getStartTime()
    }

    private fun Int.getStartTime() = when (this) {
        1 -> LocalTime(8, 0)
        2 -> LocalTime(9, 35)
        3 -> LocalTime(11, 10)
        4 -> LocalTime(12, 45)
        else -> LocalTime(14, 20)
    }
}