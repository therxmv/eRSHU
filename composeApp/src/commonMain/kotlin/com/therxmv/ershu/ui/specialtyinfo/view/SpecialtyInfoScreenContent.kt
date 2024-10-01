package com.therxmv.ershu.ui.specialtyinfo.view

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.therxmv.ershu.Res
import com.therxmv.ershu.data.models.FacultyModel
import com.therxmv.ershu.data.models.SpecialtyModel
import com.therxmv.ershu.ui.base.views.inputs.RSHUDropDown
import com.therxmv.ershu.ui.base.views.inputs.RSHUInputsColors
import com.therxmv.ershu.ui.specialtyinfo.viewmodel.utils.SpecialtyInfoDropdown
import com.therxmv.ershu.ui.specialtyinfo.viewmodel.utils.SpecialtyInfoDropdown.FACULTY
import com.therxmv.ershu.ui.specialtyinfo.viewmodel.utils.SpecialtyInfoDropdown.SPECIALTY
import com.therxmv.ershu.ui.specialtyinfo.viewmodel.utils.SpecialtyInfoDropdown.YEAR
import com.therxmv.ershu.ui.specialtyinfo.viewmodel.utils.SpecialtyInfoUiEvent

@Composable
fun SpecialtyInfoScreenContent(
    modifier: Modifier = Modifier,
    facultyState: DropDownState<FacultyModel>,
    yearState: DropDownState<String>,
    specialtyState: DropDownState<SpecialtyModel>,
    isFacultyButtonEnabled: Boolean,
    onFacultyButtonClick: () -> Unit,
    isContinueEnabled: Boolean,
    onContinueClick: () -> Unit,
    onExpand: (SpecialtyInfoDropdown, Boolean) -> Unit,
    onDismiss: (SpecialtyInfoDropdown) -> Unit,
    onItemClick: (SpecialtyInfoUiEvent, SpecialtyInfoDropdown) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        RSHUDropDown(
            modifier = Modifier
                .fillMaxWidth(),
            isExpanded = facultyState.isExpanded,
            input = facultyState.input,
            placeholder = Res.string.specialtyinfo_choose_faculty,
            onExpandedChange = {
                onExpand(FACULTY, it)
            },
            onDismissRequest = {
                onDismiss(FACULTY)
            },
            itemList = facultyState.list,
            itemTitle = { it.facultyName },
            onItemClick = {
                onItemClick(
                    SpecialtyInfoUiEvent.SelectFaculty(it),
                    FACULTY,
                )
            },
        )

        SubmitFaculty(
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 12.dp),
            isEnabled = isFacultyButtonEnabled,
            onClick = onFacultyButtonClick,
        )

        RSHUDropDown(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            isExpanded = yearState.isExpanded,
            input = yearState.input,
            placeholder = Res.string.specialtyinfo_choose_year,
            onExpandedChange = {
                onExpand(YEAR, it)
            },
            onDismissRequest = {
                onDismiss(YEAR)
            },
            isEnabled = yearState.list.isNullOrEmpty().not(),
            itemList = yearState.list,
            itemTitle = { it },
            onItemClick = {
                onItemClick(
                    SpecialtyInfoUiEvent.SelectYear(it),
                    YEAR,
                )
            },
            prefix = Res.string.specialtyinfo_year_prefix,
        )
        RSHUDropDown(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            isExpanded = specialtyState.isExpanded,
            input = specialtyState.input,
            placeholder = Res.string.specialtyinfo_choose_specialty,
            onExpandedChange = {
                onExpand(SPECIALTY, it)
            },
            onDismissRequest = {
                onDismiss(SPECIALTY)
            },
            isEnabled = yearState.input.isNotEmpty(),
            itemList = specialtyState.list,
            itemTitle = { it.specialtyName },
            onItemClick = {
                onItemClick(
                    SpecialtyInfoUiEvent.SelectSpecialty(it),
                    SPECIALTY,
                )
            },
            prefix = Res.string.specialtyinfo_specialty_prefix,
        )

        Spacer(modifier = Modifier.weight(1F))

//        Text(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 12.dp),
//            text = Res.string.specialtyinfo_continue_label,
//            style = MaterialTheme.typography.labelLarge,
//            textAlign = TextAlign.Start,
//        )

        ContinueButton(
            isEnabled = isContinueEnabled,
            onClick = onContinueClick,
        )
    }
}

@Composable
private fun SubmitFaculty(
    modifier: Modifier = Modifier,
    isEnabled: Boolean,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        enabled = isEnabled,
        onClick = {
            onClick()
        },
        colors = RSHUInputsColors.defaultButtonColors(),
    ) {
        Text(text = Res.string.specialtyinfo_select)
    }
}

@Composable
private fun ContinueButton(
    isEnabled: Boolean,
    onClick: () -> Unit,
) {
    var isButtonPressed by remember { mutableStateOf(false) }

    val fraction = if (isButtonPressed) 0.15f else 1f
    val buttonWidth by animateFloatAsState(fraction, tween(300))

    Crossfade(
        targetState = isButtonPressed,
        animationSpec = tween(400)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            if (it) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = MaterialTheme.colorScheme.tertiary,
                )
            } else {
                Button(
                    modifier = Modifier.fillMaxWidth(buttonWidth),
                    enabled = isEnabled,
                    onClick = {
                        isButtonPressed = true
                        onClick()
                    },
                    colors = RSHUInputsColors.defaultButtonColors(),
                ) {
                    if (isButtonPressed.not()) {
                        Text(text = Res.string.specialtyinfo_save)
                    }
                }
            }
        }
    }
}

@Stable
data class DropDownState<T>(
    val isExpanded: Boolean,
    val input: String,
    val list: List<T>?,
)