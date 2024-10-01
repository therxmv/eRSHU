package com.therxmv.ershu.ui.base.views.inputs

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> RSHUDropDown(
    modifier: Modifier,
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    input: String,
    placeholder: String,
    onDismissRequest: () -> Unit,
    itemList: List<T>?,
    itemTitle: (T) -> String,
    onItemClick: (T) -> Unit,
    isEnabled: Boolean = true,
    prefix: String? = null,
) {
    val radius = (if (isExpanded) 0 else 8).dp
    val bottomRadius = animateDpAsState(targetValue = radius)

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = onExpandedChange,
    ) {
        TextField(
            modifier = modifier.menuAnchor(),
            value = input,
            prefix = {
                if (prefix != null && input.isNotEmpty()) {
                    Text(
                        modifier = Modifier.padding(end = 8.dp),
                        text = prefix,
                    )
                }
            },
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            placeholder = {
                Text(text = placeholder)
            },
            colors = RSHUInputsColors.defaultDropdownColors(),
            shape = RoundedCornerShape(
                topStart = 8.dp,
                topEnd = 8.dp,
                bottomEnd = bottomRadius.value,
                bottomStart = bottomRadius.value,
            ),
            enabled = isEnabled,
            textStyle = TextStyle(fontWeight = FontWeight.Bold),
        )

        DropdownMenu(
            modifier = Modifier.exposedDropdownSize(),
            expanded = isExpanded && isEnabled,
            onDismissRequest = onDismissRequest,
        ) {
            itemList?.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = {
                        Text(text = itemTitle(item))
                    },
                    onClick = {
                        onItemClick(item)
                    },
                )

                if (index < itemList.lastIndex) {
                    Divider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.surface,
                    )
                }
            }
        }
    }
}