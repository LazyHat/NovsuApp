package com.lazyhat.novsuapp.navigation

import androidx.navigation.NavHostController
import com.lazyhat.novsuapp.R

enum class Pages(val NavRoute: String, val labelRes: Int) {
    TimeTable("tt", R.string.timetable_label),
    EditGroup("eg", R.string.editgroup_label),
    Settings("ss", R.string.settings_label)
}

class NavigationActions(private val navController: NavHostController) {
    fun navigateToTimeTable() {
        navController.navigate(Pages.TimeTable.NavRoute)
    }

    fun navigateToEditGroup() {
        navController.navigate(Pages.EditGroup.NavRoute)
    }

    fun navigateToSettings() {
        navController.navigate(Pages.Settings.NavRoute)
    }
}