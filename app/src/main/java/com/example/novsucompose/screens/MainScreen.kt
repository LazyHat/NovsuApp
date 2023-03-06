package com.example.novsucompose.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.novsucompose.R
import com.example.novsucompose.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(drawerState: DrawerState, scope: CoroutineScope) {
    TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) },
        navigationIcon = {
            Icon(
                imageVector = Icons.Filled.Menu,
                "icMenu",
                modifier = Modifier
                    .scale(1.5F)
                    .padding(10.dp)
                    .clickable {
                        scope.launch { drawerState.open() }
                    })
        })
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val navItems = listOf("Расписание", "Изменить группу", "Настройки")
    val selectedNavItem = remember { mutableStateOf(0) }
    val group = remember { mutableStateOf(DEFAULT_GROUP) }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        onClick = { scope.launch { drawerState.close() } },
                        modifier = Modifier
                            .padding(NavigationDrawerItemDefaults.ItemPadding)
                            .padding(top = 5.dp),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(15.dp),
                        content = { Icon(Icons.Default.Close, "icClose") }
                    )
                    Text(
                        "Меню", style = TextStyle(fontSize = 20.sp)
                    )
                }
                Spacer(Modifier.padding(10.dp))
                navItems.forEachIndexed() { index, item ->
                    NavigationDrawerItem(
                        label = { Text(item) },
                        selected = index == selectedNavItem.value,
                        modifier = Modifier.padding(
                            NavigationDrawerItemDefaults.ItemPadding
                        ),
                        onClick = {
                            scope.launch { drawerState.close() }
                            selectedNavItem.value = index
                        },
                    )
                }
            }
        },
        drawerState = drawerState
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopBar(
                drawerState = drawerState,
                scope = scope
            )
            NavHost(
                navController = navController,
                startDestination = navItems[selectedNavItem.value]
            ) {
                composable(navItems[0]) { TimeTablePage(group.value) }
                composable(navItems[1]) { EditGroupPage(group, selectedNavItem) }
                composable(navItems[2]) { SettingsPage() }
            }
        }
    }
}