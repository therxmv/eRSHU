package com.therxmv.ershu.ui.rating.viewmodel.utils

import com.therxmv.ershu.data.models.RatingModel

sealed class RatingUiState {
    data object Loading : RatingUiState()
    data class Ready(val data: RatingModel) : RatingUiState()
    data object NotAvailable : RatingUiState()
}