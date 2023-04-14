package com.lazyhat.novsuapp.data

data class DayModel(
    val lessons: List<LessonModel>
)

data class LessonModel(
    val time: Time?,
    val lessonName: String,
    val lessonType: String,
    val subGroup: String,
    val auditorium: String,
    val teacher: String?,
    val week: Week,
    val description: String,
)

data class Time(
    val start: String,
    val end: String,
    val hours: Int
) {
    fun getString() = "${this.start} - ${this.end}, ${this.hours}"
}