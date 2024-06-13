package com.therxmv.ershu.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

object ERSHUShapes {
    val lessonShape: Shape = RoundedCornerShape(0.dp)
    val lastLessonShape: Shape = RoundedCornerShape(
        bottomEnd = 8.dp,
        bottomStart = 8.dp
    )
}