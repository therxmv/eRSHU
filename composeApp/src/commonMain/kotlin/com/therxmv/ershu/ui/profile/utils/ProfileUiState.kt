package com.therxmv.ershu.ui.profile.utils

sealed class ProfileUiState {
    data object Loading : ProfileUiState()
    data class Ready(val faculty: String, val specialty: String) : ProfileUiState()
}