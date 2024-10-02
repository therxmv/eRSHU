package com.therxmv.ershu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.therxmv.ershu.di.getDependency
import com.therxmv.ershu.ui.base.AppbarTitleStore
import com.therxmv.ershu.ui.base.BaseScreen
import com.therxmv.ershu.ui.base.BaseViewModel
import com.therxmv.ershu.ui.base.views.ERSHUAppBar
import com.therxmv.ershu.ui.home.view.HomeScreen
import com.therxmv.ershu.ui.theme.AppTheme

@Composable
internal fun App(
    appbarTitleProvider: AppbarTitleStore = getDependency(),
) = AppTheme {
    Navigator(HomeScreen()) { navigator ->
        val titleFlow = appbarTitleProvider.titleFlow.collectAsState().value
        Scaffold(
            topBar = {
                ERSHUAppBar(
                    canPop = navigator.canPop,
                    title = titleFlow,
                    onBackClick = {
                        navigator.pop()
                    },
                    onBellClick = navigator.getBellClick(),
                )
            },
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                CurrentScreen()
            }
        }
    }
}

fun Navigator.getBellClick() = when(this.lastItem) {
    is BaseScreen -> BaseViewModel::toggleDialog
    else -> null
}