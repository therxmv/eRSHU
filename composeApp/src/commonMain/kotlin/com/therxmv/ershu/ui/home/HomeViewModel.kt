package com.therxmv.ershu.ui.home

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.core.screen.Screen
import com.therxmv.ershu.Res
import com.therxmv.ershu.data.models.toUiData
import com.therxmv.ershu.data.source.local.profile.ProfileLocalSourceApi
import com.therxmv.ershu.ui.home.utils.HomeItems
import com.therxmv.ershu.ui.home.utils.HomeItems.PROFILE
import com.therxmv.ershu.ui.home.utils.HomeItems.SCHEDULE
import com.therxmv.ershu.ui.home.utils.HomeUiEvent
import com.therxmv.ershu.ui.home.utils.HomeUiState
import com.therxmv.ershu.ui.home.utils.HomeUiState.EmptyProfile
import com.therxmv.ershu.ui.home.utils.HomeUiState.Loading
import com.therxmv.ershu.ui.home.utils.HomeUiState.Ready
import com.therxmv.ershu.ui.profile.ProfileScreen
import com.therxmv.ershu.ui.schedule.ScheduleScreen
import compose.icons.FeatherIcons
import compose.icons.feathericons.List
import compose.icons.feathericons.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val profileLocalSourceApi: ProfileLocalSourceApi,
) : ScreenModel {

    private val _uiState = MutableStateFlow<HomeUiState>(Loading)
    val uiState = _uiState.asStateFlow()

    init {
        checkIfProfileExists()
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.ItemClick -> {
                event.navigator.push(
                    getScreen(event.item)
                )
            }
        }
    }

    private fun getScreen(item: HomeItems): Screen = when (item) {
        SCHEDULE -> ScheduleScreen(profileLocalSourceApi.getProfileInfo()?.toUiData())
        PROFILE -> ProfileScreen()
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
    )
}