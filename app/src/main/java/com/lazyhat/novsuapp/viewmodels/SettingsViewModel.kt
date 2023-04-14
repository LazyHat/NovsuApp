package com.lazyhat.novsuapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazyhat.novsuapp.data.DataSource
import com.lazyhat.novsuapp.data.MainRepository
import com.lazyhat.novsuapp.data.datastore.settings.Settings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SettingsEvent {
    data class ColorSchemeChange(val scheme: DataSource.ColorSchemes) : SettingsEvent()
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {
    val uiState = mainRepository.getSettingsStream().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Settings()
    )

    fun createEvent(event: SettingsEvent) = onEvent(event)

    private fun onEvent(event: SettingsEvent) = when (event) {
        is SettingsEvent.ColorSchemeChange -> {
            viewModelScope.launch {
                mainRepository.updateSettings(
                    uiState.value.copy(
                        theme = event.scheme
                    )
                )
            }
        }
    }
}