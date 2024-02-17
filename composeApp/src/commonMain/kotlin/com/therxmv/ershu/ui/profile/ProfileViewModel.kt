package com.therxmv.ershu.ui.profile

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.therxmv.ershu.data.source.local.profile.ProfileLocalSourceApi
import com.therxmv.ershu.data.source.remote.ERSHUApi
import com.therxmv.ershu.data.source.remote.isFailure
import com.therxmv.ershu.ui.profile.utils.ProfileUiEvent
import com.therxmv.ershu.ui.profile.utils.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val ershuApi: ERSHUApi,
    private val profileLocalSourceApi: ProfileLocalSourceApi,
) : ScreenModel {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    private val profile by lazy {
        profileLocalSourceApi.getProfileInfo()
    }

    init {
        loadData()
    }

    fun onEvent(event: ProfileUiEvent) {
        when (event) {
            is ProfileUiEvent.SelectFaculty -> {
                _uiState.update {
                    it.copy(
                        selectedFaculty = event.faculty,
                        isFacultyButtonEnabled = true,
                    )
                }
                clearSpecialtySelection()
            }

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
                with(_uiState.value) {
                    profileLocalSourceApi.setProfileInfo(
                        selectedYear,
                        selectedFaculty?.folderName,
                        selectedSpecialty?.specialtyName,
                    )
                }
                event.onComplete()
            }

            is ProfileUiEvent.LoadSpecialties -> {
                screenModelScope.launch {
                    loadAllSpecialties()
                }
                _uiState.update {
                    it.copy(
                        isFacultyButtonEnabled = false,
                    )
                }
            }

            is ProfileUiEvent.ToggleDropdownState -> {
                val map = _uiState.value.dropdownExpandedMap.toMutableMap()

                map[event.dropdown] = event.value

                _uiState.update {
                    it.copy(
                        dropdownExpandedMap = map,
                    )
                }
            }
        }
    }

    private fun clearSpecialtySelection() {
        _uiState.update {
            it.copy(
                yearsList = emptyList(),
                selectedYear = null,
                specialtiesList = emptyList(),
                selectedSpecialty = null,
            )
        }
    }

    private fun loadData() {
        screenModelScope.launch {
            loadAllFaculties()
            loadCallsSchedule()
        }
    }

    private suspend fun loadCallsSchedule() {
        val callsSchedule = ershuApi.getCallSchedule().value

        _uiState.update { state ->
            state.copy(
                callsSchedule = callsSchedule,
            )
        }
    }

    private suspend fun loadAllFaculties() {
        val result = ershuApi.getAllFaculties()
        val allFaculties = result.value.allFaculties

        _uiState.update { state ->
            state.copy(
                facultiesList = allFaculties,
                selectedFaculty = allFaculties.find { it.folderName == profile?.faculty },
                isOffline = result.isFailure(),
            )
        }

        if (profile?.faculty.isNullOrEmpty().not()) {
            onEvent(ProfileUiEvent.LoadSpecialties)
        }
    }

    private suspend fun loadAllSpecialties() {
        val facultyPath = _uiState.value.selectedFaculty?.folderName.orEmpty()

        val result = ershuApi.getAllSpecialties(facultyPath)
        val allSpecialties = result.value
        val yearsList = List(allSpecialties.allYears.size) { index ->
            "${index + 1}"
        }

        _uiState.update { state ->
            state.copy(
                yearsList = yearsList,
                selectedYear = yearsList.find { it == profile?.year },
                specialtiesList = allSpecialties.allYears,
                selectedSpecialty = allSpecialties.allYears.flatten().find { it.specialtyName == profile?.specialty },
                isOffline = result.isFailure(),
            )
        }
    }
}