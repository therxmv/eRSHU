package com.therxmv.ershu.ui.profile

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.therxmv.ershu.data.source.local.profile.ProfileLocalSourceApi
import com.therxmv.ershu.ui.base.ViewModelDisposer
import com.therxmv.ershu.ui.profile.utils.ProfileUiState
import com.therxmv.ershu.ui.profile.utils.ProfileUiState.Ready
import com.therxmv.ershu.ui.profile.utils.ProfileUiState.Loading
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileLocalSourceApi: ProfileLocalSourceApi
) : ScreenModel, ViewModelDisposer {

    private val _profileState = MutableStateFlow<ProfileUiState>(Loading)
    val profileState = _profileState.asStateFlow()

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