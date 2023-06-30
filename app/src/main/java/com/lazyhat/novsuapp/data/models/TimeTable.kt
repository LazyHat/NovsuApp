package com.lazyhat.novsuapp.data.models

import kotlinx.serialization.Serializable

@Serializable
data class TimeTable(
    val parameters: TimeTableParameters,
    val timetable: List<LessonDay>
)
