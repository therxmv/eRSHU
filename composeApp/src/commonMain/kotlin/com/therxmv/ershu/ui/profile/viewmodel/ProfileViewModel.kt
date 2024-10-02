package com.therxmv.ershu.ui.profile.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.therxmv.ershu.Res
import com.therxmv.ershu.data.source.local.profile.ProfileLocalSourceApi
import com.therxmv.ershu.ui.base.AppbarTitleStore
import com.therxmv.ershu.ui.base.ViewModelDisposer
import com.therxmv.ershu.ui.profile.viewmodel.utils.ProfileUiState
import com.therxmv.ershu.ui.profile.viewmodel.utils.ProfileUiState.Loading
import com.therxmv.ershu.ui.profile.viewmodel.utils.ProfileUiState.Ready
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileLocalSourceApi: ProfileLocalSourceApi,
    appbarTitleStore: AppbarTitleStore,
) : ScreenModel, ViewModelDisposer {

    private val _profileState = MutableStateFlow<ProfileUiState>(Loading)
    val profileState = _profileState.asStateFlow()

    init {
        appbarTitleStore.titleFlow.update { Res.string.profile_title }
    }

    override fun resetState() {
        _profileState.update { Loading }
    }

    fun loadData() {
        screenModelScope.launch {
            delay(200)
            val profile = profileLocalSourceApi.getProfileInfo()

            _profileState.update {
                Ready(
                    profile?.facultyName.orEmpty(),
                    profile?.specialtyName.orEmpty(),
                )
            }
        }
    }
}