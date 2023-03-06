package com.example.novsucompose.data

import org.jsoup.HttpStatusException
import org.jsoup.nodes.Document
import java.io.IOException
import java.net.SocketTimeoutException
import java.time.LocalTime

fun tryCatchIO(content: () -> Unit): ErrorCodes {
    try {
        content()
    } catch (e: IOException) {
        return ErrorCodes.IOError
    } catch (e: HttpStatusException) {
        return ErrorCodes.ResponseError
    } catch (e: SocketTimeoutException) {
        return ErrorCodes.TimeOutError
    }
    return ErrorCodes.OK
}

enum class ErrorCodes(val msg: String) {
    OK(""),
    IOError("Отсутствие сети"),
    TimeOutError("Время ожидания истекло"),
    ResponseError("Ошибка запроса")
}

data class Request(
    val instituteId: String,
    var group: String,
    val subGroup: String
)

data class Response(
    val group: String,
    val subGroup: String,
    val mainDoc: Document,
    val groupDoc: Document
)

data class MainModel(
    val group: String,
    val subGroup: String,
    val week: Week,
    val days: List<DayModel>
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
) {
    fun getString(): String {
        return "${start}-${end} $hours"
    }
}

enum class Week(val label: String) {
    Upper("Верхняя"),
    Lower("Нижняя"),
    All("")
}
