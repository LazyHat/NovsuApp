package com.lazyhat.novsuapp.ui.activities.main.screens.timetable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lazyhat.novsuapp.data.models.LessonDay

@Composable
fun DayPage(day: LessonDay) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(day.lessons) {
            if (it != day.lessons.first())
                Divider(Modifier.fillMaxWidth(), 4.dp)
            LessonCard(it)
        }
    }
}