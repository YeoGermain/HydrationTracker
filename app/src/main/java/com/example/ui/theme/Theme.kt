package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = TurquoisePrimary,
    onPrimary = Color(0xFF002025),
    primaryContainer = Color(0xFF004F58),
    onPrimaryContainer = TurquoiseTertiary,
    secondary = TurquoiseSecondary,
    onSecondary = Color(0xFF003735),
    secondaryContainer = SurfaceHoverDark,
    onSecondaryContainer = TurquoiseSecondary,
    tertiary = TurquoiseTertiary,
    onTertiary = Color(0xFF003735),
    background = BackgroundDark,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = TextSecondary,
    outline = Color(0xFF2B4452),
    outlineVariant = Color(0xFF1B2E38)
)

@Composable
fun HydrationTheme(
    darkTheme: Boolean = true, // Default to dark theme as requested
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}

// Retain alias for backwards compatibility with tests
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    HydrationTheme(darkTheme = darkTheme, content = content)
}
