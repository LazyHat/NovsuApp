package com.lazyhat.novsuapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazyhat.novsuapp.BuildConfig
import com.lazyhat.novsuapp.R
import com.lazyhat.novsuapp.data.DataSource
import com.lazyhat.novsuapp.ui.theme.NovsuTheme

@Composable
fun AboutPage(openDrawer: () -> Unit) {
    Scaffold(topBar = {
        TopBar(openDrawer = openDrawer, label = stringResource(id = R.string.about_label))
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(it),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Image(
                painterResource(id = R.drawable.ic_launcher),
                "mainIc",
                modifier = Modifier
                    .fillMaxHeight(0.2f)
                    .fillMaxWidth()
            )
            Text(
                text = stringResource(id = R.string.app_name),
                style = TextStyle(fontSize = 30.sp),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(20.dp))
            TextField(text = "${stringResource(id = R.string.version)}: ${BuildConfig.VERSION_NAME}")
            Spacer(modifier = Modifier.height(40.dp))
            AnnotatedTextField(
                text = stringResource(id = R.string.about_bugs),
                annotatedWordInText = stringResource(
                    id = R.string.about_link_p
                ),
                url = "https://forms.gle/uQjDuZ2qowv3ie8L9"
            )
            AnnotatedTextField(
                text = stringResource(id = R.string.about_suggestions),
                annotatedWordInText = stringResource(
                    id = R.string.about_link_p
                ),
                url = "https://forms.gle/a7qre3bjpvFo65JK6"
            )
            AnnotatedTextField(
                text = stringResource(id = R.string.about_roadmap),
                annotatedWordInText = stringResource(
                    id = R.string.about_link_p
                ),
                url = "https://docs.google.com/spreadsheets/d/18v-n_Ja5l1qha7y5AFK4FT0Z0ZRy9FyRWSeb9xDRoys/edit?usp=sharing"
            )
            AnnotatedTextField(
                text = stringResource(id = R.string.about_vk),
                annotatedWordInText = stringResource(
                    id = R.string.about_link_i
                ),
                url = "https://vk.com/lazyhatdevclub"
            )
        }
    }
}

@Composable
private fun TextField(text: String) {
    Text(text = text, style = TextStyle(fontSize = 14.sp), modifier = Modifier.padding(5.dp))
}

@Composable
private fun AnnotatedTextField(text: String, annotatedWordInText: String, url: String) {
    val startIndex = text.indexOf(annotatedWordInText)
    val endIndex = startIndex + annotatedWordInText.length
    val astr = buildAnnotatedString {
        append(text)
        addStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.surfaceVariant
            ),
            startIndex,
            endIndex
        )
        addStringAnnotation("astr", url, startIndex, endIndex)
    }
    val uriHandler = LocalUriHandler.current
    ClickableText(
        text = astr,
        style = TextStyle(fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground),
        modifier = Modifier.padding(5.dp)
    ) {
        astr.getStringAnnotations(startIndex, endIndex).firstOrNull()?.let {
            uriHandler.openUri(it.item)
        }
    }
}

@Preview
@Composable
fun AboutPagePreview() {
    NovsuTheme(scheme = DataSource.ColorSchemes.Pink.scheme) {
        AboutPage {}
    }
}