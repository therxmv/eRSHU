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
import com.therxmv.ershu.ui.schedule.utils.mapWithGroups

@Composable
fun ScheduleList(
    schedule: List<List<LessonModel>>,
    isExpanded: (Int) -> Boolean,
    dayName: (Int) -> String,
    onDayClick: (Int) -> Unit,
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
                        dayName = dayName(index + 1),
                        scheduleList = lessonModels.mapWithGroups(),
                        isExpanded = isExpanded(index),
                        onDayClick = {
                            onDayClick(index)
                        },
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}