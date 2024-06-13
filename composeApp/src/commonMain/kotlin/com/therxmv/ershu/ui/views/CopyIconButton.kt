package com.therxmv.ershu.ui.views

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Copy

@Composable
fun CopyIconButton(text: String) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    IconButton(
        onClick = {
            clipboardManager.setText(AnnotatedString(text))
        },
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = FeatherIcons.Copy,
            contentDescription = "Copy",
        )
    }
}