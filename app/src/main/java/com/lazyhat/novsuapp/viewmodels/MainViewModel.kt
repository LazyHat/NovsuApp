package com.lazyhat.novsuapp.viewmodels

import androidx.lifecycle.ViewModel
import com.lazyhat.novsuapp.data.GroupSpecs
import com.lazyhat.novsuapp.data.MainModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MainModel())
    val uiState: StateFlow<MainModel> = _uiState.asStateFlow()

    fun updateGroupSpecs(newGroupSpecs: GroupSpecs) {
        _uiState.update { mainModel -> mainModel.copy(groupSpecs = newGroupSpecs) }
    }
}