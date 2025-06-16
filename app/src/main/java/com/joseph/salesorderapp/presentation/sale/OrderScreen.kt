package com.joseph.salesorderapp.presentation.sale

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.joseph.salesorderapp.ui.component.SearchableDropdown
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import java.text.DecimalFormat

@Composable
fun OrderScreen(viewModel: OrderViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    val priceFormatter = DecimalFormat("#,##0.00")
    val paymentModes = listOf("Cash", "Card", "Credit")

    val productFocusRequester = remember { FocusRequester() }
    val quantityFocusRequester = remember { FocusRequester() }

    val requestFocus by viewModel.requestProductFocus.collectAsState()
    val requestQuantityFocus by viewModel.requestQuantityFocus.collectAsState()

    val labelStyle = MaterialTheme.typography.bodySmall.copy(
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    val totalStyle = MaterialTheme.typography.bodySmall.copy(
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.SemiBold
    )

    LaunchedEffect(requestFocus) {
        if (requestFocus) {
            productFocusRequester.requestFocus()
            viewModel.resetProductFocusRequest()
        }
    }

    LaunchedEffect(requestQuantityFocus) {
        if (requestQuantityFocus) {
            quantityFocusRequester.requestFocus()
            viewModel.clearRequestQuantityFocus()
        }
    }

    Scaffold(
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

                Button(
                    onClick = viewModel::saveOrder,
                    enabled = state.selectedCustomer != null && state.orderItems.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Order")
                }
            }
        }

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp), // spacing below dropdowns
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SearchableDropdown(
                    items = state.customers,
                    selectedItem = state.selectedCustomer,
                    onItemSelected = viewModel::selectCustomer,
                    onSearchQueryChanged = viewModel::updateCustomerSearch,
                    itemLabel = { it.name },
                    label = "Customer",
                    modifier = Modifier.weight(1f)
                )

                SearchableDropdown(
                    items = paymentModes,
                    selectedItem = state.selectedPaymentMode,
                    onItemSelected = { viewModel.selectPaymentMode(it) },
                    onSearchQueryChanged = {},
                    itemLabel = { it },
                    label = "Pay Mode",
                    modifier = Modifier.weight(1f)
                )
            }

            SearchableDropdown(
                items = state.products,
                selectedItem = state.selectedProduct,
                onItemSelected = { viewModel.selectProduct(it) },
                onSearchQueryChanged = { viewModel.updateProductSearch(it) },
                itemLabel = { it.name },
                label = "Select Product",
                modifier = Modifier.fillMaxWidth(),
                focusRequester = productFocusRequester
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = state.quantity,
                    onValueChange = viewModel::updateQuantity,
                    label = { Text("Quantity") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(quantityFocusRequester)
                )

                Button(
                    onClick = viewModel::addProductToOrder,
                    enabled = state.selectedProduct != null && state.quantity.isNotEmpty(),
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text("Add Item")
                }
            }


            Spacer(Modifier.height(16.dp))

            Text("Order Items:", style = MaterialTheme.typography.titleMedium)
            LazyColumn {
                itemsIndexed(state.orderItems) { index, item ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp)
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

                            // Name and details
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = item.product.name,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    ),
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Qty: ${item.quantity}",
                                        style = labelStyle,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = "Rate: ${priceFormatter.format(item.product.sellingPrice)}",
                                        style = labelStyle,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = "Total: ${priceFormatter.format(item.quantity * item.product.sellingPrice)}",
                                        style = totalStyle, // Highlight the total
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }

                        // Divider line
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

