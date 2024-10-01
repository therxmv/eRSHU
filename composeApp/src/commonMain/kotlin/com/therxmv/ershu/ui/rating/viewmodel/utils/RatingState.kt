package com.therxmv.ershu.ui.rating.viewmodel.utils

sealed class RatingState {
    data object Initial : RatingState()
    data class Success(val rating: Float) : RatingState()
    data object Error : RatingState()
}