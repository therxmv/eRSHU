package com.therxmv.ershu.ui.home.viewmodel.utils

import androidx.compose.runtime.Immutable
import com.therxmv.ershu.ui.home.view.HomeItemModel

sealed class HomeUiState {
    data object EmptyProfile : HomeUiState()
    data object Loading : HomeUiState()

    @Immutable
    data class Ready(val items: List<HomeItemModel>) : HomeUiState()
}