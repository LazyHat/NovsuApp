package com.example.novsucompose.data

import java.time.LocalTime

val EMPTY_MODEL = MainModel(
    "", "", Week.Lower, listOf(
        DayModel(
            "", listOf(
                LessonModel(
                    Time(
                        LocalTime.of(0, 0), LocalTime.of(0, 0), "null"
                    ), "", "", "", "", "", Week.All, ""
                )
            )
        )
    )
)

val DEFAULT_GROUP = Request(
    instituteId = "815132",
    group = "2092",
    subGroup = "2"
)