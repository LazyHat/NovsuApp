package com.lazyhat.novsuapp.data.fake

import com.lazyhat.novsuapp.data.models.LessonDay
import com.lazyhat.novsuapp.data.models.Lesson
import com.lazyhat.novsuapp.data.models.LessonTime
import com.lazyhat.novsuapp.data.models.LessonType
import com.lazyhat.novsuapp.data.models.LessonWeek
import com.lazyhat.novsuapp.data.models.TimeTable
import com.lazyhat.novsuapp.data.models.TimeTableParameters

object FakeData {
    val data = TimeTable(
        parameters = TimeTableParameters(1, "wefwef", "2092"),
        timetable = listOf(
            LessonDay(
                listOf(
                    Lesson(
                        0,
                        "Математика и программирование",
                        LessonType(LessonType.LECTURE, LessonType.PRACTICE, LessonType.LABORATORY),
                        LessonTime(10, 12),
                        "123",
                        "Люба",
                        LessonWeek.Lower,
                        2,
                        "тупо пара",
                        true
                    ),
                    Lesson(
                        1,
                        "Математика",
                        LessonType(LessonType.LABORATORY),
                        LessonTime(10, 12),
                        null,
                        "Люба",
                        LessonWeek.Lower,
                        null,
                        "тупо пара",
                        true
                    ),
                    Lesson(
                        2,
                        "Математика",
                        LessonType(LessonType.LABORATORY),
                        LessonTime(10, 11),
                        null,
                        "Люба",
                        LessonWeek.Lower,
                        3,
                        "тупо пsdfdfара",
                        false
                    ),
                    Lesson(
                        3,
                        "Математика fdsfsdfsdfsdfsdfasfdasdfasdf",
                        LessonType(LessonType.LABORATORY),
                        LessonTime(10, 16),
                        null,
                        "Люба",
                        LessonWeek.Lower,
                        1,
                        "тупо пfdsfара",
                        false
                    ),
                    Lesson(
                        4,
                        "Математика asdffffffffffffffffffffffff",
                        null,
                        LessonTime(10, 12),
                        null,
                        "Люба",
                        LessonWeek.Lower,
                        null,
                        "тупоfds",
                        true
                    )
                )
            ),
            LessonDay(
                listOf(
                    Lesson(
                        5,
                        "Математика",
                        LessonType(LessonType.PRACTICE),
                        LessonTime(10, 12),
                        "2314",
                        "Люба",
                        LessonWeek.Lower,
                        2,
                        "тупо пара",
                        true
                    ),
                    Lesson(
                        6,
                        "Математика",
                        LessonType(LessonType.PRACTICE),
                        LessonTime(10, 12),
                        null,
                        "Люба",
                        LessonWeek.Lower,
                        1,
                        "тупо пара",
                        false
                    )
                )
            )
        )
    )
}