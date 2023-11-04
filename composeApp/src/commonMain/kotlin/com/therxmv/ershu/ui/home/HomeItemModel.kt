package com.therxmv.ershu.ui.home

import androidx.compose.ui.graphics.vector.ImageVector

data class HomeItemModel(
    val title: String,
    val icon: ImageVector,
    val onClickAction: () -> Unit,
)