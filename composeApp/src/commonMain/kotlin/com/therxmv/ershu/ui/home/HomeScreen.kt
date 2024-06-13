package com.therxmv.ershu.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.therxmv.ershu.di.getScreenModel
import com.therxmv.ershu.ui.base.BaseScreen
import com.therxmv.ershu.ui.base.ProgressIndicator
import com.therxmv.ershu.ui.home.utils.HomeUiEvent.ItemClick
import com.therxmv.ershu.ui.home.utils.HomeUiState.EmptyProfile
import com.therxmv.ershu.ui.home.utils.HomeUiState.Loading
import com.therxmv.ershu.ui.home.utils.HomeUiState.Ready
import com.therxmv.ershu.ui.home.views.HomeScreenContent
import com.therxmv.ershu.ui.specialtyinfo.SpecialtyInfoScreen

class HomeScreen : BaseScreen() {

    override val key = "HomeScreen"

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<HomeViewModel>()
        val uiState by viewModel.uiState.collectAsState()

        val navigator = LocalNavigator.currentOrThrow

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