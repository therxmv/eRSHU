package com.therxmv.ershu.ui.views.common

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import compose.icons.feathericons.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ERSHUAppBar(
    canPop: Boolean,
    title: String,
    onBackClick: () -> Unit,
    onBellClick: (() -> Unit)?,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        },
        navigationIcon = {
            if (canPop) {
                IconButton(
                    onClick = onBackClick,
                ) {
                    Icon(
                        imageVector = FeatherIcons.ArrowLeft,
                        contentDescription = "back"
                    )
                }
            }
        },
        actions = {
            if (onBellClick != null) {
                IconButton(
                    onClick = onBellClick,
                ) {
                    Icon(
                        imageVector = FeatherIcons.Clock,
                        contentDescription = "Clock",
                    )
                }
            }
        }
    )
}