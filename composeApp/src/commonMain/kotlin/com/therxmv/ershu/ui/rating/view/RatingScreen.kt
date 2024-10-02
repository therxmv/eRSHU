package com.therxmv.ershu.ui.rating.view

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.therxmv.ershu.Res
import com.therxmv.ershu.di.getScreenModel
import com.therxmv.ershu.ui.base.BaseScreen
import com.therxmv.ershu.ui.base.views.ProgressIndicator
import com.therxmv.ershu.ui.rating.viewmodel.RatingViewModel
import com.therxmv.ershu.ui.rating.viewmodel.utils.RatingUiState

class RatingScreen : BaseScreen() {

    override val key = "RatingScreen"

    @Composable
    override fun Content() {
        val viewModel = getScreenModel<RatingViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        val inputsState by viewModel.inputsState.collectAsState()
        val ratingState by viewModel.ratingState.collectAsState()

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
            ) { state ->
                when (state) {
                    is RatingUiState.Ready -> RatingScreenContent(
                        modifier = modifier,
                        data = state.data,
                        inputs = inputsState,
                        onValueChange = viewModel::updateInputByIndex,
                        ratingState = ratingState,
                        calculateRating = viewModel::calculateRating,
                    )

                    is RatingUiState.NotAvailable -> NotAvailableBanner()
                    is RatingUiState.Loading -> ProgressIndicator()
                }
            }
        }
    }

    @Composable
    private fun NotAvailableBanner() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(36.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = Res.string.rating_not_available,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
            )
        }
    }
}