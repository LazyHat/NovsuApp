package com.lazyhat.novsuapp.navigation

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lazyhat.novsuapp.screens.EGPage
import com.lazyhat.novsuapp.screens.SettingsPage
import com.lazyhat.novsuapp.screens.TTPage
import com.lazyhat.novsuapp.util.AppModalDrawer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NovsuNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    startDestination: String = Pages.TimeTable.NavRoute,
    navActions: NavigationActions = remember(navController) { NavigationActions(navController) }
) {
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: startDestination
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Pages.TimeTable.NavRoute) {
            AppModalDrawer(
                drawerState = drawerState,
                currentRoute = currentRoute,
                navigationActions = navActions
            ) {
                TTPage(openDrawer = { coroutineScope.launch { drawerState.open() } })
            }
        }
        composable(Pages.EditGroup.NavRoute) {
            AppModalDrawer(
                drawerState = drawerState,
                currentRoute = currentRoute,
                navigationActions = navActions
            ) {
                EGPage(
                    openDrawer = {coroutineScope.launch { drawerState.open() } }, onResult = {navActions.navigateToTimeTable()})
            }
        }
        composable(Pages.Settings.NavRoute) {
            AppModalDrawer(
                drawerState = drawerState,
                currentRoute = currentRoute,
                navigationActions = navActions
            ) {
                SettingsPage(openDrawer = { coroutineScope.launch { drawerState.open() } })
            }
        }
    }
}