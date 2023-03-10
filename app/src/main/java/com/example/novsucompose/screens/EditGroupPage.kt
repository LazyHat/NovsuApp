package com.example.novsucompose.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.novsucompose.R
import com.example.novsucompose.data.DataSource
import com.example.novsucompose.data.GroupSpecs
import com.example.novsucompose.data.Institute
import com.example.novsucompose.viewmodels.EditGroupViewModel


@Composable
fun EditGroupPage(groupSpecs: GroupSpecs, onResult: (GroupSpecs) -> Unit) {
    val viewModel = EditGroupViewModel(groupSpecs)
    val uiState by viewModel.uiState.collectAsState()
    var buttonEnabled by remember { mutableStateOf(false) }

    Scaffold(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Button(
                enabled = buttonEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 10.dp),
                content = { Text(stringResource(id = R.string.eg_apply)) },
                onClick = {
                    onResult(uiState.copy(groupList = listOf()))
                })
            LazyColumn(
                modifier = Modifier
                    .padding(it),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                item {
                    ListTextBox(
                        Institute.values().toList().map { stringResource(id = it.labelRes) },
                        stringResource(id = uiState.institute.labelRes),
                        stringResource(id = R.string.eg_institute),
                    ) { _, index ->
                        viewModel.updateInstituteAndGrade(Institute.values()[index])
                    }
                }
                item {
                    ListTextBox(
                        DataSource.gradesList,
                        uiState.grade,
                        stringResource(id = R.string.eg_grade),
                    ) { updatedItem, _ ->
                        viewModel.updateInstituteAndGrade(newGrade = updatedItem)
                    }
                }
                item {
                    ListTextBox(
                        items = uiState.groupList,
                        selectedItem = uiState.group,
                        label = stringResource(id = R.string.eg_group)
                    ) { updatedItem, _ ->
                        viewModel.updateGroup(updatedItem)
                        buttonEnabled = true
                    }
                }
                item {
                    ListTextBox(
                        items = DataSource.subGroupsList,
                        selectedItem = uiState.subGroup,
                        label = stringResource(id = R.string.tt_eg_subgroup)
                    ) { updatedItem, _ ->
                        viewModel.updateSubGroup(updatedItem)
                        buttonEnabled = true
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
    update: (Item: String, index: Int) -> Unit
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
            items.forEachIndexed { index, it ->
                DropdownMenuItem(
                    text = { Text(it.ifEmpty { "Не указано" }) }, onClick = {
                        expanded = false
                        update(it, index)
                    })
            }
        }
    }
}