package com.therxmv.ershu.ui.specialtyinfo.utils

import com.therxmv.ershu.data.models.FacultyModel
import com.therxmv.ershu.data.models.SpecialtyModel
import com.therxmv.ershu.db.Profile

sealed class SpecialtyInfoUiEvent {
    data class SelectFaculty(val faculty: FacultyModel?) : SpecialtyInfoUiEvent()
    data class SelectYear(val year: String?) : SpecialtyInfoUiEvent()
    data class SelectSpecialty(val specialty: SpecialtyModel) : SpecialtyInfoUiEvent()
    data class Save(val profile: Profile?, val onComplete: (Profile?) -> Unit) : SpecialtyInfoUiEvent()
    data object LoadSpecialties : SpecialtyInfoUiEvent()
    data class ToggleDropdownState(val dropdown: SpecialtyInfoDropdown, val value: Boolean = false) : SpecialtyInfoUiEvent()
}