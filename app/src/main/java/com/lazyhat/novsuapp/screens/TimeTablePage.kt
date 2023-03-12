package com.lazyhat.novsuapp.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lazyhat.novsuapp.R
import com.lazyhat.novsuapp.data.*
import com.lazyhat.novsuapp.viewmodels.TimeTableViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TimeTablePage(
    groupSpecs: GroupSpecs,
    viewModel: TimeTableViewModel = TimeTableViewModel(groupSpecs)
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(bottomBar = {
        if (uiState.weekModel.days.isNotEmpty())
            TabsBar(
                currentIndex = pagerState.currentPage,
                countOfDays = uiState.weekModel.days.size,
                updateTab = {
                    scope.launch {
                        pagerState.animateScrollToPage(it)
                    }
                }
            )
    },
        floatingActionButton = {
            if (uiState.weekModel.days.isNotEmpty())
                WeekGroupFab(currentWeek = uiState.sort) { updatedWeek ->
                    viewModel.updateSort(updatedWeek)
                }
        }) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (uiState.weekModel.days.isEmpty()) {
                LoadingPage()
            } else
                HorizontalPager(
                    pageCount = uiState.weekModel.days.size,
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxHeight(0.94F)
                        .fillMaxWidth()
                ) { page ->
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(5.dp)
                    ) {
                        with(uiState.weekModel.days[page].lessons) {
                            if (this.isNotEmpty())
                                items(this) {
                                    LessonCard(model = it)
                                }
                        }
                    }
                }
        }
    }
}

@Composable
fun WeekGroupFab(currentWeek: Week?, update: (updatedWeek: Week?) -> Unit) {
    FloatingActionButton(
        onClick = {
            update(
                when (currentWeek) {
                    null -> Week.Upper
                    Week.Upper -> Week.Lower
                    Week.Lower -> null
                }
            )
        }, shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            when (val id = currentWeek?.labelRes) {
                null -> stringResource(id = R.string.tt_week_all)
                else -> stringResource(id = id)
            }
        )
    }
}

@Composable
fun TabsBar(currentIndex: Int, countOfDays: Int, updateTab: (updatedIndex: Int) -> Unit) {
    TabRow(
        selectedTabIndex = currentIndex,
        containerColor = Color.Transparent,
        contentColor = Color.White,
    ) {
        val tabItems = stringArrayResource(id = R.array.tt_days)
        for (it in 0 until countOfDays) {
            Tab(modifier = Modifier.padding(10.dp),
                selected = it == currentIndex,
                content = { Text(tabItems[it]) },
                onClick = {
                    updateTab(it)
                })
        }
    }
}

@Composable
fun LessonCard(model: LessonModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp, bottom = 5.dp),
        border = BorderStroke(1.dp, Color.Blue),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),

        ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        ) {
            Row {
                CardStroke(model.lessonType, R.drawable.ic_type)
                CardStroke(model.lessonName, R.drawable.ic_lessonname)
            }
            CardStroke(
                "${model.time.getString()} ${
                    when (model.time.hours) {
                        0 -> ""
                        1 -> stringResource(id = R.string.tt_time_one_hour)
                        else -> stringResource(id = R.string.tt_time_few_hours)
                    }
                }", R.drawable.ic_schedule
            )
            CardStroke(
                model.subGroup,
                R.drawable.ic_group,
                stringResource(id = R.string.tt_eg_subgroup)
            )
            CardStroke(model.teacher, R.drawable.ic_teacher)
            CardStroke(
                text = model.auditorium,
                iconResId = R.drawable.ic_auditorium,
                stringResource(R.string.tt_auditorium)
            )
            if (model.week != null)
                CardStroke(
                    text = stringResource(id = model.week.labelRes),
                    iconResId = R.drawable.ic_week,
                    stringResource(id = R.string.tt_week)
                )
            CardStroke(text = model.description, iconResId = R.drawable.ic_description)
        }
    }
}

@Composable
fun CardStroke(text: String, iconResId: Int, subtext: String = "") {
    if (text.isNotEmpty()) {
        Row {
            Icon(
                painter = painterResource(id = iconResId), contentDescription = "icLessonDesc"
            )
            Text(
                text = "${if (subtext.isEmpty()) "" else "$subtext: "}$text",
                textAlign = TextAlign.Start
            )
        }
    }
}