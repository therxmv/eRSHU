package com.therxmv.ershu.ui.profile

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
        }
    }

    private fun loadAllSpecialties() {
        viewModelScope.launch {
            val allSpecialties = ershuApi.getAllSpecialties()

            _uiState.update {
                it.copy(
                    yearsList = List(allSpecialties.allYears.size) { index ->
                        "${index + 1}"
                    },
                    specialtiesList = allSpecialties.allYears
                )
            }
        }
    }
}