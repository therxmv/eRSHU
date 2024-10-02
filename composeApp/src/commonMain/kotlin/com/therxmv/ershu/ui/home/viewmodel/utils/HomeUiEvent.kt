package com.therxmv.ershu.ui.home.viewmodel.utils

import cafe.adriel.voyager.navigator.Navigator

sealed class HomeUiEvent {
    data class ItemClick(val navigator: Navigator, val item: HomeItems) : HomeUiEvent()
}

enum class HomeItems {
    SCHEDULE,
    PROFILE,
    EXAMS,
    RATING,
}