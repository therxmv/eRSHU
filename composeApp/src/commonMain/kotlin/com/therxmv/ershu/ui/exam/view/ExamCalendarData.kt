package com.therxmv.ershu.ui.exam.view

import androidx.compose.runtime.Immutable

@Immutable
data class ExamCalendarData(
    val items: List<Item>,
) {

    sealed interface Item {
        data class Title(val title: String) : Item

        data class EmptyPlaceholder(val text: String) : Item

        data class Exam(
            val teacher: String,
            val lesson: String,
            val date: String,
        ) : Item

        data class Zalik(
            val lesson: String,
        ) : Item
    }
}