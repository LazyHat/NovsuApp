package com.example.novsucompose.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.novsucompose.data.Group
import com.example.novsucompose.data.Institute
import com.example.novsucompose.data.getGroupsList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun EditGroupPage(groupState: MutableState<Group>, selectedNavItem: MutableState<Int>) {
    val scope = rememberCoroutineScope()
    var grade by remember { mutableStateOf("1") }
    var group by remember { mutableStateOf(groupState.value.group) }
    var groupsList by remember { mutableStateOf(listOf<String>()) }
    var subGroup by remember { mutableStateOf("") }
    var institute by remember { mutableStateOf(groupState.value.institute.label) }
    var buttonVisibility by remember { mutableStateOf(false) }

    Scaffold(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (buttonVisibility)
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 10.dp),
                    content = { Text("Подтвердить") },
                    onClick = {
                        groupState.value =
                            Group(Institute.find(institute), group, subGroup)
                        buttonVisibility = false
                        selectedNavItem.value = 0
                    })
            LazyColumn(
                modifier = Modifier
                    .padding(it),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                item {
                    ListTextBox(
                        Institute.values().map { inst -> inst.label },
                        institute,
                        "Институт",
                    ) { updatedItem ->
                        institute = updatedItem
                    }
                }
                item {
                    ListTextBox(
                        listOf("1", "2", "3", "4", "5", "6"),
                        grade,
                        "Курс",
                    ) { updatedItem ->
                        grade = updatedItem
                    }
                }
                item {
                    LaunchedEffect(key1 = institute, key2 = grade) {
                        scope.launch(Dispatchers.IO) {
                            groupsList = listOf("Загрузка...")
                            groupsList =
                                getGroupsList(
                                    grade,
                                    Institute.find(institute)
                                ).toList()
                            if (!groupsList.contains(group)) group = ""
                        }
                    }
                    ListTextBox(
                        items = groupsList,
                        selectedItem = group,
                        label = "Группа"
                    ) { updatedItem ->
                        group = updatedItem
                        buttonVisibility = true
                    }
                }
                item {
                    ListTextBox(
                        items = listOf("", "1", "2", "3"),
                        selectedItem = subGroup,
                        label = "Подгруппа"
                    ) { updatedItem ->
                        subGroup = updatedItem
                        buttonVisibility = true
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListTextBox(
    items: List<String>,
    selectedItem: String,
    label: String,
    update: (updatedItem: String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp),
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            value = selectedItem.ifEmpty { "Не указано" },
            readOnly = true,
            onValueChange = {},
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) })
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            items.forEach {
                DropdownMenuItem(
                    text = { Text(it.ifEmpty { "Не указано" }) }, onClick = {
                        expanded = false
                        update(it)
                    })
            }
        }
    }
}