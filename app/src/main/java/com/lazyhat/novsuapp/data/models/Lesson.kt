package com.lazyhat.novsuapp.data.models

data class Lesson(
    val name: String,
    val type: LessonType,
    val time: LessonTime?,
    val teacher: String?,
    val week: LessonWeek,
    val description: String?
)
