package com.therxmv.ershu.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalFocusManager
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.therxmv.ershu.di.getScreenModel
import com.therxmv.ershu.ui.profile.utils.ProfileDropdown
import com.therxmv.ershu.ui.profile.utils.ProfileDropdown.FACULTY
import com.therxmv.ershu.ui.profile.utils.ProfileDropdown.SPECIALTY
import com.therxmv.ershu.ui.profile.utils.ProfileDropdown.YEAR
import com.therxmv.ershu.ui.profile.utils.ProfileUiEvent
import com.therxmv.ershu.ui.profile.views.DropDownState
import com.therxmv.ershu.ui.profile.views.ProfileScreenContent
import com.therxmv.ershu.ui.schedule.ScheduleScreen
import com.therxmv.ershu.ui.views.OfflineBanner
import com.therxmv.ershu.ui.views.calls.CallsDialog
import com.therxmv.ershu.ui.views.calls.CallsScreen
import com.therxmv.ershu.ui.views.common.ProgressIndicator

class ProfileScreen : Screen, CallsScreen() {

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<ProfileViewModel>()

        val uiState by viewModel.uiState.collectAsState()
        val selectedState by viewModel.selectedState.collectAsState()
        val expandedState by viewModel.expandedMap.collectAsState()
        val callsDialogState by isDialogOpen.collectAsState()
        val facultyButtonState by viewModel.facultyButtonState.collectAsState()
        val callsScheduleState by viewModel.callsScheduleState.collectAsState()
        val isOffline by viewModel.isOffline.collectAsState()

        val isFacultyExpanded = expandedState[FACULTY] == true
        val isYearExpanded = expandedState[YEAR] == true
        val isSpecialtyExpanded = expandedState[SPECIALTY] == true

        val navigator = LocalNavigator.currentOrThrow
        val focusManager = LocalFocusManager.current

        if (callsDialogState) {
            CallsDialog(callsScheduleState) {
                toggleDialog()
            }
        }

        Column {
            if (isOffline) {
                OfflineBanner()
            }

            if (uiState.facultiesList.isNotEmpty()) {
                ProfileScreenContent(
                    facultyState = DropDownState(
                        isExpanded = isFacultyExpanded,
                        input = selectedState.selectedFaculty?.facultyName.orEmpty(),
                        list = uiState.facultiesList,
                    ),
                    yearState = DropDownState(
                        isExpanded = isYearExpanded,
                        input = selectedState.selectedYear.orEmpty(),
                        list = uiState.getYears(),
                    ),
                    specialtyState = DropDownState(
                        isExpanded = isSpecialtyExpanded,
                        input = selectedState.selectedSpecialty?.specialtyName.orEmpty(),
                        list = uiState.getSpecialties(selectedState.selectedYear),
                    ),
                    isFacultyButtonEnabled = facultyButtonState,
                    onFacultyButtonClick = {
                        viewModel.onEvent(ProfileUiEvent.LoadSpecialties)
                    },
                    isContinueEnabled = uiState.isFieldsFilled(selectedState),
                    onContinueClick = {
                        viewModel.onEvent(
                            ProfileUiEvent.Continue {
                                navigator.push(
                                    ScheduleScreen.createScreen(
                                        selectedState.selectedFaculty?.folderName,
                                        selectedState.selectedYear,
                                        selectedState.selectedSpecialty?.specialtyName,
                                    )
                                )
                            }
                        )
                    },
                    onExpand = { dropdown, value ->
                        viewModel.toggleDropdown(dropdown, value)
                    },
                    onDismiss = {
                        viewModel.toggleDropdown(it)
                        focusManager.clearFocus()
                    },
                    onItemClick = { event, dropdown ->
                        viewModel.onEvent(event)
                        viewModel.toggleDropdown(dropdown)
                        focusManager.clearFocus()
                    }
                )
            } else {
                ProgressIndicator()
            }
        }
    }
}

private fun ProfileViewModel.toggleDropdown(dropdown: ProfileDropdown, value: Boolean = false) {
    onEvent(ProfileUiEvent.ToggleDropdownState(dropdown, value))
}