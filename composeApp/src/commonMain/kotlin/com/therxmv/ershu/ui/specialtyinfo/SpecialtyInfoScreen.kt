package com.therxmv.ershu.ui.specialtyinfo

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalFocusManager
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.therxmv.ershu.Res
import com.therxmv.ershu.data.models.ProfileUiData
import com.therxmv.ershu.data.models.toProfile
import com.therxmv.ershu.data.models.toUiData
import com.therxmv.ershu.di.getScreenModel
import com.therxmv.ershu.ui.specialtyinfo.utils.SpecialtyInfoDropdown
import com.therxmv.ershu.ui.specialtyinfo.utils.SpecialtyInfoDropdown.FACULTY
import com.therxmv.ershu.ui.specialtyinfo.utils.SpecialtyInfoDropdown.SPECIALTY
import com.therxmv.ershu.ui.specialtyinfo.utils.SpecialtyInfoDropdown.YEAR
import com.therxmv.ershu.ui.specialtyinfo.utils.SpecialtyInfoUiEvent
import com.therxmv.ershu.ui.specialtyinfo.views.DropDownState
import com.therxmv.ershu.ui.specialtyinfo.views.SpecialtyInfoScreenContent
import com.therxmv.ershu.ui.views.ScreenTitleProvider
import com.therxmv.ershu.ui.base.BaseScreen
import com.therxmv.ershu.ui.base.ProgressIndicator
import com.therxmv.ershu.ui.home.HomeScreen
import com.therxmv.ershu.ui.schedule.ScheduleScreen

class SpecialtyInfoScreen(
    private val isClearStack: Boolean = false,
    private val tempProfile: ProfileUiData? = null,
) : BaseScreen(), ScreenTitleProvider {

    override val key = "SpecialtyInfoScreen"

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<SpecialtyInfoViewModel>()

        val uiState by viewModel.uiState.collectAsState()
        val selectedState by viewModel.selectedState.collectAsState()
        val expandedState by viewModel.expandedMap.collectAsState()
        val facultyButtonState by viewModel.facultyButtonState.collectAsState()

        val isFacultyExpanded = expandedState[FACULTY] == true
        val isYearExpanded = expandedState[YEAR] == true
        val isSpecialtyExpanded = expandedState[SPECIALTY] == true

        LaunchedEffect(Unit) {
            viewModel.setProfileData(tempProfile?.toProfile())
        }

        super.BaseContent { modifier ->
            val navigator = LocalNavigator.currentOrThrow
            val focusManager = LocalFocusManager.current

            Crossfade(
                targetState = uiState.facultiesList.isNotEmpty(),
                animationSpec = tween(500),
            ) { state ->
                when {
                    state -> SpecialtyInfoScreenContent(
                        modifier = modifier,
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
                            viewModel.onEvent(SpecialtyInfoUiEvent.LoadSpecialties)
                        },
                        isContinueEnabled = uiState.isFieldsFilled(selectedState),
                        onContinueClick = {
                            viewModel.onEvent(
                                SpecialtyInfoUiEvent.Save(tempProfile?.toProfile()) { profile ->
                                    when {
                                        isClearStack -> navigator.replace(HomeScreen())
                                        tempProfile != null -> {
                                            navigator.pop()
                                            navigator.replace(ScheduleScreen(profile?.toUiData()))
                                        }
                                        else -> navigator.pop()
                                    }
                                }
                            )
                        },
                        onExpand = { dropdown, value ->
                            viewModel.toggleDropdown(dropdown, value)
                        },
                        onDismiss = { dropdown ->
                            viewModel.toggleDropdown(dropdown)
                            focusManager.clearFocus()
                        },
                        onItemClick = { event, dropdown ->
                            viewModel.onEvent(event)
                            viewModel.toggleDropdown(dropdown)
                            focusManager.clearFocus()
                        }
                    )

                    else -> ProgressIndicator()
                }
            }
        }
    }

    override fun getTitle() = when {
        isClearStack -> Res.string.specialtyinfo_title
        tempProfile != null -> Res.string.specialtyinfo_change
        else -> Res.string.specialtyinfo_edit
    }
}

private fun SpecialtyInfoViewModel.toggleDropdown(dropdown: SpecialtyInfoDropdown, value: Boolean = false) {
    onEvent(SpecialtyInfoUiEvent.ToggleDropdownState(dropdown, value))
}