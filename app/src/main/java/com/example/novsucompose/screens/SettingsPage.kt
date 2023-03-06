package com.example.novsucompose.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import com.example.novsucompose.R

@Composable
fun SettingsPage() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(stringArrayResource(id = R.array.menu_items)[2])
    }
}