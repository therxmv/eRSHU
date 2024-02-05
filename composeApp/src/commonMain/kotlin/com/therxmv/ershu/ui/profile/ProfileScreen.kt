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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.therxmv.ershu.Res
import com.therxmv.ershu.di.getScreenModel
import com.therxmv.ershu.ui.profile.utils.ProfileUiEvent
import com.therxmv.ershu.ui.profile.views.DropDown
import com.therxmv.ershu.ui.profile.views.InputTitle
import com.therxmv.ershu.ui.schedule.ScheduleScreen
import com.therxmv.ershu.ui.views.OfflineBanner

class ProfileScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<ProfileViewModel>()
        val uiState by viewModel.uiState.collectAsState()

        val navigator = LocalNavigator.currentOrThrow
        val focusManager = LocalFocusManager.current

        var isSpecialtyExpanded by remember {
            mutableStateOf(false)
        }

        var isYearExpanded by remember {
            mutableStateOf(false)
        }

        Column {
            if (uiState.isOffline) {
                OfflineBanner()
            }

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
                    isExpanded = isYearExpanded,
                    input = uiState.selectedYear ?: "",
                    placeholder = Res.string.profile_choose_year,
                    onExpandedChange = {
                        isYearExpanded = it
                    },
                    onDismissRequest = {
                        isYearExpanded = false
                        focusManager.clearFocus()
                    },
                    isEnabled = true,
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
                                isYearExpanded = false
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
                        isSpecialtyExpanded = it
                    },
                    onDismissRequest = {
                        isSpecialtyExpanded = false
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
                                isSpecialtyExpanded = false
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
        }
    }
}