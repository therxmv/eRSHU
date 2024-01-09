package com.therxmv.ershu.ui.profile.utils

import com.therxmv.ershu.data.models.SpecialtyModel

data class ProfileUiState(
    val yearsList: List<String> = emptyList(),
    val selectedYear: Int? = null,
    val specialtiesList: List<List<SpecialtyModel>> = emptyList(),
    val selectedSpecialty: SpecialtyModel? = null
) {
    fun getSpecialties() = specialtiesList[(selectedYear ?: 1) - 1]

    fun isFieldsFilled() = selectedSpecialty?.specialtyName.isNullOrEmpty().not() && selectedYear != null
}