package com.therxmv.ershu.ui.schedule.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.therxmv.ershu.Res
import com.therxmv.ershu.data.models.LessonModel
import com.therxmv.ershu.theme.ERSHUShapes.lastLessonShape
import com.therxmv.ershu.theme.ERSHUShapes.lessonShape
import com.therxmv.ershu.utils.isValidLink
import compose.icons.FeatherIcons
import compose.icons.feathericons.Copy

@Composable
fun LessonItem(
    item: LessonModel,
    isNotLast: Boolean,
) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val uriHandler = LocalUriHandler.current
    val lessonLink = item.link.orEmpty()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                enabled = lessonLink.isNotEmpty() && isValidLink(lessonLink)
            ) {
                uriHandler.openLink(lessonLink)
            },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = if (isNotLast) lessonShape else lastLessonShape,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            item.lessonNumber?.takeIf { it.isNotEmpty() }?.let {
                Text(
                    modifier = Modifier.padding(end = 12.dp),
                    text = it,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
            Text(
                modifier = Modifier.weight(0.5F),
                text = item.lessonName ?: Res.string.schedule_no_lesson,
                overflow = TextOverflow.Ellipsis,
                maxLines = 5,
            )

            if (lessonLink.isNotEmpty()) {
                if (isValidLink(lessonLink)) {
                    IconButton(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(lessonLink))
                        },
                    ) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            imageVector = FeatherIcons.Copy,
                            contentDescription = "Copy",
                        )
                    }
                } else {
                    Text(
                        text = lessonLink,
                        style = MaterialTheme.typography.bodySmall,
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

private fun UriHandler.openLink(link: String) {
    try {
        openUri(link)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}