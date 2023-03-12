package com.lazyhat.novsuapp.data

import org.jsoup.nodes.Document
import java.time.LocalTime

data class GroupSpecs(
    val institute: Institute = Institute.IEIS,
    val grade: String = "1",
    val group: String = "2092",
    val subGroup: String = "2"
)

data class EditGroupModel(
    val groupSpecs: GroupSpecs = GroupSpecs(),
    val groupsList: List<String> = listOf()
)

data class Response(
    val ttDoc: Document = Document(""),
    val mainDoc: Document = Document("")
)

data class MainModel(
    val groupSpecs: GroupSpecs = GroupSpecs()
)

data class TTModel(
    val weekModel: WeekModel = WeekModel(),
    val sort: Week?
)

data class WeekModel(
    val groupSpecs: GroupSpecs = GroupSpecs(),
    val week: Week? = null,
    val error: String? = null,
    val days: List<DayModel> = listOf()
)

data class DayModel(
    val dayName: String,
    val lessons: List<LessonModel>
)

data class LessonModel(
    val time: Time,
    val lessonName: String,
    val lessonType: String,
    val subGroup: String,
    val auditorium: String,
    val teacher: String,
    val week: Week?,
    val description: String,
)

data class Time(
    val start: LocalTime,
    val end: LocalTime,
    val hours: Int
) {
    fun getString() = "${this.start} - ${this.end}, ${this.hours}"
}
