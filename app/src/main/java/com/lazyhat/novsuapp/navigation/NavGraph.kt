package com.lazyhat.novsuapp.navigation

import androidx.compose.animation.*
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.lazyhat.novsuapp.screens.AboutPage
import com.lazyhat.novsuapp.screens.EGPage
import com.lazyhat.novsuapp.screens.SettingsPage
import com.lazyhat.novsuapp.screens.TTPage
import com.lazyhat.novsuapp.util.AppModalDrawer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NovsuNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberAnimatedNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    startDestination: String = Pages.TimeTable.NavRoute,
    navActions: NavigationActions = remember(navController) { NavigationActions(navController) }
) {
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: startDestination
    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        enterTransition = { slideInHorizontally { -it*2 } },
        exitTransition = { slideOutHorizontally { -it*2 } }
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
                    openDrawer = { coroutineScope.launch { drawerState.open() } },
                    onResult = { navActions.navigateToTimeTable() })
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
        composable(Pages.About.NavRoute) {
            AppModalDrawer(
                drawerState = drawerState,
                currentRoute = currentRoute,
                navigationActions = navActions
            ) {
                AboutPage(openDrawer = { coroutineScope.launch { drawerState.open() } })
            }
        }
    }
}