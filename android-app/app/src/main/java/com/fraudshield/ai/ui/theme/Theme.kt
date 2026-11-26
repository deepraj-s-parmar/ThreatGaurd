package com.fraudshield.ai.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val FraudShieldColorScheme = darkColorScheme(
    primary = Blue500,
    onPrimary = White,
    primaryContainer = Navy700,
    onPrimaryContainer = Blue300,
    secondary = Indigo500,
    onSecondary = White,
    secondaryContainer = Navy700,
    onSecondaryContainer = Indigo400,
    tertiary = GreenSafe,
    onTertiary = White,
    background = Navy950,
    onBackground = Gray200,
    surface = Navy900,
    onSurface = Gray200,
    surfaceVariant = Navy800,
    onSurfaceVariant = Gray400,
    error = RedCritical,
    onError = White,
    outline = Gray600,
    outlineVariant = Navy600,
)

@Composable
fun FraudShieldTheme(content: @Composable () -> Unit) {
    val colorScheme = FraudShieldColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Navy950.toArgb()
            window.navigationBarColor = Navy950.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
