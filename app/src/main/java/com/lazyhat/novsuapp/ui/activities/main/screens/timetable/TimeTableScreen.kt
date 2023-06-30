package com.lazyhat.novsuapp.ui.activities.main.screens.timetable

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.lazyhat.novsuapp.data.fake.FakeData

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TimeTableScreen(
    vm: TimeTableVM = hiltViewModel()
) {
    val pagerState = rememberPagerState(0) { days.size }
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("NovsuCompose") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }) {
        HorizontalPager(
            modifier = Modifier
                .fillMaxSize()
                .padding(it), state = pagerState
        ) { page ->
            days[page].also { it1 ->
                // if (it1 != null)
            }?.let { it2 ->
                DayPage(day = it2)
            }
        }
    }
}