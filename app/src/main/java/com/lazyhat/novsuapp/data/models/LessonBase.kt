package com.lazyhat.novsuapp.data.models

interface LessonBase {
    val name: String
    val type: LessonType?
    val time: LessonTime?
    val auditorium: String?
    val teacher: String?
    val week: LessonWeek
    val subGroup: Int?
    val description: String?
}