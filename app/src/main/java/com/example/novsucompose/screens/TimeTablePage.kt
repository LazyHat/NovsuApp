package com.example.novsucompose.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.novsucompose.R
import com.example.novsucompose.data.*
import com.example.novsucompose.viewmodels.TimeTableViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TimeTablePage(model: MainModel) {
    val viewModel = TimeTableViewModel(model)
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(bottomBar = {
        TabsBar(
            currentIndex = pagerState.currentPage,
            countOfDays = uiState.mainModel.days.size,
            updateTab = {
                scope.launch {
                    pagerState.animateScrollToPage(it)
                }
            }
        )
    },
        floatingActionButton = {
            WeekGroupFab(currentWeek = uiState.sort) { updatedWeek ->
                viewModel.updateSort(updatedWeek)
            }
        }) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            HorizontalPager(
                pageCount = uiState.mainModel.days.size,
                state = pagerState,
                modifier = Modifier
                    .fillMaxHeight(0.94F)
                    .fillMaxWidth()
            ) { page ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(5.dp)
                ) {
                    items(viewModel.getLessons(page)) {
                        LessonCard(model = it)
                    }
                }
                else if (error != ErrorCodes.OK) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "Error. msg: ${error.msg}",
                        style = TextStyle(fontSize = 20.sp)
                    )
                }
            } else if (mainModel.value.group == "") {
                var loadingS by remember { mutableStateOf("") }
                Box(
                    modifier = Modifier.fillMaxSize(), contentAlignment = Center
                ) {
                    Text(
                        text = "${stringResource(id = R.string.loading)}$loadingS",
                        style = TextStyle(fontSize = 20.sp)
                    )
                }
                LaunchedEffect(key1 = loadingS, block = {
                    if (loadingS == "....") loadingS = ""
                    delay(300L)
                    loadingS += "."
                })
            }
            }
        }
    }
}

@Composable
fun WeekGroupFab(currentWeek: Week, update: (updatedWeek: Week) -> Unit) {
    FloatingActionButton(
        onClick = {
            update(
                when (currentWeek) {
                    Week.All -> Week.Upper
                    Week.Upper -> Week.Lower
                    Week.Lower -> Week.All
                }
            )
        }, shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            when (val s = stringResource(id = currentWeek.labelRes)) {
                "" -> stringResource(id = R.string.tt_week_all)
                else -> s
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
        for (it in 0..countOfDays) {
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
            CardStroke(
                text = model.week.label,
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