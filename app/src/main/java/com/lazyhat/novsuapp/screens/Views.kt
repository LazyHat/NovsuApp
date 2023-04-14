package com.lazyhat.novsuapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lazyhat.novsuapp.R
import com.lazyhat.novsuapp.data.DataSource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListTextBoxGrid(
    items: List<String>,
    selectedItem: String,
    label: String,
    columns: Int = 5,
    update: (item: String, index: Int) -> Unit
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
            value = selectedItem.ifEmpty { stringResource(id = R.string.eg_not_entered) },
            readOnly = true,
            onValueChange = {},
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                disabledContainerColor = MaterialTheme.colorScheme.background,
                focusedIndicatorColor = MaterialTheme.colorScheme.outline,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.onBackground,
                focusedTrailingIconColor = MaterialTheme.colorScheme.outline,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.onBackground,
                focusedLabelColor = MaterialTheme.colorScheme.outline,
                unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
            )
        )
        ExposedDropdownMenu(
            modifier = Modifier.background(MaterialTheme.colorScheme.onSurface),
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            if (items.size == 1 && items[0].isEmpty())
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = R.string.loading)) }, onClick = {})
            else {
                val rows = items.size / columns
                for (row in 0..rows) Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    for (column in 0 until columns) {
                        val index = column + row * columns
                        if (index in items.indices)
                            OutlinedButton(
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                shape = RoundedCornerShape(5.dp),
                                contentPadding = PaddingValues(
                                    top = 2.dp,
                                    bottom = 2.dp,
                                    start = 2.dp,
                                    end = 2.dp
                                ),
                                onClick = {
                                    expanded = false
                                    update(items[index], index)
                                }) {
                                Text(
                                    text = items[index].ifEmpty { stringResource(id = R.string.eg_not_entered) }
                                )
                            }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListGridColorTheme(
    selectedItem: DataSource.ColorSchemes,
    label: String,
    columns: Int = 5,
    update: (DataSource.ColorSchemes) -> Unit
) {
    val items = DataSource.ColorSchemes.values()
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
            value = stringResource(id = selectedItem.labelRes),
            readOnly = true,
            onValueChange = {},
            label = { Text(label) },
            trailingIcon = {
                Button(
                    modifier = Modifier
                        .clip(CircleShape)
                        .padding(end = 8.dp)
                        .width(40.dp)
                        .height(40.dp),
                    onClick = { expanded = !expanded },
                    colors = ButtonDefaults.buttonColors(containerColor = selectedItem.scheme.primary)
                ) {}
            },
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                disabledContainerColor = MaterialTheme.colorScheme.background,
                focusedIndicatorColor = MaterialTheme.colorScheme.outline,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.onBackground,
                focusedTrailingIconColor = MaterialTheme.colorScheme.outline,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.onBackground,
                focusedLabelColor = MaterialTheme.colorScheme.outline,
                unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
            )
        )
        ExposedDropdownMenu(
            modifier = Modifier.background(MaterialTheme.colorScheme.onSurface),
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            val rows = items.size / columns
            for (row in 0..rows) Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                for (column in 0 until columns) {
                    val index = column + row * columns
                    if (index in items.indices)
                        OutlinedButton(
                            colors = ButtonDefaults.buttonColors(containerColor = items[index].scheme.primary),
                            shape = RoundedCornerShape(5.dp),
                            contentPadding = PaddingValues(
                                top = 2.dp,
                                bottom = 2.dp,
                                start = 2.dp,
                                end = 2.dp
                            ),
                            onClick = {
                                expanded = false
                                update(items[index])
                            }) {}
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(openDrawer: () -> Unit, label: String) {
    TopAppBar(
        title = { Text(text = label) },
        navigationIcon = {
            Icon(
                imageVector = Icons.Filled.Menu,
                "icMenu",
                modifier = Modifier
                    .scale(1.5F)
                    .padding(10.dp)
                    .clickable {
                        openDrawer()
                    })
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        )
    )
}