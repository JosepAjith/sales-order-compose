package com.joseph.salesorderapp.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SearchableDropdown(
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T?) -> Unit,
    onSearchQueryChanged: (String) -> Unit,
    itemLabel: (T) -> String,
    label: String,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester = FocusRequester()
) {
    var expanded by remember { mutableStateOf(false) }
    var internalSearchQuery by remember { mutableStateOf("") }

    LaunchedEffect(selectedItem) {
        if (selectedItem == null) {
            internalSearchQuery = ""
        } else {
            expanded = false
            internalSearchQuery = itemLabel(selectedItem)
        }
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = internalSearchQuery,
            onValueChange = { query ->
                internalSearchQuery = query
                onSearchQueryChanged(query)
                if (!expanded) expanded = true
            },
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .focusRequester(focusRequester),
            singleLine = true
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(itemLabel(item)) },
                    onClick = {
                        onItemSelected(item)
                    }
                )
            }
        }
    }
}





