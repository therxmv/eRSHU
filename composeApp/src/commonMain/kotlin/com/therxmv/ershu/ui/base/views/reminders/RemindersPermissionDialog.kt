package com.therxmv.ershu.ui.base.views.reminders

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.therxmv.ershu.Res
import com.therxmv.ershu.ui.base.views.dialog.RshuDialog
import compose.icons.FeatherIcons
import compose.icons.feathericons.Bell

@Composable
fun RemindersPermissionDialog(
    isVisible: Boolean,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
) {
    RshuDialog(
        isVisible = isVisible,
        onDismiss = onDismiss,
    ) {
        RemindersPermissionContent(
            onClick = onClick,
            onDismiss = onDismiss,
        )
    }
}

@Composable
private fun RemindersPermissionContent(
    onClick: () -> Unit,
    onDismiss: () -> Unit,
) {
    Column(modifier = Modifier.padding(24.dp)) {
        Icon(
            modifier = Modifier
                .size(36.dp)
                .align(Alignment.CenterHorizontally),
            imageVector = FeatherIcons.Bell,
            contentDescription = "reminder",
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = Res.string.reminders_dialog_title,
            style = MaterialTheme.typography.headlineSmall,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = Res.string.reminders_dialog_description)

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.align(Alignment.End),
        ) {
            TextButton(
                onClick = onDismiss,
            ) {
                Text(Res.string.reminders_dialog_deny)
            }

            Spacer(modifier = Modifier.width(8.dp))

            TextButton(
                onClick = {
                    onClick()
                    onDismiss()
                },
            ) {
                Text(Res.string.reminders_dialog_grant)
            }
        }
    }
}