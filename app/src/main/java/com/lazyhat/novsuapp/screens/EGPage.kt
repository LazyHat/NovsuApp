package com.lazyhat.novsuapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lazyhat.novsuapp.R
import com.lazyhat.novsuapp.data.*
import com.lazyhat.novsuapp.viewmodels.EGEvent
import com.lazyhat.novsuapp.viewmodels.EGViewModel


@Composable
fun EGPage(
    viewModel: EGViewModel = hiltViewModel(),
    openDrawer: () -> Unit,
    onResult: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    Scaffold(
        topBar = { TopBar(openDrawer) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(it),
        ) {
            ListTextBoxGrid(
                items = viewModel.getInstitutes(context),
                selectedItem = stringResource(id = uiState.groupSpecs.institute.labelRes),
                label = stringResource(id = R.string.eg_institute)
            ) { _, index ->
                viewModel.createEvent(EGEvent.UpdateInstitute(index))
            }
            ListTextBoxGrid(
                items = viewModel.getGrades(),
                selectedItem = uiState.groupSpecs.grade,
                label = stringResource(id = R.string.eg_grade)
            ) { item, _ ->
                viewModel.createEvent(EGEvent.UpdateGrade(item))
            }
            ListTextBoxGrid(
                items = if (uiState.isGroupsListLoading) listOf() else uiState.groupsList,
                selectedItem = if (uiState.isGroupsListLoading) stringResource(id = R.string.loading) else uiState.groupSpecs.group,
                label = stringResource(id = R.string.eg_group)
            ) { updatedItem, _ ->
                viewModel.createEvent(EGEvent.UpdateGroup(updatedItem))
            }
            Button(
                enabled = uiState.buttonEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                shape = RoundedCornerShape(5.dp),
                content = { Text(stringResource(id = R.string.eg_apply)) },
                onClick = {
                    viewModel.createEvent(EGEvent.OnResult)
                    onResult()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.onSurface,
                    disabledContentColor = MaterialTheme.colorScheme.surface
                )
            )
            Divider(
                color = MaterialTheme.colorScheme.surface,
                thickness = 2.dp,
                modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = uiState.ifSubgroupEntered,
                    onCheckedChange = { viewModel.createEvent(EGEvent.SubGroupEnableChanged) },
                    colors = CheckboxDefaults.colors(
                        uncheckedColor = MaterialTheme.colorScheme.primary,
                        checkedColor = MaterialTheme.colorScheme.primary,
                    )
                )
                Text(text = stringResource(id = R.string.eg_enter_subgroup))
            }
            if (uiState.ifSubgroupEntered)
                ListTextBoxGrid(
                    items = DataSource.subGroupsList,
                    selectedItem = uiState.groupSpecs.subGroup,
                    label = stringResource(id = R.string.tt_eg_subgroup)
                ) { updatedItem, _ ->
                    viewModel.createEvent(EGEvent.UpdateSubGroup(updatedItem))
                }
            Divider(
                color = MaterialTheme.colorScheme.surface,
                thickness = 2.dp,
                modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
            )
            Text(text = stringResource(id = R.string.eg_description))
        }
    }
}