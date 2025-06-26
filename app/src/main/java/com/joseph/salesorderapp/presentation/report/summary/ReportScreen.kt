package com.joseph.salesorderapp.presentation.report.summary

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.DecimalFormat
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    viewModel: ReportViewModel = hiltViewModel(),
) {
    val priceFormatter = DecimalFormat("#,##0.000")
    val state by viewModel.uiState.collectAsState()

    val labelStyle =
        MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
    val totalStyle = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)


    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBar(
                title = { Text("Order Report") },
                navigationIcon = {
                    IconButton(onClick = viewModel::onBackPress) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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

                    OutlinedButton(
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        onClick = { viewModel.searchOrders() }
                    ) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                        Spacer(Modifier.width(4.dp))
                        Text("Search")
                    }
                }
            }

            LazyColumn {
                itemsIndexed(state.allOrders) { index, order ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.onOrderClicked(order.id) }
                            .padding(vertical = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Serial Number
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

                            // Order Summary Details
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = order.orderID,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    ),
                                    maxLines = 1
                                )
                                Text(
                                    text = order.customerName,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    ),
                                    maxLines = 1
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Date: ${order.orderDate}",
                                        style = labelStyle,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = "Amount: ${priceFormatter.format(order.totalAmount)}",
                                        style = totalStyle,
                                        modifier = Modifier.weight(1f),
                                        textAlign = TextAlign.End
                                    )
                                }
                            }
                        }

                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
            }

        }
    }
}

