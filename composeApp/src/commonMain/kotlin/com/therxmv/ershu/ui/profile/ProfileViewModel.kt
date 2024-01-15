package com.therxmv.ershu.ui.profile

import com.therxmv.ershu.data.source.local.profile.ProfileLocalSource
import com.therxmv.ershu.data.source.local.profile.ProfileLocalSourceApi
import com.therxmv.ershu.data.source.remote.ERSHUApi
import com.therxmv.ershu.data.source.remote.ERSHUService
import com.therxmv.ershu.ui.profile.utils.ProfileUiEvent
import com.therxmv.ershu.ui.profile.utils.ProfileUiState
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val ershuApi: ERSHUApi = ERSHUService(),
    private val profileLocalSourceApi: ProfileLocalSourceApi = ProfileLocalSource(),
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadAllSpecialties()
    }

    fun onEvent(event: ProfileUiEvent) {
        when (event) {
            is ProfileUiEvent.SelectYear -> {
                _uiState.update {
                    it.copy(
                        selectedYear = event.year,
                        selectedSpecialty = null,
                    )
                }
            }

            is ProfileUiEvent.SelectSpecialty -> {
                _uiState.update {
                    it.copy(
                        selectedSpecialty = event.specialty,
                    )
                }
            }

            is ProfileUiEvent.Continue -> {
                profileLocalSourceApi.setProfileInfo(_uiState.value.selectedYear, _uiState.value.selectedSpecialty?.specialtyName)
                event.onComplete()
            }
        }
    }

    private fun loadAllSpecialties() {
        viewModelScope.launch {
            val allSpecialties = ershuApi.getAllSpecialties()
            val yearsList = List(allSpecialties.allYears.size) { index ->
                "${index + 1}"
            }

            val profile = profileLocalSourceApi.getProfileInfo()

            _uiState.update { state ->
                state.copy(
                    yearsList = yearsList,
                    selectedYear = yearsList.firstOrNull { it == profile?.year },
                    specialtiesList = allSpecialties.allYears,
                    selectedSpecialty = allSpecialties.allYears.flatten().firstOrNull { it.specialtyName == profile?.specialty },
                )
            }
        }
    }
}