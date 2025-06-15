package com.joseph.salesorderapp.presentation.customer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.joseph.salesorderapp.data.local.dao.CustomerDao
import com.joseph.salesorderapp.data.local.entity.CustomerEntity
import com.joseph.salesorderapp.data.remote.model.CustomersItem

@Composable
fun CustomerItem( customer: CustomerEntity, modifier: Modifier = Modifier) {
    Column(modifier = Modifier.padding(12.dp)) {
        Text(text = customer.name.toString(), style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Phone: ${customer.phoneNo}", style = MaterialTheme.typography.bodySmall)
        Text(
            text = "Address: ${customer.address}",
            style = MaterialTheme.typography.bodySmall
        )
    }
}
