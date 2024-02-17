package com.therxmv.ershu.ui.profile.utils

import com.therxmv.ershu.data.models.AllCallsScheduleModel
import com.therxmv.ershu.data.models.FacultyModel
import com.therxmv.ershu.data.models.SpecialtyModel
import com.therxmv.ershu.ui.profile.utils.ProfileDropdown.FACULTY
import com.therxmv.ershu.ui.profile.utils.ProfileDropdown.SPECIALTY
import com.therxmv.ershu.ui.profile.utils.ProfileDropdown.YEAR

data class ProfileUiState(
    val facultiesList: List<FacultyModel> = emptyList(),
    val selectedFaculty: FacultyModel? = null,
    val yearsList: List<String> = emptyList(),
    val selectedYear: String? = null,
    val specialtiesList: List<List<SpecialtyModel>> = emptyList(),
    val selectedSpecialty: SpecialtyModel? = null,
    val callsSchedule: AllCallsScheduleModel? = null,
    val isOffline: Boolean = false,
    val isFacultyButtonEnabled: Boolean = true,
    val dropdownExpandedMap: Map<ProfileDropdown, Boolean> = mapOf(
        FACULTY to false,
        YEAR to false,
        SPECIALTY to false,
    )
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

enum class ProfileDropdown {
    FACULTY,
    YEAR,
    SPECIALTY,
}