package com.lazyhat.novsuapp.viewmodels


import androidx.lifecycle.*
import com.lazyhat.novsuapp.data.*
import com.lazyhat.novsuapp.data.LessonsWorkerStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


const val LESSONS_WEEK_FILTER_KEY = "lessons_filter_key"

data class TTUiState(
    val days: List<DayModel> = listOf(),
    val weekFilter: Week = Week.All,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isEmpty: Boolean = false,
    val error: String = "",
    val dayOfWeek: Int = 0,
    val isCurrentWeek: Boolean = false,
) {
    fun isNormal(): Boolean = !(isEmpty || isError || isLoading)
}

//data class TableUiState(
//    val cols: Int = 0,
//    val rows: Int = 0,
//    val days: List<Map<TableTime, CellInfo>> = listOf()
//)

//data class CellInfo(
//    val text: String = "",
//    val style: TextStyle = TextStyle(),
//    val color: Color = Color.Transparent
//)

sealed class TTEvent {
    object ChangeSort : TTEvent()
    object Refresh : TTEvent()
}

@HiltViewModel
class TTViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _savedWeekFilter: StateFlow<Week?> =
        savedStateHandle.getStateFlow(LESSONS_WEEK_FILTER_KEY, null)
    private val _daysStream = mainRepository.getDaysStream()

    val uiState: StateFlow<TTUiState> =
        combine(
            _daysStream,
            _savedWeekFilter,
            mainRepository.getTimeStream()
        ) { days, filter, timeData ->
            if (filter == null || timeData.week == null) {
                savedStateHandle[LESSONS_WEEK_FILTER_KEY] = timeData.week
                TTUiState(isLoading = true)
            } else {
                val filteredDays = filterDays(days, filter)
                TTUiState(
                    filteredDays,
                    filter,
                    isLoading = days == LessonsWorkerStatus.Loading.EXTERNAL,
                    isEmpty = days.isEmpty(),
                    dayOfWeek = timeData.dayOfWeek,
                    isCurrentWeek = timeData.week == filter
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TTUiState(isLoading = true)
        )

//    val tableUiState: StateFlow<TableUiState> = uiState.map { state ->
//        val hours = getAllHours(state.days)
//        TableUiState(cols = 2, rows = hours.size, state.days.map { it.toTableMap(hours) })
//    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TableUiState())

    fun createEvent(event: TTEvent) = onEvent(event)

    @Suppress("IMPLICIT_CAST_TO_ANY")
    private fun onEvent(event: TTEvent) = when (event) {
        is TTEvent.ChangeSort ->
            when (uiState.value.weekFilter) {
                Week.Lower -> savedStateHandle[LESSONS_WEEK_FILTER_KEY] = Week.Upper
                Week.Upper -> savedStateHandle[LESSONS_WEEK_FILTER_KEY] = Week.All
                Week.All -> savedStateHandle[LESSONS_WEEK_FILTER_KEY] = Week.Lower
            }
        is TTEvent.Refresh ->
            viewModelScope.launch {
                mainRepository.refreshWeek()
            }
    }

//    private fun getAllHours(filteredDays: List<DayModel>): List<TableTime> {
//        val hours = mutableListOf<TableTime>()
//        filteredDays.forEach { day ->
//            day.getHours().forEach { tt ->
//                if (hours.find { it == tt } == null)
//                    hours.add(tt)
//            }
//        }
//        return hours.sortedBy { tt ->
//            tt.start.substring(0 until tt.start.indexOf(':')).toInt()
//        }
//    }

//    private fun DayModel.toTableMap(hours: List<TableTime>): Map<TableTime, CellInfo> {
//        val result = mutableMapOf<TableTime, CellInfo>()
//        lessons.forEach { lesson ->
//            if (lesson.time != null) {
//                result[hours.find { it.start == lesson.time.start }!!] = CellInfo(lesson.lessonName)
//            }
//        }
//        return result
//    }

    private fun filterDays(days: List<DayModel>, filter: Week): List<DayModel> {
        return days.map { day ->
            day.copy(lessons = day.lessons.filter { filter == Week.All || it.week == Week.All || filter == it.week })
        }
    }
}