package com.lazyhat.novsuapp.screens.timetable

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazyhat.novsuapp.R
import com.lazyhat.novsuapp.data.Week

@Composable
fun EmptyDay() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(id = R.string.tt_empty_day),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun EmptyWeek() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(id = R.string.tt_empty_week),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun TabsBar(currentIndex: Int, countOfDays: Int, updateTab: (updatedIndex: Int) -> Unit) {
    TabRow(
        selectedTabIndex = currentIndex,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        indicator = { tabPositions ->
            Box(
                modifier = Modifier
                    .tabIndicatorOffset(tabPositions[currentIndex])
                    .height(4.dp)
                    .padding(horizontal = 10.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(color = MaterialTheme.colorScheme.onTertiary)
            )
        }
    ) {
        val tabTitles = stringArrayResource(id = R.array.tt_days)
        for (index in 0 until countOfDays)
            Tab(
                modifier = Modifier.height(40.dp),
                selected = index == currentIndex,
                content = { Text(tabTitles[index]) },
                onClick = {
                    updateTab(index)
                },
                unselectedContentColor = MaterialTheme.colorScheme.tertiary,
                selectedContentColor = MaterialTheme.colorScheme.onTertiary,
            )
    }
}

@Composable
fun WeekGroupFab(filter: Week, isCurrentWeek: Boolean, update: () -> Unit) {
    FloatingActionButton(
        modifier = Modifier.fillMaxWidth(0.16F),
        onClick = {
            update()
        }, shape = RoundedCornerShape(20.dp),
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Crossfade(
                targetState = filter,
                animationSpec = tween(200, 0, easing = EaseInOut)
            ) {
                Text(
                    text = stringResource(id = it.labelRes),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            AnimatedVisibility(
                visible = isCurrentWeek,
                enter = expandVertically { -it / 3 },
                exit = shrinkVertically { -it / 3 }
            ) {
                Text(
                    text = stringResource(id = R.string.tt_today),
                    style = TextStyle(fontSize = 12.sp)
                )
            }
        }
    }
}