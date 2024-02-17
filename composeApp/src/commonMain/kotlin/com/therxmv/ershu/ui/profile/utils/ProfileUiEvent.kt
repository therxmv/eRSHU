package com.therxmv.ershu.ui.profile.utils

import com.therxmv.ershu.data.models.FacultyModel
import com.therxmv.ershu.data.models.SpecialtyModel

sealed class ProfileUiEvent {
    data class SelectFaculty(val faculty: FacultyModel?) : ProfileUiEvent()
    data class SelectYear(val year: String?) : ProfileUiEvent()
    data class SelectSpecialty(val specialty: SpecialtyModel) : ProfileUiEvent()
    data class Continue(val onComplete: () -> Unit) : ProfileUiEvent()
    data object LoadSpecialties : ProfileUiEvent()
    data class ToggleDropdownState(val dropdown: ProfileDropdown, val value: Boolean = false) : ProfileUiEvent()
}