package com.joseph.salesorderapp.presentation.sale

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import java.text.DecimalFormat
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import com.joseph.salesorderapp.util.DateUtils


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(viewModel: OrderViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    val priceFormatter = DecimalFormat("#,##0.000")
    val paymentModes = listOf("Cash", "Card", "Credit", "Online", "Cash/Card")

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
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = viewModel::onBackPress) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                modifier = Modifier.padding(top = 0.dp),
                windowInsets = WindowInsets(0)
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    SearchableDropdown(
                        items = paymentModes,
                        selectedItem = state.selectedPaymentMode,
                        onItemSelected = { viewModel.selectPaymentMode(it) },
                        onSearchQueryChanged = {},
                        itemLabel = { it },
                        label = "Pay Mode",
                        modifier = Modifier.weight(1f)
                    )
                    if (state.isEnableDiscount){
                        OutlinedTextField(
                            value = state.discount,
                            onValueChange = viewModel::updateDiscount,
                            label = { Text("Discount") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .weight(1f)
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Total Items
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Total Items",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = state.orderItems.size.toString(),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Total Amount",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = priceFormatter.format(
                                state.totalAmount
                            ),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = viewModel::clearState,
                        enabled = state.selectedCustomer != null || state.orderItems.isNotEmpty(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Text("Cancel Order")
                    }
                    Button(
                        onClick = viewModel::saveOrder,
                        enabled = state.selectedCustomer != null && state.orderItems.isNotEmpty(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Text("Save Order")
                    }
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
                    .fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)
            )

            {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "SO NO: SO${state.nextOrderID}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End,
                    text = DateUtils.getCurrentDate(),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            SearchableDropdown(
                items = state.customers,
                selectedItem = state.selectedCustomer,
                onItemSelected = viewModel::selectCustomer,
                onSearchQueryChanged = viewModel::updateCustomerSearch,
                itemLabel = { it.name },
                label = "Customer",
                modifier = Modifier.fillMaxWidth(),
            )
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
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    enabled = state.isEnablePriceEdit,
                    value =state.price,
                    onValueChange = viewModel::updatePrice,
                    label = { Text("Price") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier
                        .weight(1f)

                )
                OutlinedTextField(
                    value = state.quantity,
                    singleLine = true,
                    onValueChange = viewModel::updateQuantity,
                    label = { Text("Quantity") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(quantityFocusRequester)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = viewModel::addProductToOrder,
                    enabled = state.selectedProduct != null && state.quantity.isNotEmpty()
                ) {
                    Text("Add Item")
                }
            }
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
                                        style = totalStyle,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }

                            IconButton(onClick = { viewModel.removeOrderItem(index) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Remove Item",
                                    tint = MaterialTheme.colorScheme.error
                                )
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

