package com.therxmv.ershu.ui.views.calls

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.therxmv.ershu.Res
import com.therxmv.ershu.data.models.AllCallsScheduleModel
import com.therxmv.ershu.ui.base.ProgressIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CallsDialog(
    callsModel: AllCallsScheduleModel?,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
    ) {
        Surface(
            shape = MaterialTheme.shapes.large
        ) {
            CallsDialogContent(
                callsModel = callsModel,
                onDismiss = onDismiss,
            )
        }
    }
}

@Composable
private fun CallsDialogContent(
    callsModel: AllCallsScheduleModel?,
    onDismiss: () -> Unit,
) {
    Column(modifier = Modifier.padding(24.dp)) {
        Text(
            text = Res.string.calls_schedule_title,
            style = MaterialTheme.typography.headlineSmall,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Crossfade(targetState = callsModel) {
            if (it != null) {
                LazyColumn {
                    item {
                        Text(
                            modifier = Modifier.padding(bottom = 10.dp),
                            text = Res.string.calls_schedule_first_shift,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }

                    itemsIndexed(it.first.time) { index, item ->
                        Text("${index + 1}) $item")
                    }

                    item {
                        Text(
                            modifier = Modifier.padding(top = 18.dp, bottom = 10.dp),
                            text = Res.string.calls_schedule_second_shift,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }

                    itemsIndexed(it.second.time) { index, item ->
                        Text("${index + 1}) $item")
                    }
                }
            } else {
                ProgressIndicator(
                    modifier = Modifier,
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        DialogActions(callsModel, onDismiss)
    }
}

@Composable
private fun ColumnScope.DialogActions(
    callsModel: AllCallsScheduleModel?,
    onDismiss: () -> Unit,
) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    Row(
        modifier = Modifier
            .align(Alignment.End),
    ) {
        TextButton(
            onClick = onDismiss,
        ) {
            Text(Res.string.calls_schedule_close)
        }

        Spacer(modifier = Modifier.width(8.dp))

        TextButton(
            onClick = {
                callsModel?.let {
                    clipboardManager.setText(
                        AnnotatedString(
                            (it.first.time + it.second.time).toNumberedString()
                        )
                    )
                }
            },
        ) {
            Text(Res.string.calls_schedule_copy)
        }
    }
}

private fun List<String>.toNumberedString() = this.distinct().mapIndexed { index, s ->
    "${index + 1}) $s"
}.joinToString(separator = "\n")