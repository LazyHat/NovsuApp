package com.lazyhat.novsuapp.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun NovsuTheme(
    scheme: ColorScheme,
    content: @Composable () -> Unit
) {
    rememberSystemUiController().setSystemBarsColor(color = scheme.background)

    MaterialTheme(
        colorScheme = scheme,
        content = content,
        shapes = Shapes(
            extraSmall = RoundedCornerShape(1.dp),
            small = RoundedCornerShape(2.dp),
            medium = RoundedCornerShape(4.dp),
            large = RoundedCornerShape(6.dp)
        )
    )
}