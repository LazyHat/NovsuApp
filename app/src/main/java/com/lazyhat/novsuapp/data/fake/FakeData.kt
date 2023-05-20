package com.lazyhat.novsuapp.data.fake

import com.lazyhat.novsuapp.data.models.LessonDay
import com.lazyhat.novsuapp.data.models.Lesson
import com.lazyhat.novsuapp.data.models.LessonTime
import com.lazyhat.novsuapp.data.models.LessonType
import com.lazyhat.novsuapp.data.models.LessonWeek

object FakeData {
    val days = mapOf(
        0 to LessonDay(
            listOf(
                Lesson(
                    "Математика",
                    LessonType(LessonType.LECTURE),
                    LessonTime(10, 12),
                    "Люба",
                    LessonWeek.Lower,
                    "тупо пара"
                ),
                Lesson(
                    "Математика",
                    LessonType(LessonType.LABORATORY),
                    LessonTime(10, 12),
                    "Люба",
                    LessonWeek.Lower,
                    "тупо пара"
                )
            )
        ),
        1 to LessonDay(
            listOf(
                Lesson(
                    "Математика",
                    LessonType(LessonType.PRACTICE),
                    LessonTime(10, 12),
                    "Люба",
                    LessonWeek.Lower,
                    "тупо пара"
                ),
                Lesson(
                    "Математика",
                    LessonType(LessonType.PRACTICE),
                    LessonTime(10, 12),
                    "Люба",
                    LessonWeek.Lower,
                    "тупо пара"
                )
            )
        )
    )
}