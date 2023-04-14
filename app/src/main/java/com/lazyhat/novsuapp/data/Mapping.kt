package com.lazyhat.novsuapp.data

import android.os.Bundle
import com.lazyhat.novsuapp.data.datastore.groupspecs.GroupSpecs
import com.lazyhat.novsuapp.data.db.LocalLesson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.random.Random

fun LocalLesson.toExternal(): LessonModel {
    var result = LessonModel(
        lessonName = name,
        time = null,
        lessonType = type,
        subGroup = subGroup,
        auditorium = auditorium,
        teacher = teacher,
        week = Week.valueOf(week),
        description = description
    )
    if (!timeStart.isNullOrEmpty()) {
        result = result.copy(Time(timeStart!!, timeEnd!!, countOfHours!!))
    }
    if (!week.isEmpty()) {
        result = result.copy(week = Week.valueOf(week))
    }
    return result
}

fun List<LocalLesson>.toDaysList(): List<DayModel> {
    val days = listOf(
        mutableListOf<LessonModel>(), mutableListOf(), mutableListOf(), mutableListOf(),
        mutableListOf(), mutableListOf(), mutableListOf()
    )
    forEach {
        days[it.dayIndex].add(it.toExternal())
    }
    return days.map {
        DayModel(it.toList())
    }
}

fun LessonModel.toLocal(dayIndex: Int): LocalLesson = LocalLesson(
    id = Random.nextInt(),
    dayIndex = dayIndex,
    name = lessonName,
    timeStart = time?.start,
    timeEnd = time?.end,
    countOfHours = time?.hours,
    type = lessonType,
    subGroup = subGroup,
    auditorium = auditorium,
    teacher = teacher,
    week = week.name,
    description = description
)

fun List<DayModel>.toLocal(): List<LocalLesson> {
    val result = mutableListOf<LocalLesson>()
    forEachIndexed { index, day ->
        day.lessons.forEach {
            result.add(it.toLocal(index))
        }
    }
    return result
}

private const val GS_KEY = "gs_key"

fun GroupSpecs.toBundle(): Bundle {
    val bundle = Bundle()
    bundle.putByteArray(
        GS_KEY,
        Json.encodeToString(serializer(), this).encodeToByteArray()
    )
    return bundle
}

fun Flow<Bundle>.toGroupSpecs(): Flow<GroupSpecs> {
    return this.map {
        if (it.containsKey(GS_KEY))
            Json.decodeFromString(
                GroupSpecs.serializer(),
                it.getByteArray(GS_KEY)!!.decodeToString()
            )
        else
            GroupSpecs()
    }
}

//fun DayModel.getHours(): List<TableTime> {
//    var minTime = 24
//    var maxTime = 0
//    lessons.forEach {
//        if (it.time != null) {
//            minTime = minTime.coerceAtMost(it.time.start.substring(0..1).toInt())
//            maxTime = minTime.coerceAtLeast(it.time.end.substring(0..1).toInt())
//        }
//    }
//    return minTime.rangeTo(maxTime).map { start ->
//        TableTime("$start:00", "$start:45")
//    }
//}

//fun List<LessonModel>.getHours(): List<TableTime> {
//    var minTime = 24
//    var maxTime = 0
//    forEach {
//        if (it.time != null) {
//            minTime = minTime.coerceAtMost(it.time.start.substring(0..1).toInt())
//            maxTime = minTime.coerceAtLeast(it.time.end.substring(0..1).toInt())
//        }
//    }
//    return minTime.rangeTo(maxTime).map { start ->
//        TableTime("$start:00", "$start:45")
//    }
//}