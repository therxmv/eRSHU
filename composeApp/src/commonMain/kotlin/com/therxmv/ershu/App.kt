package com.therxmv.ershu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.therxmv.ershu.ui.home.HomeScreen
import com.therxmv.ershu.ui.theme.AppTheme
import com.therxmv.ershu.ui.base.ERSHUAppBar
import com.therxmv.ershu.ui.views.ScreenTitleProvider
import com.therxmv.ershu.ui.base.BaseScreen
import com.therxmv.ershu.ui.base.BaseViewModel

@Composable
internal fun App() = AppTheme {

    Navigator(HomeScreen()) { navigator ->
        Scaffold(
            topBar = {
                ERSHUAppBar(
                    canPop = navigator.canPop,
                    title = navigator.getAppBarTitle(),
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

fun Navigator.getAppBarTitle() = when(this.lastItem) {
    is ScreenTitleProvider -> (this.lastItem as ScreenTitleProvider).getTitle()
    else -> Res.string.app_name
}