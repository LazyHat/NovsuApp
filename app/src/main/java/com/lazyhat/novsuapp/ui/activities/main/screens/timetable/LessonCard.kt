package com.lazyhat.novsuapp.ui.activities.main.screens.timetable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.lazyhat.novsuapp.data.models.Lesson

@Composable
fun LessonCard(lesson: Lesson) {
    val context = LocalContext.current
    Card {
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = lesson.name)
            Text(text = lesson.type.toString(context))
        }
    }
}