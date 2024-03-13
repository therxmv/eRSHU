package com.therxmv.ershu.ui.profile.views

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun InputTitle(
    text: String,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun Input(
    modifier: Modifier,
    input: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    onDone: (KeyboardActionScope.() -> Unit),
) {
    OutlinedTextField(
        modifier = modifier,
        value = input,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
            )
        },
        shape = RoundedCornerShape(8.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = onDone
        ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DropDown(
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
) {
    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = onExpandedChange,
    ) {
        TextField(
            modifier = modifier.menuAnchor(),
            value = input,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            placeholder = {
                Text(text = placeholder)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            shape = RoundedCornerShape(
                topStart = 8.dp,
                topEnd = 8.dp,
                bottomEnd = 0.dp,
                bottomStart = 0.dp
            ),
            enabled = isEnabled,
        )

        DropdownMenu(
            modifier = Modifier.exposedDropdownSize(),
            expanded = isExpanded && isEnabled,
            onDismissRequest = onDismissRequest,
        ) {
            itemList?.forEach {
                DropdownMenuItem(
                    text = {
                        Text(text = itemTitle(it))
                    },
                    onClick = {
                        onItemClick(it)
                    }
                )
            }
        }
    }
}