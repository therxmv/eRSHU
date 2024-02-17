package com.therxmv.ershu.theme

import android.app.Activity
import android.graphics.Color
import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
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

@Composable
internal actual fun getDynamicColorSchemes(): Pair<ColorScheme?, ColorScheme?> =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val context = LocalView.current.context
        Pair(dynamicLightColorScheme(context), dynamicDarkColorScheme(context))
    } else {
        Pair(null, null)
    }