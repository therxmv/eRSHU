package com.therxmv.ershu.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExamCalendarModel(
    @SerialName("exams") val exams: List<Exam> = emptyList(),
    @SerialName("zalik") val zalik: List<Exam> = emptyList(),
)

@Serializable
data class Exam(
    @SerialName("teacher") val teacher: String?,
    @SerialName("lesson") val lesson: String,
    @SerialName("date") val date: String?,
)