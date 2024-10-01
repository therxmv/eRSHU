package com.therxmv.ershu.ui.base.views.inputs

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object RSHUInputsColors {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun defaultDropdownColors() =
        ExposedDropdownMenuDefaults.textFieldColors(
            unfocusedContainerColor = MaterialTheme.colorScheme.primary,
            focusedContainerColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,

            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,

            focusedTrailingIconColor = MaterialTheme.colorScheme.onPrimary,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.onPrimary,

            unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
            focusedTextColor = MaterialTheme.colorScheme.onPrimary,

            unfocusedPrefixColor = MaterialTheme.colorScheme.onPrimary,
            focusedPrefixColor = MaterialTheme.colorScheme.onPrimary,

            unfocusedPlaceholderColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedPlaceholderColor = MaterialTheme.colorScheme.surfaceVariant,
        )

    @Composable
    fun defaultOutlinedTextFieldColors() =
        OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surface,

            unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
        )

    @Composable
    fun defaultButtonColors() =
        ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary,
        )
}