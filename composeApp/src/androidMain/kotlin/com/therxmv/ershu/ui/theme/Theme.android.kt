package com.therxmv.ershu.ui.theme

import android.app.Activity
import android.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat.getInsetsController
import androidx.core.view.WindowCompat.setDecorFitsSystemWindows

@Composable
internal actual fun SystemAppearance(isDark: Boolean) {
    val view = LocalView.current
    val systemBarColor = Color.TRANSPARENT

    LaunchedEffect(isDark) {
        val window = (view.context as Activity).window

        with(window) {
            statusBarColor = systemBarColor
            navigationBarColor = systemBarColor

            setDecorFitsSystemWindows(this, false)
            getInsetsController(this, decorView).apply {
                isAppearanceLightStatusBars = isDark
                isAppearanceLightNavigationBars = isDark
            }
        }
    }
}