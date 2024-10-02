package com.therxmv.ershu.ui.home.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.core.screen.Screen
import com.therxmv.ershu.Res
import com.therxmv.ershu.analytics.AnalyticsApi
import com.therxmv.ershu.data.models.toUiData
import com.therxmv.ershu.data.source.local.profile.ProfileLocalSourceApi
import com.therxmv.ershu.ui.base.AppbarTitleStore
import com.therxmv.ershu.ui.exam.view.ExamCalendarScreen
import com.therxmv.ershu.ui.home.view.HomeItemModel
import com.therxmv.ershu.ui.home.viewmodel.utils.HomeItems
import com.therxmv.ershu.ui.home.viewmodel.utils.HomeItems.EXAMS
import com.therxmv.ershu.ui.home.viewmodel.utils.HomeItems.PROFILE
import com.therxmv.ershu.ui.home.viewmodel.utils.HomeItems.RATING
import com.therxmv.ershu.ui.home.viewmodel.utils.HomeItems.SCHEDULE
import com.therxmv.ershu.ui.home.viewmodel.utils.HomeUiEvent
import com.therxmv.ershu.ui.home.viewmodel.utils.HomeUiState
import com.therxmv.ershu.ui.home.viewmodel.utils.HomeUiState.EmptyProfile
import com.therxmv.ershu.ui.home.viewmodel.utils.HomeUiState.Loading
import com.therxmv.ershu.ui.home.viewmodel.utils.HomeUiState.Ready
import com.therxmv.ershu.ui.profile.view.ProfileScreen
import com.therxmv.ershu.ui.rating.view.RatingScreen
import com.therxmv.ershu.ui.schedule.view.ScheduleScreen
import compose.icons.FeatherIcons
import compose.icons.feathericons.Calendar
import compose.icons.feathericons.DivideSquare
import compose.icons.feathericons.List
import compose.icons.feathericons.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val profileLocalSourceApi: ProfileLocalSourceApi,
    private val analyticsApi: AnalyticsApi,
    private val appbarTitleStore: AppbarTitleStore,
) : ScreenModel {

    private val _uiState = MutableStateFlow<HomeUiState>(Loading)
    val uiState = _uiState.asStateFlow()

    init {
        checkIfProfileExists()
    }

    fun setTitle() {
        appbarTitleStore.titleFlow.update { Res.string.app_name }
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.ItemClick -> {
                analyticsApi.homeItemClicked(event.item.name)
                event.navigator.push(
                    getScreen(event.item)
                )
            }
        }
    }

    private fun getScreen(item: HomeItems): Screen = when (item) {
        SCHEDULE -> ScheduleScreen(profileLocalSourceApi.getProfileInfo()?.toUiData())
        PROFILE -> ProfileScreen()
        EXAMS -> ExamCalendarScreen()
        RATING -> RatingScreen()
    }

    private fun checkIfProfileExists() {
        screenModelScope.launch {
            // TODO fix for calls loading. Maybe use NavigatorScreenModel
            delay(500)

            val profile = profileLocalSourceApi.getProfileInfo()

            _uiState.update {
                if (profile == null) {
                    EmptyProfile
                } else {
                    Ready(getHomeItems())
                }
            }
        }
    }

    private fun getHomeItems() = listOf(
        HomeItemModel(
            id = SCHEDULE,
            title = Res.string.home_schedule,
            icon = FeatherIcons.List,
        ),
        HomeItemModel(
            id = PROFILE,
            title = Res.string.home_profile,
            icon = FeatherIcons.User,
        ),
        HomeItemModel(
            id = EXAMS,
            title = Res.string.exams_title,
            icon = FeatherIcons.Calendar,
        ),
        HomeItemModel(
            id = RATING,
            title = Res.string.rating_title,
            icon = FeatherIcons.DivideSquare,
        ),
    )
}