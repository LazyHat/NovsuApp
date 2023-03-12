package com.lazyhat.novsuapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.lazyhat.novsuapp.R

@Composable
fun SettingsPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(stringResource(id = R.string.settings_label))
    }
}