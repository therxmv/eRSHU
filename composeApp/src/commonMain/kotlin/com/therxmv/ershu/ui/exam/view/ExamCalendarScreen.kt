package com.therxmv.ershu.ui.exam.view

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.therxmv.ershu.di.getScreenModel
import com.therxmv.ershu.ui.base.BaseScreen
import com.therxmv.ershu.ui.base.views.ProgressIndicator
import com.therxmv.ershu.ui.exam.viewmodel.ExamCalendarViewModel

class ExamCalendarScreen : BaseScreen() {

    override val key = "ExamCalendarScreen"

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<ExamCalendarViewModel>()
        val dataState = viewModel.dataStateFlow.collectAsState().value

        LaunchedEffect(Unit) {
            viewModel.loadData()
        }

        super.BaseContent { modifier ->
            Crossfade(
                targetState = dataState,
                animationSpec = tween(500),
            ) {
                when (it) {
                    is ExamCalendarViewModel.DataState.Ready -> {
                        ExamCalendarContent(
                            modifier = modifier,
                            data = it.data,
                        )
                    }

                    is ExamCalendarViewModel.DataState.Loading -> ProgressIndicator()

                    else -> {}
                }
            }
        }
    }
}