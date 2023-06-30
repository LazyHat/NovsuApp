package com.lazyhat.novsuapp.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Lesson(
    val id: Int,
    override val name: String,
    override val type: LessonType?,
    override val time: LessonTime?,
    override val auditorium: String?,
    override val teacher: String?,
    override val week: LessonWeek,
    override val subGroup: Int?,
    override val description: String?
    val isNew: Boolean,
) : LessonBase

