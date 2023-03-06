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
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.novsucompose.R
import com.example.novsucompose.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TimeTablePage(request: Request) {
    val pagerState = rememberPagerState()
    var fabState by remember { mutableStateOf(Week.All) }
    val scope = rememberCoroutineScope()
    val mainModel = remember { mutableStateOf(EMPTY_MODEL) }
    var error by remember { mutableStateOf(ErrorCodes.OK) }
    LaunchedEffect(key1 = request, block = {
        scope.launch(Dispatchers.IO) {
            error = getData(request, mainModel)
        }
    })
    Scaffold(bottomBar = { TabsBar(pagerState = pagerState, model = mainModel) },
        floatingActionButton = {
            WeekGroupFab(state = fabState) { updatedWeek ->
                fabState = updatedWeek
            }
        }) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            HorizontalPager(
                pageCount = if (error != ErrorCodes.OK) 1 else mainModel.value.days.size,
                state = pagerState,
                modifier = Modifier
                    .fillMaxHeight(0.94F)
                    .fillMaxWidth()
            ) { page ->
                if (mainModel.value.group != "" && error == ErrorCodes.OK) LazyColumn(
                    modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(5.dp)
                ) {
                    items(mainModel.value.days[page].lessons.filter { lessonModel ->
                        fabState == Week.All || lessonModel.week == Week.All || lessonModel.week == fabState
                    }) {
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
fun WeekGroupFab(state: Week, update: (updatedWeek: Week) -> Unit) {
    FloatingActionButton(
        onClick = {
            update(
                when (state) {
                    Week.All -> Week.Upper
                    Week.Upper -> Week.Lower
                    Week.Lower -> Week.All
                }
            )
        }, shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            when (val s = state.label) {
                "" -> stringResource(id = R.string.tt_week_all)
                else -> s
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabsBar(pagerState: PagerState, model: MutableState<MainModel>) {
    val scope = rememberCoroutineScope()
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        containerColor = Color.Transparent,
        contentColor = Color.White,
    ) {
        model.value.days.forEachIndexed { index, tmodel ->
            Tab(modifier = Modifier.padding(10.dp),
                selected = index == pagerState.currentPage,
                content = { Text(tmodel.dayName) },
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
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
            CardStroke(model.time.getString(), R.drawable.ic_schedule)
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