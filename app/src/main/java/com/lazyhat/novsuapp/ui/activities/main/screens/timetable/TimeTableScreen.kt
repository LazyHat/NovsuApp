package com.lazyhat.novsuapp.ui.activities.main.screens.timetable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lazyhat.novsuapp.data.fake.FakeData

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TimeTableScreen() {
    val days = FakeData.days
    val pagerState = rememberPagerState(0) { days.size }
    Scaffold(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(modifier = Modifier.fillMaxSize().padding(it), state = pagerState) { page ->
            days[page].also { it1 ->
                // if (it1 != null)
            }?.let { it2 ->
                DayPage(day = it2)
            }
        }
    }
}