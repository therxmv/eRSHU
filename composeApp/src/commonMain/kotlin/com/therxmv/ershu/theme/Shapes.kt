package com.therxmv.ershu.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

object ERSHUShapes {
    val lessonShape: Shape = RoundedCornerShape(0.dp)
    val lastLessonShape: Shape = RoundedCornerShape(
        bottomEnd = 8.dp,
        bottomStart = 8.dp
    )

    val scheduleDayShape: Shape = RoundedCornerShape(8.dp)
    val expandedScheduleDayShape: Shape = RoundedCornerShape(
        topEnd = 8.dp,
        topStart = 8.dp,
    )
}