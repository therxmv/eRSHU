package com.therxmv.ershu.utils

import com.therxmv.ershu.ui.home.HomeItemModel
import compose.icons.FeatherIcons
import compose.icons.feathericons.Calendar
import compose.icons.feathericons.Info
import compose.icons.feathericons.Settings
import compose.icons.feathericons.User

object MockData {
    val homeItems = listOf(
        HomeItemModel(
            "Schedule",
            FeatherIcons.Calendar,
            doNothing(),
        ),
        HomeItemModel(
            "Profile",
            FeatherIcons.User,
            doNothing(),
        ),
        HomeItemModel(
            "Help & Info",
            FeatherIcons.Info,
            doNothing(),
        ),
        HomeItemModel(
            "Settings",
            FeatherIcons.Settings,
            doNothing(),
        ),
    )
}