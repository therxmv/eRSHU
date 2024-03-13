package com.therxmv.ershu.ui.schedule.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
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

fun LazyListScope.dayItem(
    dayName: String,
    scheduleList: List<LessonModel>,
    isExpanded: Boolean,
    onDayClick: () -> Unit,
) {
    val isEmpty = scheduleList.isEmpty()

    item(
        key = dayName,
    ) {
        ScheduleDayCard(
            modifier = Modifier
                .clickable(
                    enabled = isEmpty.not()
                ) {
                    onDayClick()
                },
            colors = getDayItemColor(isEmpty),
            isExpanded = isExpanded,
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = getDayItemTitle(isEmpty, dayName),
                style = getDayItemTitleStyle(isEmpty),
                fontSize = 20.sp,
            )
        }
    }

    itemsIndexed(
        items = scheduleList,
        key = { index, item ->
            "${dayName}_${index}_${item.lessonNumber}_${item.lessonName}"
        },
    ) { index, item ->
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut(),
        ) {
            val isNotLast = index < scheduleList.lastIndex

            LessonItem(item, isNotLast)
        }
    }
}

@Composable
private fun ScheduleDayCard(
    modifier: Modifier = Modifier,
    colors: CardColors,
    isExpanded: Boolean,
    content: @Composable (ColumnScope.() -> Unit),
) {
    val radius = (if (isExpanded) 0 else 8).dp
    val bottomRadius = animateDpAsState(targetValue = radius)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        colors = colors,
        shape = RoundedCornerShape(
            topEnd = 8.dp,
            topStart = 8.dp,
            bottomEnd = bottomRadius.value,
            bottomStart = bottomRadius.value,
        ),
        content = content,
    )
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