package com.example.novsucompose.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.novsucompose.R
import com.example.novsucompose.data.Request
import com.example.novsucompose.data.getGroupsList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun EditGroupPage(requestState: MutableState<Request>, selectedNavItem: MutableState<Int>) {
    val institutes = stringArrayResource(id = R.array.eg_institutes)
    val instituteIds = stringArrayResource(id = R.array.eg_institutes_id)
    val institutelabels = stringArrayResource(id = R.array.eg_institutes_label)
    val scope = rememberCoroutineScope()
    var grade by remember { mutableStateOf("1") }
    var group by remember { mutableStateOf(requestState.value.group) }
    var groupsList by remember { mutableStateOf(listOf<String>()) }
    var subGroup by remember { mutableStateOf("") }
    var institute by remember { mutableStateOf(institutes[instituteIds.indexOf(requestState.value.instituteId)]) }
    var buttonVisibility by remember { mutableStateOf(false) }

    Scaffold(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (buttonVisibility)
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 10.dp),
                    content = { Text(stringResource(id = R.string.eg_apply)) },
                    onClick = {
                        requestState.value =
                            Request(instituteIds[institutes.indexOf(institute)], group, subGroup)
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
                        institutes.toList(),
                        institute,
                        stringResource(id = R.string.eg_institute),
                    ) { updatedItem ->
                        institute = updatedItem
                    }
                }
                item {
                    ListTextBox(
                        listOf("1", "2", "3", "4", "5", "6"),
                        grade,
                        stringResource(id = R.string.eg_grade),
                    ) { updatedItem ->
                        grade = updatedItem
                    }
                }
                item {
                    val context = LocalContext.current
                    LaunchedEffect(key1 = institute, key2 = grade) {
                        scope.launch(Dispatchers.IO) {
                            groupsList = listOf(
                                "${context.resources.getString(R.string.loading)}..."
                            )
                            groupsList =
                                getGroupsList(
                                    grade,
                                    institutelabels[institutes.indexOf(institute)]
                                ).toList()
                            if (!groupsList.contains(group)) group = ""
                        }
                    }
                    ListTextBox(
                        items = groupsList,
                        selectedItem = group,
                        label = stringResource(id = R.string.eg_group)
                    ) { updatedItem ->
                        group = updatedItem
                        buttonVisibility = true
                    }
                }
                item {
                    ListTextBox(
                        items = listOf("", "1", "2", "3"),
                        selectedItem = subGroup,
                        label = stringResource(id = R.string.tt_eg_subgroup)
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