package com.therxmv.ershu.ui.specialtyinfo.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.therxmv.ershu.analytics.AnalyticsApi
import com.therxmv.ershu.data.source.local.profile.ProfileLocalSourceApi
import com.therxmv.ershu.data.source.remote.ERSHUApi
import com.therxmv.ershu.data.source.remote.isFailure
import com.therxmv.ershu.db.Profile
import com.therxmv.ershu.ui.base.BaseViewModel
import com.therxmv.ershu.ui.specialtyinfo.viewmodel.utils.SelectedFieldsState
import com.therxmv.ershu.ui.specialtyinfo.viewmodel.utils.SpecialtyInfoDropdown
import com.therxmv.ershu.ui.specialtyinfo.viewmodel.utils.SpecialtyInfoUiEvent
import com.therxmv.ershu.ui.specialtyinfo.viewmodel.utils.SpecialtyInfoUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SpecialtyInfoViewModel(
    private val ershuApi: ERSHUApi,
    private val profileLocalSourceApi: ProfileLocalSourceApi,
    private val analyticsApi: AnalyticsApi,
) : ScreenModel {

    private val _uiState = MutableStateFlow(SpecialtyInfoUiState())
    val uiState = _uiState.asStateFlow()

    private val _selectedState = MutableStateFlow(SelectedFieldsState())
    val selectedState = _selectedState.asStateFlow()

    private val _facultyButtonState = MutableStateFlow(true)
    val facultyButtonState = _facultyButtonState.asStateFlow()

    private val _expandedMap = MutableStateFlow(
        mapOf(
            SpecialtyInfoDropdown.FACULTY to false,
            SpecialtyInfoDropdown.YEAR to false,
            SpecialtyInfoDropdown.SPECIALTY to false,
        )
    )
    val expandedMap = _expandedMap.asStateFlow()

    private var profile: Profile? = null

    fun setProfileData(data: Profile?) {
        profile = data ?: profileLocalSourceApi.getProfileInfo()

        loadData()
    }

    fun onEvent(event: SpecialtyInfoUiEvent) {
        when (event) {
            is SpecialtyInfoUiEvent.SelectFaculty -> {
                _selectedState.update {
                    it.copy(
                        selectedFaculty = event.faculty,
                    )
                }

                _facultyButtonState.update { true }

                clearSpecialtySelection()
            }

            is SpecialtyInfoUiEvent.SelectYear -> {
                _selectedState.update {
                    it.copy(
                        selectedYear = event.year,
                        selectedSpecialty = null,
                    )
                }
            }

            is SpecialtyInfoUiEvent.SelectSpecialty -> {
                _selectedState.update {
                    it.copy(
                        selectedSpecialty = event.specialty,
                    )
                }
            }

            is SpecialtyInfoUiEvent.Save -> {
                screenModelScope.launch {
                    val profile = with(_selectedState.value) {
                        Profile(
                            "",
                            selectedYear,
                            selectedFaculty?.folderName,
                            selectedFaculty?.facultyName,
                            selectedSpecialty?.specialtyName,
                        )
                    }

                    if (event.profile == null) {
                        profileLocalSourceApi.setProfileInfo(
                            profile.year,
                            profile.facultyPath,
                            profile.facultyName,
                            profile.specialtyName,
                        )
                        analyticsApi.specialtyInfoSaved(profile.facultyName.orEmpty(), profile.specialtyName.orEmpty())
                    }

                    delay(500)
                    event.onComplete(profile)
                }
            }

            is SpecialtyInfoUiEvent.LoadSpecialties -> {
                screenModelScope.launch {
                    loadAllSpecialties()
                }

                _facultyButtonState.update { false }
            }

            is SpecialtyInfoUiEvent.ToggleDropdownState -> {
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
        }
    }

    private suspend fun loadAllFaculties() {
        val result = ershuApi.getAllFaculties()
        val allFaculties = result.value.allFaculties

        _uiState.update {
            it.copy(facultiesList = allFaculties)
        }

        BaseViewModel.setChildIsOffline(result.isFailure())

        _selectedState.update { state ->
            state.copy(
                selectedFaculty = allFaculties.find { it.folderName == profile?.facultyPath },
            )
        }

        if (profile?.facultyPath.isNullOrEmpty().not()) {
            onEvent(SpecialtyInfoUiEvent.LoadSpecialties)
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

        BaseViewModel.setChildIsOffline(result.isFailure())

        _selectedState.update { state ->
            state.copy(
                selectedYear = yearsList.find { it == profile?.year },
                selectedSpecialty = allSpecialties.allYears.flatten().find { it.specialtyName == profile?.specialtyName },
            )
        }
    }
}