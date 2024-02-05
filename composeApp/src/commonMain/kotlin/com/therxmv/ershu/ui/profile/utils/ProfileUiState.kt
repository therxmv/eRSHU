package com.therxmv.ershu.ui.profile.utils

import com.therxmv.ershu.data.models.SpecialtyModel

data class ProfileUiState(
    val yearsList: List<String> = emptyList(),
    val selectedYear: String? = null,
    val specialtiesList: List<List<SpecialtyModel>> = emptyList(),
    val selectedSpecialty: SpecialtyModel? = null,
    val isOffline: Boolean = false
) {
    fun getYears() = yearsList.filter { year ->
        year.toIntOrNull()?.let {
            specialtiesList[it - 1].firstOrNull()?.specialtyName.isNullOrEmpty().not()
        } ?: false
    }

    fun getSpecialties() = selectedYear?.toIntOrNull()?.let {
        specialtiesList[it - 1]
    }

    fun isFieldsFilled() = selectedSpecialty?.specialtyName.isNullOrEmpty().not() && selectedYear != null
}