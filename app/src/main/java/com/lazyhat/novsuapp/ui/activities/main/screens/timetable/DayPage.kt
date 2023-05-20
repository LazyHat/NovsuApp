package com.lazyhat.novsuapp.ui.activities.main.screens.timetable

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.lazyhat.novsuapp.data.models.LessonDay

@Composable
fun DayPage(day: LessonDay) {
    LazyColumn() {
        items(day.lessons) {
            LessonCard(it)
        }
    }
}