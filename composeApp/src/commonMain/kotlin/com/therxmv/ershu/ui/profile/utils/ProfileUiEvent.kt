package com.therxmv.ershu.ui.profile.utils

import com.therxmv.ershu.data.models.SpecialtyModel

sealed class ProfileUiEvent {
    data class SelectYear(val year: String?) : ProfileUiEvent()
    data class SelectSpecialty(val specialty: SpecialtyModel) : ProfileUiEvent()
    data class Continue(val onComplete: () -> Unit) : ProfileUiEvent()
}