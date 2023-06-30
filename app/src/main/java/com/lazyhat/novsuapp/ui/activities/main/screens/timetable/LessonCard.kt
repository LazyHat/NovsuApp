package com.lazyhat.novsuapp.ui.activities.main.screens.timetable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazyhat.novsuapp.R
import com.lazyhat.novsuapp.data.models.Lesson
import com.lazyhat.novsuapp.data.models.LessonWeek

@Composable
fun LessonCard(lesson: Lesson) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = lesson.name,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.weight(0.7f)
                )
                lesson.type?.let { Text(text = it.toString(context), maxLines = 1) }
            }
            Spacer(Modifier.height(5.dp))
            lesson.time?.let {
                Text(
                    modifier = Modifier.padding(bottom = 3.dp),
                    text = it.toString(context),
                    style = TextStyle(fontSize = 20.sp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            lesson.auditorium?.let { Text(text = it) }
            lesson.teacher?.let { Text(text = it) }
            lesson.week.takeIf { it != LessonWeek.All }
                ?.let { Text(text = "${stringResource(id = R.string.tt_week)}: ${it.name}") }
            lesson.description?.let { Text(text = it) }
        }
    }
}