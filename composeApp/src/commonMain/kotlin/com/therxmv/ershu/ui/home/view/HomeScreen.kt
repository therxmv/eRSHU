package com.therxmv.ershu.ui.home.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.therxmv.ershu.di.getScreenModel
import com.therxmv.ershu.ui.base.BaseScreen
import com.therxmv.ershu.ui.base.views.ProgressIndicator
import com.therxmv.ershu.ui.home.viewmodel.HomeViewModel
import com.therxmv.ershu.ui.home.viewmodel.utils.HomeUiEvent.ItemClick
import com.therxmv.ershu.ui.home.viewmodel.utils.HomeUiState.EmptyProfile
import com.therxmv.ershu.ui.home.viewmodel.utils.HomeUiState.Loading
import com.therxmv.ershu.ui.home.viewmodel.utils.HomeUiState.Ready
import com.therxmv.ershu.ui.specialtyinfo.view.SpecialtyInfoScreen

class HomeScreen : BaseScreen() {

    override val key = "HomeScreen"

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<HomeViewModel>()
        val uiState by viewModel.uiState.collectAsState()

        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(Unit) {
            viewModel.setTitle()
        }

        super.BaseContent { modifier ->
            when (uiState) {
                is EmptyProfile -> navigator.replace(SpecialtyInfoScreen(true))
                is Loading -> ProgressIndicator()
                is Ready -> {
                    HomeScreenContent(
                        modifier = modifier,
                        items = (uiState as Ready).items,
                        onItemClick = {
                            viewModel.onEvent(ItemClick(navigator, it))
                        }
                    )
                }
            }
        }
    }
}