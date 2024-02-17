package com.therxmv.ershu.ui.schedule.utils

import com.therxmv.ershu.data.models.LessonModel

fun List<LessonModel>.mapWithGroups() = if (this.firstOrNull()?.lessonNumber.isNullOrEmpty()) {
    this
} else {
    this.groupBy { it.lessonNumber }.values.map { list ->
        list.mapIndexed { index, item ->
            if (list.size > 1) {
                item.copy(lessonNumber = "${item.lessonNumber}.${index + 1}")
            } else {
                item
            }
        }
    }.flatten()
}