package com.therxmv.ershu.ui.profile.viewmodel.utils

sealed class ProfileUiState {
    data object Loading : ProfileUiState()
    data class Ready(val faculty: String, val specialty: String) : ProfileUiState()
}