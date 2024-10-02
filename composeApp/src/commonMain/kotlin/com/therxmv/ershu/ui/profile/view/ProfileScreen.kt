package com.therxmv.ershu.ui.profile.view

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.therxmv.ershu.di.getScreenModel
import com.therxmv.ershu.ui.base.BaseScreen
import com.therxmv.ershu.ui.base.views.ProgressIndicator
import com.therxmv.ershu.ui.profile.viewmodel.ProfileViewModel
import com.therxmv.ershu.ui.profile.viewmodel.utils.ProfileUiState.Loading
import com.therxmv.ershu.ui.profile.viewmodel.utils.ProfileUiState.Ready
import com.therxmv.ershu.ui.specialtyinfo.view.SpecialtyInfoScreen

class ProfileScreen : BaseScreen() {

    override val key = "ProfileScreen"

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<ProfileViewModel>()
        val navigator = LocalNavigator.currentOrThrow

        val uiState by viewModel.profileState.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.loadData()
        }

        DisposableEffect(Unit) {
            onDispose {
                viewModel.resetState()
            }
        }

        super.BaseContent { modifier ->
            Crossfade(
                targetState = uiState,
                animationSpec = tween(500),
            ) {
                when (it) {
                    is Ready -> {
                        val data = uiState as Ready
                        ProfileScreenContent(
                            modifier = modifier,
                            facultyName = data.faculty,
                            specialtyName = data.specialty,
                            onEditClick = {
                                navigator.push(SpecialtyInfoScreen()) // TODO make screen provider
                            }
                        )
                    }
                    is Loading -> ProgressIndicator()
                }
            }
        }
    }
}