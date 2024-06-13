package com.therxmv.ershu.ui.profile

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.therxmv.ershu.Res
import com.therxmv.ershu.di.getScreenModel
import com.therxmv.ershu.ui.base.BaseScreen
import com.therxmv.ershu.ui.base.ProgressIndicator
import com.therxmv.ershu.ui.profile.utils.ProfileUiState.Loading
import com.therxmv.ershu.ui.profile.utils.ProfileUiState.Ready
import com.therxmv.ershu.ui.profile.views.ProfileScreenContent
import com.therxmv.ershu.ui.specialtyinfo.SpecialtyInfoScreen
import com.therxmv.ershu.ui.views.ScreenTitleProvider

class ProfileScreen : BaseScreen(), ScreenTitleProvider {

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

    override fun getTitle() = Res.string.profile_title
}