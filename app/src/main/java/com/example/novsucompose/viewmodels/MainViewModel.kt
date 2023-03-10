package com.example.novsucompose.viewmodels

import androidx.lifecycle.ViewModel
import com.example.novsucompose.data.MainModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MainModel())
    val uiState: StateFlow<MainModel> = _uiState.asStateFlow()
}