package com.lazyhat.novsuapp.ui.activities.main.screens.timetable

data class TimeTableState(
    val days: List<LessonDayState>
)
data class LessonDayState(
    val lessons: List<LessonState>
)
data class LessonState(
    val
)