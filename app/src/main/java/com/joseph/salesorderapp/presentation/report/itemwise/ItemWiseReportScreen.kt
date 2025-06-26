package com.joseph.salesorderapp.presentation.report.itemwise

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.joseph.salesorderapp.ui.component.SearchableDropdown
import java.text.DecimalFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemWiseReportScreen(
    viewModel: ItemWiseViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    val priceFormatter = DecimalFormat("#,##0.000")

    val labelStyle = MaterialTheme.typography.labelMedium.copy(
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    val totalStyle = MaterialTheme.typography.bodyMedium.copy(
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )


    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBar(
                title = { Text("ItemWise Report") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onBackPress() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.printReport() }) {
                        Icon(Icons.Default.Print, contentDescription = "Print")
                    }
                },
                windowInsets = WindowInsets(0)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "From Date",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            OutlinedButton(
                                onClick = { viewModel.showFromDatePicker() },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.DateRange, contentDescription = "From Date")
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(state.fromDateFormatted)
                            }
                        }


                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "To Date",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            OutlinedButton(
                                onClick = { viewModel.showToDatePicker() },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.DateRange, contentDescription = "To Date")
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(state.toDateFormatted)
                            }
                        }

                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SearchableDropdown(
                            items = state.customers,
                            selectedItem = state.selectedCustomer,
                            onItemSelected = viewModel::selectCustomer,
                            onSearchQueryChanged = viewModel::updateCustomerSearch,
                            itemLabel = { it.name },
                            label = "Customer",
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1.6f),
                        )

                        OutlinedButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.4f),
                            shape = RoundedCornerShape(8.dp),
                            onClick = { viewModel.searchItems() }
                        ) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }
                    }
                }
            }
            Text("Products", style = MaterialTheme.typography.titleMedium)
            LazyColumn {
                itemsIndexed(state.itemList) { index, item ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${index + 1}",
                                modifier = Modifier.width(24.dp),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.productName,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    maxLines = 1
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        "Code: ${item.productCode}",
                                        style = labelStyle
                                    )
                                    Text("Qty: ${item.totalQty}", style = labelStyle)
                                    Text(
                                        "Total: ${priceFormatter.format(item.totalAmount)}",
                                        style = totalStyle
                                    )
                                }
                            }
                        }

                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}