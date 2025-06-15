package com.joseph.salesorderapp.presentation.sale

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.joseph.salesorderapp.ui.component.SearchableDropdown
import androidx.compose.foundation.lazy.itemsIndexed

@Composable
fun OrderScreen(viewModel: OrderViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        SearchableDropdown(
            items = state.customers,
            selectedItem = state.selectedCustomer,
            onItemSelected = viewModel::selectCustomer,
            itemLabel = { it.name },
            label = "Select Customer"
        )

        Spacer(Modifier.height(16.dp))
        SearchableDropdown(
            items = state.products,
            selectedItem = state.selectedProduct,
            onItemSelected = viewModel::selectProduct,
            itemLabel = { it.name },
            label = "Select Product"
        )


        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = state.quantity,
            onValueChange = viewModel::updateQuantity,
            label = { Text("Quantity") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = viewModel::addProductToOrder,
            enabled = state.selectedProduct != null && state.quantity.isNotEmpty()
        ) {
            Text("Add Product")
        }

        Spacer(Modifier.height(16.dp))

        Text("Order Items:", style = MaterialTheme.typography.titleMedium)
        LazyColumn {
            itemsIndexed(state.orderItems) { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "${index + 1}", modifier = Modifier.weight(0.1f))
                    Text(text = item.product.name, modifier = Modifier.weight(0.5f))
                    Text(text = "x ${item.quantity}", modifier = Modifier.weight(0.4f))
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = viewModel::saveOrder,
            enabled = state.selectedCustomer != null && state.orderItems.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Order")
        }

        if (state.isOrderSaved) {
            Text(
                "✅ Order saved successfully!",
                color = Color.Green,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}
