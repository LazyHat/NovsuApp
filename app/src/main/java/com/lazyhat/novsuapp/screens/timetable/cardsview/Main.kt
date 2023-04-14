package com.lazyhat.novsuapp.screens.timetable.cardsview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazyhat.novsuapp.R
import com.lazyhat.novsuapp.data.LessonModel
import com.lazyhat.novsuapp.data.Week

@Composable
fun DayColumnCards(lessons: List<LessonModel>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(5.dp)
    ) {
        with(lessons) {
            if (this.isNotEmpty())
                items(this) {
                    LessonCard(model = it)
                }
        }
    }
}

@Composable
fun LessonCard(model: LessonModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp, bottom = 5.dp),
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                CardStroke(
                    text = model.lessonName,
                    modifier = Modifier
                        .fillMaxWidth(0.80f)
                        .padding(bottom = 5.dp),
                    style = TextStyle(fontSize = 21.sp)
                )
                CardStroke(
                    text = model.lessonType,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            if (model.time != null)
                CardStroke(
                    text = "${model.time.getString()} ${
                        pluralStringResource(
                            id = R.plurals.tt_time,
                            count = model.time.hours
                        )
                    }",
                    modifier = Modifier
                        .padding(bottom = 5.dp),
                    style = TextStyle(fontSize = 18.sp),
                    color = MaterialTheme.colorScheme.secondary
                )
            CardStroke(
                text = model.auditorium,
                subtext = stringResource(R.string.tt_auditorium)
            )
            CardStroke(
                text = model.subGroup,
                subtext = stringResource(id = R.string.tt_eg_subgroup)
            )
            CardStroke(text = model.teacher)
            if (model.week != Week.All)
                CardStroke(
                    text = stringResource(id = model.week.labelRes),
                    subtext = stringResource(id = R.string.tt_week)
                )
            LinkedCardStroke(text = model.description)
        }
    }
}