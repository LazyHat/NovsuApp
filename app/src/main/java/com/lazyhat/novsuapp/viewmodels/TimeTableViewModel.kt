package com.lazyhat.novsuapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lazyhat.novsuapp.data.GroupSpecs
import com.lazyhat.novsuapp.data.TTModel
import com.lazyhat.novsuapp.data.Week
import com.lazyhat.novsuapp.data.getData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class TimeTableViewModel(groupSpecs: GroupSpecs) : ViewModel() {

    private val _uiState = MutableStateFlow(TTModel(sort = null))
    val uiState = _uiState.asStateFlow()

    init {
        updateWeekModel(groupSpecs)
    }

    fun updateSort(newSort: Week?) {
        _uiState.update { ttModel -> ttModel.copy(sort = newSort) }
    }

    private fun updateWeekModel(groupSpecs: GroupSpecs) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { ttModel ->
                ttModel.copy(
                    weekModel = getData(groupSpecs)
                )
            }
        }
    }
}