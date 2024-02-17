package com.therxmv.ershu.ui.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.therxmv.ershu.Res
import com.therxmv.ershu.di.getScreenModel
import com.therxmv.ershu.ui.profile.utils.ProfileDropdown
import com.therxmv.ershu.ui.profile.utils.ProfileDropdown.FACULTY
import com.therxmv.ershu.ui.profile.utils.ProfileDropdown.SPECIALTY
import com.therxmv.ershu.ui.profile.utils.ProfileDropdown.YEAR
import com.therxmv.ershu.ui.profile.utils.ProfileUiEvent
import com.therxmv.ershu.ui.profile.views.DropDown
import com.therxmv.ershu.ui.profile.views.InputTitle
import com.therxmv.ershu.ui.schedule.ScheduleScreen
import com.therxmv.ershu.ui.views.OfflineBanner
import com.therxmv.ershu.ui.views.ProgressIndicator
import com.therxmv.ershu.ui.views.calls.CallsDialog
import com.therxmv.ershu.ui.views.calls.CallsScreen

class ProfileScreen : Screen, CallsScreen() {

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<ProfileViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        val callsDialogState = isDialogOpen.collectAsState().value

        val isFacultyExpanded = uiState.dropdownExpandedMap[FACULTY] == true
        val isYearExpanded = uiState.dropdownExpandedMap[YEAR] == true
        val isSpecialtyExpanded = uiState.dropdownExpandedMap[SPECIALTY] == true

        val navigator = LocalNavigator.currentOrThrow
        val focusManager = LocalFocusManager.current

        if (callsDialogState) {
            CallsDialog(
                uiState.callsSchedule,
            ) {
                toggleDialog()
            }
        }

        Column {
            if (uiState.isOffline) {
                OfflineBanner()
            }

            if (uiState.facultiesList.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 24.dp, end = 24.dp, bottom = 12.dp)
                ) {
                    InputTitle(Res.string.profile_specialty)

                    DropDown(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        isExpanded = isFacultyExpanded,
                        input = uiState.selectedFaculty?.facultyName.orEmpty(),
                        placeholder = Res.string.profile_choose_faculty,
                        onExpandedChange = {
                            viewModel.toggleDropdown(FACULTY, it)
                        },
                        onDismissRequest = {
                            viewModel.toggleDropdown(FACULTY)
                            focusManager.clearFocus()
                        },
                    ) {
                        uiState.facultiesList.forEach {
                            DropdownMenuItem(
                                text = {
                                    Text(text = it.facultyName)
                                },
                                onClick = {
                                    viewModel.onEvent(
                                        ProfileUiEvent.SelectFaculty(it)
                                    )
                                    viewModel.toggleDropdown(FACULTY)
                                }
                            )
                        }
                    }

                    Button(
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(top = 12.dp),
                        enabled = uiState.isFacultyButtonEnabled,
                        onClick = {
                            viewModel.onEvent(ProfileUiEvent.LoadSpecialties)
                        },
                    ) {
                        Text(text = Res.string.profile_select)
                    }

                    DropDown(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        isExpanded = isYearExpanded,
                        input = uiState.selectedYear.orEmpty(),
                        placeholder = Res.string.profile_choose_year,
                        onExpandedChange = {
                            viewModel.toggleDropdown(YEAR, it)
                        },
                        onDismissRequest = {
                            viewModel.toggleDropdown(YEAR)
                            focusManager.clearFocus()
                        },
                        isEnabled = uiState.yearsList.isNotEmpty(),
                    ) {
                        uiState.getYears().forEach {
                            DropdownMenuItem(
                                text = {
                                    Text(text = it)
                                },
                                onClick = {
                                    viewModel.onEvent(
                                        ProfileUiEvent.SelectYear(it)
                                    )
                                    viewModel.toggleDropdown(YEAR)
                                }
                            )
                        }
                    }
                    DropDown(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        isExpanded = isSpecialtyExpanded,
                        input = uiState.selectedSpecialty?.specialtyName.orEmpty(),
                        placeholder = Res.string.profile_choose_specialty,
                        onExpandedChange = {
                            viewModel.toggleDropdown(SPECIALTY, it)
                        },
                        onDismissRequest = {
                            viewModel.toggleDropdown(SPECIALTY)
                            focusManager.clearFocus()
                        },
                        isEnabled = uiState.selectedYear != null
                    ) {
                        uiState.getSpecialties()?.forEach { item ->
                            val text = item.specialtyName

                            DropdownMenuItem(
                                text = {
                                    Text(text = text)
                                },
                                onClick = {
                                    viewModel.onEvent(
                                        ProfileUiEvent.SelectSpecialty(item)
                                    )
                                    viewModel.toggleDropdown(SPECIALTY)
                                }
                            )
                        }
                    }

                    Box(modifier = Modifier.fillMaxWidth().weight(1F))

                    Text(
                        modifier = Modifier.padding(vertical = 12.dp),
                        text = Res.string.profile_continue_label,
                        style = MaterialTheme.typography.labelLarge,
                    )

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = uiState.isFieldsFilled(),
                        onClick = {
                            viewModel.onEvent(
                                ProfileUiEvent.Continue {
                                    navigator.push(
                                        ScheduleScreen.createScreen(
                                            uiState.selectedFaculty?.folderName,
                                            uiState.selectedYear,
                                            uiState.selectedSpecialty?.specialtyName,
                                        )
                                    )
                                }
                            )
                        },
                    ) {
                        Text(text = Res.string.profile_continue)
                    }
                }
            } else {
                ProgressIndicator()
            }
        }
    }
}

private fun ProfileViewModel.toggleDropdown(dropdown: ProfileDropdown, value: Boolean = false) {
    onEvent(ProfileUiEvent.ToggleDropdownState(dropdown, value))
}