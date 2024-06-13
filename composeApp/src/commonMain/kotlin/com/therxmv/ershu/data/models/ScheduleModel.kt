package com.therxmv.ershu.data.models

import com.therxmv.ershu.Res
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleModel(
    @SerialName("week") var week: List<List<LessonModel>> = emptyList(),
)

@Serializable
data class LessonModel(
    @SerialName("lessonId") val lessonId: String? = null,
    @SerialName("lesson_name") val lessonName: String? = null,
    @SerialName("lesson_number") val lessonNumber: String? = null,
    @SerialName("link") val link: String? = null,
)

fun LessonModel.toText(): String {
    val number = lessonNumber?.takeUnless { it.isBlank() }
    val name = lessonName ?: Res.string.schedule_no_lesson
    val url = link.orEmpty()

    return if (number == null) {
        "$url $name"
    } else {
        "$number) $name $url"
    }
}