package com.lazyhat.novsuapp.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.lazyhat.novsuapp.R
import com.lazyhat.novsuapp.data.*
import com.lazyhat.novsuapp.screens.timetable.EmptyDay
import com.lazyhat.novsuapp.screens.timetable.EmptyWeek
import com.lazyhat.novsuapp.screens.timetable.TabsBar
import com.lazyhat.novsuapp.screens.timetable.WeekGroupFab
import com.lazyhat.novsuapp.screens.timetable.cardsview.DayColumnCards
import com.lazyhat.novsuapp.ui.theme.NovsuTheme
import com.lazyhat.novsuapp.viewmodels.TTEvent
import com.lazyhat.novsuapp.viewmodels.TTViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun TTPage(
    viewModel: TTViewModel = hiltViewModel(),
    openDrawer: () -> Unit,
    scope: CoroutineScope = rememberCoroutineScope(),
) {
    val uiState by viewModel.uiState.collectAsState()
    // val tableUiState by viewModel.tableUiState.collectAsState()
    val pagerState = rememberPagerState()
    val pullRefreshState =
        rememberPullRefreshState(
            refreshing = uiState.isLoading,
            onRefresh = { viewModel.createEvent(TTEvent.Refresh) })


    Scaffold(
        topBar = { TopBar(openDrawer, label = stringResource(id = R.string.timetable_label)) },
        bottomBar = {
            if (uiState.isNormal())
                TabsBar(
                    pagerState.currentPage,
                    uiState.days.size
                ) {
                    scope.launch {
                        pagerState.animateScrollToPage(it)
                    }
                }
        },
        floatingActionButton = {
            if (uiState.isNormal())
                WeekGroupFab(uiState.weekFilter, uiState.isCurrentWeek) {
                    viewModel.createEvent(TTEvent.ChangeSort)
                }
        },
        containerColor = MaterialTheme.colorScheme.surface,
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
                .padding(padding)
        ) {
            if (uiState.isError || uiState.isLoading) {
                LoadingPage(uiState.error)
            } else if (uiState.isEmpty) {
                EmptyWeek()
            } else {
                LaunchedEffect(key1 = uiState.isNormal()) {
                    scope.launch {
                        if (uiState.days.size - 1 >= uiState.dayOfWeek)
                            pagerState.scrollToPage(uiState.dayOfWeek)
                        else
                            pagerState.scrollToPage(0)
                    }
                }
                HorizontalPager(
                    pageCount = uiState.days.size,
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxSize()
                ) { page ->
                    if (uiState.days.size - 1 >= page)
                        with(uiState.days[page]) {
                            if (lessons.isEmpty()) {
                                EmptyDay()
                            } else
                                DayColumnCards(lessons)
                            //Table(tableUiState, page)
                        }
                }
            }
            PullRefreshIndicator(
                refreshing = uiState.isLoading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                backgroundColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onTertiary
            )
        }
    }
}


@Preview(backgroundColor = 0xFF333333)
@Composable
fun CardsPreview() {
    val lesson = LessonModel(
        Time("17.00", "18.45", 2),
        "Mathematics",
        "(lection)",
        "2",
        "1242",
        "Teacher Teacher Teacher",
        Week.Lower,
        "description"
    )
    val list = mutableListOf<LessonModel>()
    for (i in 0..3) {
        list.add(lesson)
    }
    NovsuTheme(DataSource.ColorSchemes.Pink.scheme) {
        DayColumnCards(lessons = list)
    }
}