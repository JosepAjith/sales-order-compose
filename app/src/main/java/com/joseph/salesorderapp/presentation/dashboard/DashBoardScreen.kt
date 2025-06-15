package com.joseph.salesorderapp.presentation.dashboard

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.joseph.salesorderapp.presentation.splash.SplashViewModel
import com.joseph.salesorderapp.ui.component.DashboardCard


@Composable
fun DashBoardScreen(
    viewModel: DashBoardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Settings icon
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            // Grid of cards (2 columns × 3 rows)
            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    DashboardCard(
                        label = "Download Masters",
                        icon = Icons.Default.Download,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.downloadMasters() }
                    )
                    DashboardCard(
                        label = "Customer",
                        icon = Icons.Default.Person,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.onCardClicked("customers") }
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    DashboardCard(
                        label = "Order Entry",
                        icon = Icons.Default.ListAlt,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.onCardClicked("sale_order") }
                    )
                    DashboardCard(
                        label = "Reports",
                        icon = Icons.Default.Assessment,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.onCardClicked("report") }
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    DashboardCard(
                        label = "Sync Customers",
                        icon = Icons.Default.Sync,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.onCardClicked("sync_customers") }
                    )
                    DashboardCard(
                        label = "Sync Orders",
                        icon = Icons.Default.SyncAlt,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.onCardClicked("sync_orders") }
                    )
                }
            }
        }
    }
}




