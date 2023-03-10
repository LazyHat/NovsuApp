package com.example.novsucompose.viewmodels

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.novsucompose.data.LessonModel
import com.example.novsucompose.data.MainModel
import com.example.novsucompose.data.TTModel
import com.example.novsucompose.data.Week
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class TimeTableViewModel(model: MainModel) : ViewModel() {

    private val _uiState = MutableStateFlow(TTModel(model, Week.All))
    val uiState = _uiState.asStateFlow()

    fun updateSort(newSort: Week) {
        _uiState.update { ttModel -> ttModel.copy(sort = newSort) }
    }

    fun getLessons(currentDayIndex: Int): List<LessonModel> {
        return uiState.value.mainModel.days[currentDayIndex].lessons
    }
}