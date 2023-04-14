package com.lazyhat.novsuapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazyhat.novsuapp.data.MainRepository
import com.lazyhat.novsuapp.data.datastore.settings.Settings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    val uiState = mainRepository.getSettingsStream().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000),
        Settings()
    )

    fun onCreate() {
        viewModelScope.launch {
            mainRepository.initDataBase()
        }
    }
}
