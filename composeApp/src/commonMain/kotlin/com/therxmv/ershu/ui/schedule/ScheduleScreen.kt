package com.therxmv.ershu.ui.schedule

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.therxmv.ershu.Res
import com.therxmv.ershu.data.models.ProfileUiData
import com.therxmv.ershu.data.models.toProfile
import com.therxmv.ershu.di.getScreenModel
import com.therxmv.ershu.ui.views.reminders.RemindersPermissionDialog
import com.therxmv.ershu.ui.schedule.utils.ScheduleUiEvent
import com.therxmv.ershu.ui.schedule.utils.ScheduleUiState.Success
import com.therxmv.ershu.ui.schedule.views.ScheduleList
import com.therxmv.ershu.ui.base.ProgressIndicator
import com.therxmv.ershu.ui.views.ScreenTitleProvider
import com.therxmv.ershu.ui.base.BaseScreen
import com.therxmv.ershu.ui.specialtyinfo.SpecialtyInfoScreen

class ScheduleScreen(
    private val profile: ProfileUiData?,
) : BaseScreen(), ScreenTitleProvider {

    override val key = "ScheduleScreen"

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<ScheduleViewModel>()

        val uiState by viewModel.uiState.collectAsState()
        val expandedState by viewModel.expandedList.collectAsState()
        val remindersState by viewModel.remindersState.collectAsState()
        val permissionDialogState by viewModel.permissionDialogState.collectAsState()

        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(Unit) {
            viewModel.loadData(profile?.toProfile())
        }

        DisposableEffect(Unit) {
            onDispose {
                viewModel.resetState()
            }
        }

        super.BaseContent {
            if (permissionDialogState) {
                RemindersPermissionDialog(
                    onClick = {
                        viewModel.onEvent(ScheduleUiEvent.PermissionDialogAction())
                    },
                    onDismiss = {
                        viewModel.onEvent(ScheduleUiEvent.PermissionDialogAction(true))
                    },
                )
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
                        ScheduleList(
                            modifier = it,
                            schedule = (uiState as Success).schedule.week,
                            reminders = remindersState,
                            onDayClick = { viewModel.onEvent(ScheduleUiEvent.ExpandDay(it)) },
                            isExpanded = { expandedState.getOrNull(it) == true },
                            setNotification = { viewModel.onEvent(ScheduleUiEvent.SetNotification(it)) },
                            deleteNotification = { viewModel.onEvent(ScheduleUiEvent.DeleteNotification(it)) },
                            onEditClick = {
                                navigator.push(SpecialtyInfoScreen(tempProfile = profile))
                            },
                        )
                    }

                    else -> ProgressIndicator()
                }
            }
        }
    }

    override fun getTitle() = "${Res.string.schedule_title} ${profile?.specialtyName.orEmpty()}"
}