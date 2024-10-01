package com.therxmv.ershu.ui.schedule.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.therxmv.ershu.Res
import com.therxmv.ershu.data.models.LessonModel
import com.therxmv.ershu.data.reminders.event.model.ReminderModel
import com.therxmv.ershu.ui.schedule.viewmodel.utils.mapWithGroups
import com.therxmv.ershu.utils.toDayOfWeek
import compose.icons.FeatherIcons
import compose.icons.feathericons.Edit

@Composable
fun ScheduleList(
    modifier: Modifier = Modifier,
    schedule: List<List<LessonModel>>,
    reminders: List<ReminderModel>,
    isExpanded: (Int) -> Boolean,
    onDayClick: (Int) -> Unit,
    setNotification: (LessonModel) -> Unit,
    deleteNotification: (ReminderModel) -> Unit,
    onEditClick: () -> Unit,
    sendAnalytics: (String) -> Unit,
) {
    Box {
        FilledTonalIconButton(
            modifier = Modifier
                .size(100.dp)
                .padding(18.dp)
                .align(Alignment.BottomEnd)
                .zIndex(10f)
                .shadow(elevation = 8.dp, shape = CircleShape),
            onClick = onEditClick,
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = MaterialTheme.colorScheme.tertiary
            )
        ) {
            Icon(
                modifier = Modifier
                    .padding(12.dp)
                    .size(28.dp),
                imageVector = FeatherIcons.Edit,
                contentDescription = "Change",
            )
        }

        Column(
            modifier = modifier
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
                            sendAnalytics = sendAnalytics,
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }
            }
        }
    }
}