package com.therxmv.ershu.ui.schedule

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.therxmv.ershu.Res
import com.therxmv.ershu.di.getScreenModel
import com.therxmv.ershu.ui.views.ProgressIndicator
import com.therxmv.ershu.ui.schedule.utils.ScheduleUiEvent
import com.therxmv.ershu.ui.schedule.utils.mapWithGroups
import com.therxmv.ershu.ui.schedule.views.dayItem
import com.therxmv.ershu.ui.views.calls.CallsDialog
import com.therxmv.ershu.ui.views.OfflineBanner
import com.therxmv.ershu.ui.views.ScreenTitleProvider
import com.therxmv.ershu.ui.views.calls.CallsScreen

class ScheduleScreen(
    private val facultyPath: String,
    val year: String,
    val specialty: String,
) : Screen, CallsScreen(), ScreenTitleProvider {

    companion object {
        fun createScreen(facultyPath: String?, year: String?, specialty: String?) = ScheduleScreen(
            facultyPath.orEmpty(),
            year.orEmpty(),
            specialty.orEmpty(),
        )
    }

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<ScheduleViewModel>()
        val uiState by viewModel.uiState.collectAsState()

        val callsDialogState = isDialogOpen.collectAsState().value

        LaunchedEffect(Unit) {
            viewModel.loadSchedule(facultyPath, year, specialty)
        }

        if (callsDialogState) {
            CallsDialog(
                uiState.callsSchedule,
            ) {
                toggleDialog()
            }
        }

        Column {
            if (uiState.isOffline) {
                OfflineBanner()
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
                                dayItem(
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
                ProgressIndicator()
            }
        }
    }

    override fun getTitle() = "${Res.string.schedule_title} $specialty"
}