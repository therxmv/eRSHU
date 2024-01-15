package com.therxmv.ershu.ui.profile.utils

import com.therxmv.ershu.data.models.SpecialtyModel

data class ProfileUiState(
    val yearsList: List<String> = emptyList(),
    val selectedYear: String? = null,
    val specialtiesList: List<List<SpecialtyModel>> = emptyList(),
    val selectedSpecialty: SpecialtyModel? = null,
) {
    fun getSpecialties() = selectedYear?.toIntOrNull()?.let {
        specialtiesList[it - 1]
    }

    fun isFieldsFilled() = selectedSpecialty?.specialtyName.isNullOrEmpty().not() && selectedYear != null
}