package com.lazyhat.novsuapp.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

const val LESSONS_TABLE = "lessons"

@Entity(tableName = LESSONS_TABLE)
data class LocalLesson(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var dayIndex: Int,
    var name: String,
    var timeStart: String?,
    var timeEnd: String?,
    var countOfHours: Int?,
    var type: String,
    var subGroup: String,
    var auditorium: String,
    var teacher: String?,
    var week: String,
    var description: String
)