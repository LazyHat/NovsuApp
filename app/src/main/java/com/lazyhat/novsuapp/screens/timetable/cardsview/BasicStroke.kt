package com.lazyhat.novsuapp.screens.timetable.cardsview

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun CardStroke(
    modifier: Modifier = Modifier,
    text: String?,
    subtext: String = "",
    style: TextStyle = TextStyle(fontSize = 16.sp),
    textAlign: TextAlign = TextAlign.Start,
    color: Color = MaterialTheme.colorScheme.onPrimaryContainer
) {
    if (!text.isNullOrEmpty()) {
        Text(
            modifier = modifier,
            text = "${if (subtext.isEmpty()) "" else "$subtext: "}$text",
            textAlign = textAlign,
            style = style,
            color = color
        )
    }
}