package com.lazyhat.novsuapp.screens.timetable.cardsview

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun LinkedCardStroke(
    modifier: Modifier = Modifier,
    text: String?,
    subtext: String = "",
    style: TextStyle = TextStyle(
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onPrimaryContainer
    ),
    textAlign: TextAlign = TextAlign.Start,
) {
    if (!text.isNullOrEmpty()) {
        if (text.contains("https")) {
            val astr = buildAnnotatedString {
                val str = "${if (subtext.isEmpty()) "" else "$subtext: "}$text"
                val startIndex = str.indexOf("https")
                val endIndex = str.length
                append(str)
                addStyle(
                    style = SpanStyle(color = MaterialTheme.colorScheme.secondary),
                    startIndex,
                    endIndex
                )
                addStringAnnotation(text, annotation = text, start = startIndex, end = endIndex)
            }
            val uriHandler = LocalUriHandler.current
            ClickableText(text = astr, onClick = {
                astr.getStringAnnotations(text, it, it).firstOrNull()?.let { stringAnnotation ->
                    uriHandler.openUri(stringAnnotation.item)
                }
            }, style = style)
        } else {
            Text(
                modifier = modifier,
                text = "${if (subtext.isEmpty()) "" else "$subtext: "}$text",
                textAlign = textAlign,
                style = style
            )
        }
    }
}