package com.example.novsucompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.novsucompose.screens.MainScreen
import com.example.novsucompose.ui.theme.NovsuTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NovsuTheme {
                MainScreen()
            }
        }
    }
}

