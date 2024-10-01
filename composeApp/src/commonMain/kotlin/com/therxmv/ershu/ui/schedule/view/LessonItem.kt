package com.therxmv.ershu.ui.schedule.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.therxmv.ershu.Res
import com.therxmv.ershu.data.models.LessonModel
import com.therxmv.ershu.data.reminders.event.model.ReminderModel
import com.therxmv.ershu.ui.base.views.CopyIconButton
import com.therxmv.ershu.ui.theme.ERSHUShapes.lastLessonShape
import com.therxmv.ershu.ui.theme.ERSHUShapes.lessonShape
import com.therxmv.ershu.utils.Analytics.COPY_LINK_CLICK
import com.therxmv.ershu.utils.Analytics.ONLINE_LESSON_CLICK
import com.therxmv.ershu.utils.isValidLink
import compose.icons.FeatherIcons
import compose.icons.feathericons.Bell
import compose.icons.feathericons.BellOff
import compose.icons.feathericons.Link

@Composable
fun LessonItem(
    item: LessonModel,
    isNotLast: Boolean,
    reminder: ReminderModel?,
    setNotification: (LessonModel) -> Unit,
    deleteNotification: (ReminderModel) -> Unit,
    sendAnalytics: (String) -> Unit,
) {
    val uriHandler = LocalUriHandler.current
    val lessonLink = item.link.orEmpty()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                enabled = lessonLink.isNotEmpty() && isValidLink(lessonLink)
            ) {
                sendAnalytics(ONLINE_LESSON_CLICK)
                uriHandler.openLink(lessonLink)
            },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = if (isNotLast) lessonShape else lastLessonShape,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 8.dp,
                    vertical = 12.dp,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            item.lessonNumber?.takeIf { it.isNotEmpty() }?.let {
                Text(
                    text = it,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.secondary,
                )
                Spacer(Modifier.width(10.dp))
            }
            Text(
                modifier = Modifier.weight(1F),
                text = item.lessonName ?: Res.string.schedule_no_lesson,
                overflow = TextOverflow.Ellipsis,
                maxLines = 5,
            )

            if (lessonLink.isNotEmpty()) {
                if (isValidLink(lessonLink)) {
                    CopyIconButton(
                        icon = FeatherIcons.Link,
                        text = lessonLink,
                        onCLick = {
                            sendAnalytics(COPY_LINK_CLICK)
                        }
                    )
                } else {
                    Text(
                        text = lessonLink,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
            }

            if (item.lessonName != null) {
                IconButton(
                    onClick = {
                        if (reminder == null) {
                            setNotification(item)
                        } else {
                            deleteNotification(reminder)
                        }
                    },
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = reminder.getIcon(),
                        contentDescription = "Notification",
                        tint = reminder.getIconTint(),
                    )
                }
            }
        }

        if (isNotLast) {
            Divider(
                thickness = 2.dp,
                color = MaterialTheme.colorScheme.surface,
            )
        }
    }
}

@Composable
private fun ReminderModel?.getIconTint() = if (this == null) {
    MaterialTheme.colorScheme.onSurfaceVariant
} else {
    MaterialTheme.colorScheme.primary
}

private fun ReminderModel?.getIcon() = if (this == null) {
    FeatherIcons.BellOff
} else {
    FeatherIcons.Bell
}

private fun UriHandler.openLink(link: String) {
    try {
        openUri(link)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}