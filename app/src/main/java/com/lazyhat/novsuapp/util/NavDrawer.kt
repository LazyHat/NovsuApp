package com.lazyhat.novsuapp.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lazyhat.novsuapp.R
import com.lazyhat.novsuapp.data.DataSource
import com.lazyhat.novsuapp.navigation.NavigationActions
import com.lazyhat.novsuapp.navigation.Pages
import com.lazyhat.novsuapp.ui.theme.NovsuTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AppModalDrawer(
    drawerState: DrawerState,
    currentRoute: String,
    navigationActions: NavigationActions,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                currentRoute = currentRoute,
                navigateToTimeTable = { navigationActions.navigateToTimeTable() },
                navigateToEditGroup = { navigationActions.navigateToEditGroup() },
                navigateToSettings = { navigationActions.navigateToSettings() },
                navigateToAbout = { navigationActions.navigateToAbout() },
                closeDrawer = { coroutineScope.launch { drawerState.close() } }
            )
        }
    ) {
        content()
    }
}

@Composable
private fun AppDrawer(
    currentRoute: String,
    navigateToTimeTable: () -> Unit,
    navigateToEditGroup: () -> Unit,
    navigateToSettings: () -> Unit,
    navigateToAbout: () -> Unit,
    closeDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth(0.7F)
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            DrawerHeader(stringResource(id = R.string.menu))
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp),
                color = MaterialTheme.colorScheme.surface
            )
            Spacer(Modifier.height(100.dp))
            DrawerButton(
                label = stringResource(id = Pages.TimeTable.labelRes),
                isSelected = currentRoute == Pages.TimeTable.NavRoute,
                onClick = {
                    navigateToTimeTable()
                    closeDrawer()
                }
            )
            DrawerButton(
                label = stringResource(id = Pages.EditGroup.labelRes),
                isSelected = currentRoute == Pages.EditGroup.NavRoute,
                onClick = {
                    navigateToEditGroup()
                    closeDrawer()
                }
            )
            DrawerButton(
                label = stringResource(id = Pages.Settings.labelRes),
                isSelected = currentRoute == Pages.Settings.NavRoute,
                onClick = {
                    navigateToSettings()
                    closeDrawer()
                }
            )
        }
        DrawerButton(
            label = stringResource(id = Pages.About.labelRes),
            isSelected = currentRoute == Pages.About.NavRoute,
            onClick = {
                navigateToAbout()
                closeDrawer()
            }
        )
    }
}


@Composable
private fun DrawerButton(
    isSelected: Boolean,
    label: String,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = { Text(text = label) },
        modifier = Modifier
            .fillMaxWidth(0.8F)
            .padding(top = 5.dp, bottom = 5.dp),
        onClick = onClick,
        selected = isSelected,
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = MaterialTheme.colorScheme.surface,
            selectedTextColor = MaterialTheme.colorScheme.onBackground,
            unselectedTextColor = MaterialTheme.colorScheme.onBackground,
            unselectedContainerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
fun DrawerHeader(label: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        textAlign = TextAlign.Center,
        text = label,
        color = MaterialTheme.colorScheme.onBackground,
        style = TextStyle(fontSize = 20.sp)
    )
}

@Preview
@Composable
fun DrawerPreview() {
    NovsuTheme(DataSource.ColorSchemes.Default.scheme) {
        AppDrawer(
            currentRoute = Pages.TimeTable.NavRoute,
            navigateToTimeTable = {},
            navigateToEditGroup = {},
            navigateToSettings = {},
            navigateToAbout = {},
            closeDrawer = {})
    }
}