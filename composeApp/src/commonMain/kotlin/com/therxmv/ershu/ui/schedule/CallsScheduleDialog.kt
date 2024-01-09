package com.therxmv.ershu.ui.schedule

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CallsScheduleDialog(
    callsModel: AllCallsScheduleModel?,
    onDismiss: () -> Unit,
) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    AlertDialog(
        onDismissRequest = onDismiss,
    ) {
        Surface(
            shape = MaterialTheme.shapes.large
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    modifier = Modifier.padding(bottom = 16.dp),
                    text = Res.string.calls_schedule_title,
                    style = MaterialTheme.typography.headlineSmall,
                )

                callsModel?.let {
                    LazyColumn {
                        item {
                            Text(
                                modifier = Modifier.padding(bottom = 10.dp),
                                text = Res.string.calls_schedule_first_shift,
                                style = MaterialTheme.typography.titleMedium,
                            )
                        }

                        itemsIndexed(callsModel.first.time) { index, item ->
                            Text("${index + 1}) $item")
                        }

                        item {
                            Text(
                                modifier = Modifier.padding(top = 18.dp, bottom = 10.dp),
                                text = Res.string.calls_schedule_second_shift,
                                style = MaterialTheme.typography.titleMedium,
                            )
                        }

                        itemsIndexed(callsModel.second.time) { index, item ->
                            Text("${index + 1}) $item")
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .align(Alignment.End),
                ) {
                    TextButton(
                        modifier = Modifier.padding(end = 8.dp),
                        onClick = onDismiss,
                    ) {
                        Text(Res.string.calls_schedule_close)
                    }

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
        }
    }
}

private fun List<String>.toNumberedString() = this.distinct().mapIndexed { index, s ->
    "${index + 1}) $s"
}.joinToString(separator = "\n")