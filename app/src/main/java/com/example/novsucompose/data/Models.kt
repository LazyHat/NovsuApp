package com.example.novsucompose.data

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import org.jsoup.nodes.Document
import java.time.LocalTime

data class GroupSpecs(
    val institute: Institute = Institute.IEIS,
    val grade: String = "1",
    val group: String = "2092",
    val subGroup: String = "2",
    val groupList: List<String> = listOf(),
)

data class Response(
    val ttDoc: Document = Document(""),
    val mainDoc: Document = Document("")
)

data class TTModel(
    val mainModel: MainModel,
    val sort: Week
)

data class MainModel(
    val groupSpecs: GroupSpecs = GroupSpecs(),
    val week: Week = Week.Upper,
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
    val week: Week,
    val description: String,
)

data class Time(
    val start: LocalTime,
    val end: LocalTime,
    val hours: Int
)