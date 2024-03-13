package com.therxmv.ershu.ui.schedule

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import com.therxmv.ershu.Res
import com.therxmv.ershu.di.getScreenModel
import com.therxmv.ershu.ui.schedule.utils.ScheduleUiEvent
import com.therxmv.ershu.ui.schedule.utils.ScheduleUiState.Success
import com.therxmv.ershu.ui.schedule.views.ScheduleList
import com.therxmv.ershu.ui.views.OfflineBanner
import com.therxmv.ershu.ui.views.ProgressIndicator
import com.therxmv.ershu.ui.views.ScreenTitleProvider
import com.therxmv.ershu.ui.views.calls.CallsDialog
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
        val callsDialogState by isDialogOpen.collectAsState()
        val expandedState by viewModel.expandedList.collectAsState()
        val callsScheduleState by viewModel.callsScheduleState.collectAsState()
        val isOffline by viewModel.isOffline.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.loadData(facultyPath, year, specialty)
        }

        if (callsDialogState) {
            CallsDialog(callsScheduleState) {
                toggleDialog()
            }
        }

        AnimatedContent(
            targetState = uiState,
            transitionSpec = {
                slideInVertically(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessMediumLow,
                    )
                ) { it / 2 } + fadeIn() togetherWith fadeOut()
            }
        ) { state ->
            when (state) {
                is Success -> {
                    if (isOffline) {
                        OfflineBanner()
                    }

                    ScheduleList(
                        schedule = (uiState as Success).schedule.week,
                        dayName = { viewModel.getDayOfWeek(it) },
                        onDayClick = { viewModel.onEvent(ScheduleUiEvent.ExpandDay(it)) },
                        isExpanded = { expandedState.getOrNull(it) == true }
                    )
                }

                else -> ProgressIndicator()
            }
        }
    }

    override fun getTitle() = "${Res.string.schedule_title} $specialty"
}