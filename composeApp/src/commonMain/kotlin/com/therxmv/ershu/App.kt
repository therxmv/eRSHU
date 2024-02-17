package com.therxmv.ershu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.therxmv.ershu.theme.AppTheme
import com.therxmv.ershu.ui.profile.ProfileScreen
import com.therxmv.ershu.ui.views.ERSHUAppBar
import com.therxmv.ershu.ui.views.ScreenTitleProvider
import com.therxmv.ershu.ui.views.calls.CallsScreen

@Composable
internal fun App() = AppTheme {

    Navigator(ProfileScreen()) {
        Scaffold(
            topBar = {
                ERSHUAppBar(
                    canPop = it.canPop,
                    title = it.getAppBarTitle(),
                    onBackClick = {
                        it.pop()
                    },
                    onBellClick = it.getBellClick(),
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
    is CallsScreen -> (this.lastItem as CallsScreen)::toggleDialog
    else -> null
}

fun Navigator.getAppBarTitle() = when(this.lastItem) {
    is ScreenTitleProvider -> (this.lastItem as ScreenTitleProvider).getTitle()
    else -> Res.string.app_name
}