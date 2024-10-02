package com.therxmv.ershu.ui.exam.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ExamCalendarContent(
    modifier: Modifier = Modifier,
    data: ExamCalendarData,
) {
    LazyColumn(
        modifier = modifier
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        examItems(data.items)
    }
}

private fun LazyListScope.examItems(items: List<ExamCalendarData.Item>) {
    items(items) {
        when (it) {
            is ExamCalendarData.Item.Title -> TitleItem(it.title)

            is ExamCalendarData.Item.Exam -> ExamItem(
                teacher = it.teacher,
                lesson = it.lesson,
                date = it.date,
            )

            is ExamCalendarData.Item.Zalik -> ZalikItem(it.lesson)

            is ExamCalendarData.Item.EmptyPlaceholder -> EmptyPlaceholderItem(it.text)
        }
    }
}

@Composable
private fun EmptyPlaceholderItem(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun TitleItem(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
private fun ExamItem(
    teacher: String,
    lesson: String,
    date: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = date,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = lesson,
                style = MaterialTheme.typography.titleMedium,
            )

            Text(
                text = teacher,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun ZalikItem(lesson: String) {
    LessonTitle(lesson)
}

@Composable
private fun LessonTitle(name: String) {
    Text(
        text = name,
        style = MaterialTheme.typography.titleMedium,
    )
}