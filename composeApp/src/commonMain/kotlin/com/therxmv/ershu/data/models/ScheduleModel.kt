package com.therxmv.ershu.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleModel(
    @SerialName("week") var week: List<List<LessonModel>> = emptyList(),
)

@Serializable
data class LessonModel(
    @SerialName("lesson_name") val lessonName: String? = null,
    @SerialName("lesson_number") val lessonNumber: String? = null,
    @SerialName("link") val link: String? = null,
)