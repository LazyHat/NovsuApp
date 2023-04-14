package com.lazyhat.novsuapp.data

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import com.lazyhat.novsuapp.R
import com.lazyhat.novsuapp.ui.theme.*

object DataSource {
    val gradesList = listOf("1", "2", "3", "4", "5", "6")
    val subGroupsList = listOf("1", "2", "3")

    @Suppress("unused")
    enum class ColorSchemes(val scheme: ColorScheme, val labelRes: Int) {
        Default(
            DEFAULT_COLORS, R.string.default_theme
        ),
        Pink(
            DEFAULT_COLORS.copy(
                primary = Pink60,
                secondary = Pink75,
                secondaryContainer = Pink65,
                outline = Pink75,
                onTertiary = Pink75,
                scrim = Pink20,
                surfaceVariant = Pink75,
                background = Color.Black,
                surface = Color.Black,
                onSurface = Color.Black,
            ),
            R.string.pink_theme
        ),
        Black(
            DEFAULT_COLORS.copy(
                primary = Gray10,
                background = Color.Black,
                surface = Color.Black,
                onSurface = Color.Black,
                secondary = Color.White,
                secondaryContainer = Gray20,
                onTertiary = Color.White,
                outline = Gray20,
                scrim = Color.Black,
                surfaceVariant = Gray60
            ),
            R.string.black_theme
        )
    }
}

private val DEFAULT_COLORS = ColorScheme(
    primary = Blue60,
    onPrimary = Color.White,
    primaryContainer = Gray10,
    onPrimaryContainer = Color.White,
    inversePrimary = Color.Red,
    secondary = Blue80,
    onSecondary = Color.Red,
    secondaryContainer = Blue70,
    onSecondaryContainer = Color.White,
    tertiary = Color.White, //All Tabs text
    onTertiary = Blue70, //Tabs Indicator, and selected text
    surface = Gray30,
    onSurface = Gray20,
    background = Gray10,
    onBackground = Color.White,
    tertiaryContainer = Color.Red,
    onTertiaryContainer = Color.Red,
    onSurfaceVariant = Color.Red,
    surfaceVariant = Blue80, //Annotations
    outline = Blue70,
    outlineVariant = Gray10,
    inverseSurface = Color.Red,
    inverseOnSurface = Color.Red,
    surfaceTint = Color.White,
    scrim = Blue50,
    error = Color.Red,
    onError = Color.Red,
    errorContainer = Color.Red,
    onErrorContainer = Color.Red
)