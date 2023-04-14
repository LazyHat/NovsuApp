package com.lazyhat.novsuapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.lazyhat.novsuapp.data.DataSource
import com.lazyhat.novsuapp.navigation.NovsuNavGraph
import com.lazyhat.novsuapp.ui.theme.NovsuTheme
import com.lazyhat.novsuapp.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val mainViewModel: MainViewModel = hiltViewModel()
            val uiState = mainViewModel.uiState.collectAsState()
            mainViewModel.onCreate()
            NovsuTheme(scheme = uiState.value.theme.scheme) {
                NovsuNavGraph()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    NovsuTheme(DataSource.ColorSchemes.Default.scheme) {
        NovsuNavGraph()
    }
}


