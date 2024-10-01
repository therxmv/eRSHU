package com.therxmv.ershu.ui.specialtyinfo.viewmodel.utils

import androidx.compose.runtime.Immutable
import com.therxmv.ershu.data.models.FacultyModel
import com.therxmv.ershu.data.models.SpecialtyModel

@Immutable
data class SpecialtyInfoUiState(
    val facultiesList: List<FacultyModel> = emptyList(),
    val yearsList: List<String> = emptyList(),
    val specialtiesList: List<List<SpecialtyModel>> = emptyList(),
) {
    fun getYears() = yearsList.filter { year ->
        year.toIntOrNull()?.let {
            specialtiesList[it - 1].firstOrNull()?.specialtyName.isNullOrEmpty().not()
        } ?: false
    }

    fun getSpecialties(selectedYear: String?) = selectedYear?.toIntOrNull()?.let {
        specialtiesList[it - 1]
    }

    fun isFieldsFilled(state: SelectedFieldsState) =
        state.selectedSpecialty?.specialtyName.isNullOrEmpty().not() && state.selectedYear != null
}

data class SelectedFieldsState(
    val selectedFaculty: FacultyModel? = null,
    val selectedYear: String? = null,
    val selectedSpecialty: SpecialtyModel? = null,
)

enum class SpecialtyInfoDropdown {
    FACULTY,
    YEAR,
    SPECIALTY,
}