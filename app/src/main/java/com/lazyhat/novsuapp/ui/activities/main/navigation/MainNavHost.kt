package com.lazyhat.novsuapp.ui.activities.main.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lazyhat.novsuapp.ui.activities.main.screens.timetable.TimeTableScreen

@Composable
fun MainNavHost(
    startDestination: Destinations = Destinations.TimeTable,
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = startDestination.name) {
        composable(Destinations.TimeTable.name) {
            TimeTableScreen()
        }
    }
}