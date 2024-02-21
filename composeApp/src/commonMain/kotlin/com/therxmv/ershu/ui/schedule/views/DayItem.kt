package com.therxmv.ershu.ui.schedule.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.therxmv.ershu.Res
import com.therxmv.ershu.data.models.LessonModel
import com.therxmv.ershu.theme.ERSHUShapes.expandedScheduleDayShape
import com.therxmv.ershu.theme.ERSHUShapes.scheduleDayShape

fun LazyListScope.dayItem(
    dayName: String,
    scheduleList: List<LessonModel>,
    isExpanded: Boolean,
    onDayClick: () -> Unit,
) {
    val isEmpty = scheduleList.isEmpty()

    item {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .clickable(
                    enabled = isEmpty.not()
                ) {
                    onDayClick()
                },
            colors = getDayItemColor(isEmpty),
            shape = if (isExpanded && isEmpty.not()) expandedScheduleDayShape else scheduleDayShape,
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = getDayItemTitle(isEmpty, dayName),
                style = getDayItemTitleStyle(isEmpty),
                fontSize = 20.sp,
            )
        }
    }

    if (isExpanded) {
        itemsIndexed(scheduleList) { index, item ->
            val isNotLast = index < scheduleList.lastIndex

            LessonItem(item, isNotLast)

            if (isNotLast) {
                Divider(
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.surface,
                )
            }
        }
    }
}

@Composable
private fun getDayItemTitleStyle(isEmpty: Boolean) =
    if (isEmpty) {
        TextStyle(
            fontWeight = FontWeight.Normal
        )
    } else {
        TextStyle(
            fontWeight = FontWeight.Bold
        )
    }

@Composable
private fun getDayItemTitle(isEmpty: Boolean, dayName: String) =
    if (isEmpty) {
        "$dayName - ${Res.string.schedule_no_lessons}"
    } else {
        dayName
    }

@Composable
private fun getDayItemColor(isEmpty: Boolean) =
    CardDefaults.cardColors(
        containerColor = if (isEmpty) {
            MaterialTheme.colorScheme.secondaryContainer
        } else {
            MaterialTheme.colorScheme.primary
        }
    )