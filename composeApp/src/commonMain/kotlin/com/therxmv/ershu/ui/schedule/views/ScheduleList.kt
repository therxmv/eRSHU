package com.therxmv.ershu.ui.schedule.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.therxmv.ershu.Res
import com.therxmv.ershu.data.models.LessonModel
import com.therxmv.ershu.data.reminders.event.model.ReminderModel
import com.therxmv.ershu.ui.schedule.utils.mapWithGroups
import com.therxmv.ershu.utils.toDayOfWeek

@Composable
fun ScheduleList(
    schedule: List<List<LessonModel>>,
    reminders: List<ReminderModel>,
    isExpanded: (Int) -> Boolean,
    onDayClick: (Int) -> Unit,
    setNotification: (LessonModel) -> Unit,
    deleteNotification: (ReminderModel) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 24.dp, end = 24.dp, bottom = 12.dp),
    ) {
        if (schedule.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = Res.string.schedule_no_connection,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                )
            }
        } else {
            LazyColumn {
                schedule.forEachIndexed { index, lessonModels ->
                    dayItem(
                        dayName = index.toDayOfWeek(),
                        scheduleList = lessonModels.mapWithGroups(),
                        reminders = reminders,
                        isExpanded = isExpanded(index),
                        onDayClick = {
                            onDayClick(index)
                        },
                        setNotification = setNotification,
                        deleteNotification = deleteNotification,
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}