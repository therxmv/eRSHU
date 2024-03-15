package com.therxmv.ershu.ui.profile.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.therxmv.ershu.Res
import com.therxmv.ershu.data.models.FacultyModel
import com.therxmv.ershu.data.models.SpecialtyModel
import com.therxmv.ershu.ui.profile.utils.ProfileDropdown
import com.therxmv.ershu.ui.profile.utils.ProfileDropdown.FACULTY
import com.therxmv.ershu.ui.profile.utils.ProfileDropdown.SPECIALTY
import com.therxmv.ershu.ui.profile.utils.ProfileDropdown.YEAR
import com.therxmv.ershu.ui.profile.utils.ProfileUiEvent

@Composable
fun ProfileScreenContent(
    facultyState: DropDownState<FacultyModel>,
    yearState: DropDownState<String>,
    specialtyState: DropDownState<SpecialtyModel>,
    isFacultyButtonEnabled: Boolean,
    onFacultyButtonClick: () -> Unit,
    isContinueEnabled: Boolean,
    onContinueClick: () -> Unit,
    onExpand: (ProfileDropdown, Boolean) -> Unit,
    onDismiss: (ProfileDropdown) -> Unit,
    onItemClick: (ProfileUiEvent, ProfileDropdown) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 24.dp, end = 24.dp, bottom = 12.dp)
    ) {
        Text(
            text = Res.string.profile_specialty,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )

        RSHUDropDown(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            isExpanded = facultyState.isExpanded,
            input = facultyState.input,
            placeholder = Res.string.profile_choose_faculty,
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
                    ProfileUiEvent.SelectFaculty(it),
                    FACULTY,
                )
            },
        )

        Button(
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 12.dp),
            enabled = isFacultyButtonEnabled,
            onClick = {
                onFacultyButtonClick()
            },
            colors = RSHUColors.defaultButtonColors(),
        ) {
            Text(text = Res.string.profile_select)
        }

        RSHUDropDown(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            isExpanded = yearState.isExpanded,
            input = yearState.input,
            placeholder = Res.string.profile_choose_year,
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
                    ProfileUiEvent.SelectYear(it),
                    YEAR,
                )
            }
        )
        RSHUDropDown(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            isExpanded = specialtyState.isExpanded,
            input = specialtyState.input,
            placeholder = Res.string.profile_choose_specialty,
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
                    ProfileUiEvent.SelectSpecialty(it),
                    SPECIALTY,
                )
            }
        )

        Box(modifier = Modifier.fillMaxWidth().weight(1F))

        Text(
            modifier = Modifier.padding(vertical = 12.dp),
            text = Res.string.profile_continue_label,
            style = MaterialTheme.typography.labelLarge,
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = isContinueEnabled,
            onClick = {
                onContinueClick()
            },
            colors = RSHUColors.defaultButtonColors(),
        ) {
            Text(text = Res.string.profile_continue)
        }
    }
}

@Stable
data class DropDownState<T>(
    val isExpanded: Boolean,
    val input: String,
    val list: List<T>?,
)