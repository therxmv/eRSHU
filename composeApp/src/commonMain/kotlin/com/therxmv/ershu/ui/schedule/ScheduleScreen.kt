package com.therxmv.ershu.ui.schedule

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import com.therxmv.ershu.Res
import com.therxmv.ershu.data.models.LessonModel
import com.therxmv.ershu.theme.ERSHUShapes
import com.therxmv.ershu.ui.schedule.ScheduleViewModel.Companion.isDialogOpen
import com.therxmv.ershu.ui.schedule.utils.ScheduleUiEvent
import com.therxmv.ershu.ui.schedule.utils.mapWithGroups
import com.therxmv.ershu.utils.isValidLink
import compose.icons.FeatherIcons
import compose.icons.feathericons.Copy
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import kotlinx.coroutines.flow.update

class ScheduleScreen(
    val year: String,
    val specialty: String,
) : Screen {

    companion object {
        fun createScreen(year: String?, specialty: String?) = ScheduleScreen(
            year.orEmpty(),
            specialty.orEmpty(),
        )

        private const val VIEW_MODEL_KEY = "ScheduleViewModel"
    }

    fun toggleDialog() {
        isDialogOpen.update {
            it.not()
        }
    }

    @Composable
    override fun Content() {
        val viewModel = getViewModel(VIEW_MODEL_KEY, viewModelFactory { ScheduleViewModel() })
        val uiState by viewModel.uiState.collectAsState()

        val callsDialogState = isDialogOpen.collectAsState().value

        LaunchedEffect(Unit) {
            viewModel.loadSchedule(year, specialty)
        }

        if (callsDialogState) {
            CallsScheduleDialog(
                uiState.callsSchedule,
            ) {
                toggleDialog()
            }
        }

        if (uiState.schedule != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 24.dp, end = 24.dp, bottom = 12.dp),
            ) {
                if (uiState.schedule!!.week.isEmpty()) {
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
                        uiState.schedule!!.week.forEachIndexed { index, lessonModels ->
                            scheduleDayItem(
                                dayName = viewModel.getDayOfWeek(index + 1),
                                scheduleList = lessonModels.mapWithGroups(),
                                isExpanded = uiState.expandedList[index],
                                onDayClick = {
                                    viewModel.onEvent(ScheduleUiEvent.ExpandDay(index))
                                },
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(100.dp))
                        }
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun LessonItem(
    item: LessonModel,
) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.padding(end = 12.dp),
            text = item.lessonNumber,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.secondary,
        )
        Text(
            modifier = Modifier.weight(0.5F),
            text = item.lessonName ?: Res.string.schedule_no_lesson,
            overflow = TextOverflow.Ellipsis,
            maxLines = 5,
        )

        item.link?.let { link ->
            if (isValidLink(link)) {
                IconButton(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(link))
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
                    text = link,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

private fun LazyListScope.scheduleDayItem(
    dayName: String,
    scheduleList: List<LessonModel>,
    isExpanded: Boolean,
    onDayClick: () -> Unit,
) {
    item {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .clickable { onDayClick() },
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = if (isExpanded) ERSHUShapes.expandedScheduleDayShape else ERSHUShapes.scheduleDayShape,
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = dayName,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )
        }
    }

    if (isExpanded) {
        if (scheduleList.isNotEmpty()) {
            itemsIndexed(scheduleList) { index, item ->
                val isNotLast = index < scheduleList.lastIndex
                val uriHandler = LocalUriHandler.current

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            enabled = item.link.isNullOrEmpty().not() && isValidLink(item.link.orEmpty())
                        ) {
                            item.link?.let {
                                try {
                                    uriHandler.openUri(it)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = if (isNotLast) ERSHUShapes.lessonShape else ERSHUShapes.lastLessonShape
                ) {
                    LessonItem(item)
                }

                if (isNotLast) {
                    Divider(
                        thickness = 2.dp,
                        color = MaterialTheme.colorScheme.surface,
                    )
                }
            }
        } else {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = ERSHUShapes.lastLessonShape
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = Res.string.schedule_no_lessons,
                        )
                    }
                }
            }
        }
    }
}