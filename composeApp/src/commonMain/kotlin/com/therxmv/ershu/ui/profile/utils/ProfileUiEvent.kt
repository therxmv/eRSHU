package com.therxmv.ershu.ui.profile.utils

import com.therxmv.ershu.data.models.SpecialtyModel

sealed class ProfileUiEvent {
    data class SelectYear(val year: Int?): ProfileUiEvent()
    data class SelectSpecialty(val specialty: SpecialtyModel): ProfileUiEvent()
}