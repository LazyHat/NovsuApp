package com.lazyhat.novsuapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.lazyhat.novsuapp.R
import com.lazyhat.novsuapp.viewmodels.SettingsEvent
import com.lazyhat.novsuapp.viewmodels.SettingsViewModel

@Composable
fun SettingsPage(
    viewModel: SettingsViewModel = hiltViewModel(),
    openDrawer: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopBar(openDrawer, label = stringResource(id = R.string.settings_label)) }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            ListGridColorTheme(
                selectedItem = uiState.theme,
                label = stringResource(id = R.string.color_scheme),
                update = { theme ->
                    viewModel.createEvent(SettingsEvent.ColorSchemeChange(theme))
                }
            )
        }
    }
}