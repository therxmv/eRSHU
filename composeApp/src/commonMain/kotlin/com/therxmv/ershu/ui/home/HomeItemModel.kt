package com.therxmv.ershu.ui.home

import androidx.compose.ui.graphics.vector.ImageVector
import com.therxmv.ershu.ui.home.utils.HomeItems

data class HomeItemModel(
    val id: HomeItems,
    val title: String,
    val icon: ImageVector,
)