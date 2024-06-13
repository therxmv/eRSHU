package com.therxmv.ershu.ui.profile

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.therxmv.ershu.data.models.AllCallsScheduleModel
import com.therxmv.ershu.data.source.local.profile.ProfileLocalSourceApi
import com.therxmv.ershu.data.source.remote.ERSHUApi
import com.therxmv.ershu.data.source.remote.isFailure
import com.therxmv.ershu.ui.profile.utils.ProfileDropdown
import com.therxmv.ershu.ui.profile.utils.ProfileSelectedFieldsState
import com.therxmv.ershu.ui.profile.utils.ProfileUiEvent
import com.therxmv.ershu.ui.profile.utils.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val ershuApi: ERSHUApi,
    private val profileLocalSourceApi: ProfileLocalSourceApi
) : ScreenModel {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    private val _selectedState = MutableStateFlow(ProfileSelectedFieldsState())
    val selectedState = _selectedState.asStateFlow()

    private val _facultyButtonState = MutableStateFlow(true)
    val facultyButtonState = _facultyButtonState.asStateFlow()

    private val _isOffline = MutableStateFlow(false)
    val isOffline = _isOffline.asStateFlow()

    private val _expandedMap = MutableStateFlow(
        mapOf(
            ProfileDropdown.FACULTY to false,
            ProfileDropdown.YEAR to false,
            ProfileDropdown.SPECIALTY to false,
        )
    )
    val expandedMap = _expandedMap.asStateFlow()

    private val _callsScheduleState = MutableStateFlow<AllCallsScheduleModel?>(null)
    val callsScheduleState = _callsScheduleState.asStateFlow()

    private val profile by lazy {
        profileLocalSourceApi.getProfileInfo()
    }

    init {
        loadData()
    }

    fun onEvent(event: ProfileUiEvent) {
        when (event) {
            is ProfileUiEvent.SelectFaculty -> {
                _selectedState.update {
                    it.copy(
                        selectedFaculty = event.faculty,
                    )
                }

                _facultyButtonState.update { true }

                clearSpecialtySelection()
            }

            is ProfileUiEvent.SelectYear -> {
                _selectedState.update {
                    it.copy(
                        selectedYear = event.year,
                        selectedSpecialty = null,
                    )
                }
            }

            is ProfileUiEvent.SelectSpecialty -> {
                _selectedState.update {
                    it.copy(
                        selectedSpecialty = event.specialty,
                    )
                }
            }

            is ProfileUiEvent.Continue -> {
                with(_selectedState.value) {
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

                _facultyButtonState.update { false }
            }

            is ProfileUiEvent.ToggleDropdownState -> {
                val map = _expandedMap.value.toMutableMap()

                map[event.dropdown] = event.value

                _expandedMap.update { map }
            }
        }
    }

    private fun clearSpecialtySelection() {
        _uiState.update {
            it.copy(
                yearsList = emptyList(),
                specialtiesList = emptyList(),
            )
        }

        _selectedState.update {
            it.copy(
                selectedYear = null,
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

        _callsScheduleState.update { callsSchedule }
    }

    private suspend fun loadAllFaculties() {
        val result = ershuApi.getAllFaculties()
        val allFaculties = result.value.allFaculties

        _uiState.update {
            it.copy(facultiesList = allFaculties)
        }

        _isOffline.update { result.isFailure() }

        _selectedState.update { state ->
            state.copy(
                selectedFaculty = allFaculties.find { it.folderName == profile?.faculty },
            )
        }

        if (profile?.faculty.isNullOrEmpty().not()) {
            onEvent(ProfileUiEvent.LoadSpecialties)
        }
    }

    private suspend fun loadAllSpecialties() {
        val facultyPath = _selectedState.value.selectedFaculty?.folderName.orEmpty()

        val result = ershuApi.getAllSpecialties(facultyPath)
        val allSpecialties = result.value
        val yearsList = List(allSpecialties.allYears.size) { index ->
            "${index + 1}"
        }

        _uiState.update { state ->
            state.copy(
                yearsList = yearsList,
                specialtiesList = allSpecialties.allYears,
            )
        }

        _isOffline.update { result.isFailure() }

        _selectedState.update { state ->
            state.copy(
                selectedYear = yearsList.find { it == profile?.year },
                selectedSpecialty = allSpecialties.allYears.flatten().find { it.specialtyName == profile?.specialty },
            )
        }
    }
}