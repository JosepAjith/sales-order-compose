package com.joseph.salesorderapp.presentation.dashboard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.joseph.salesorderapp.ui.component.BluetoothPermissionHandler
import com.joseph.salesorderapp.ui.component.DashboardCard


@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashBoardScreen(
    viewModel: DashBoardViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current

    BluetoothPermissionHandler(
        onPermissionGranted = {

        },
        onPermissionDenied = {

        }
    )


    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadUnsyncedCounts()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBar(
                title = { Text("Home") },
                actions = {
                    IconButton(onClick = { viewModel.onCardClicked("settings") }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                },
                windowInsets = WindowInsets(0)
            )
        }

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
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
                        onClick = { viewModel.onOrderCardClicked() }
                    )
                    DashboardCard(
                        label = "Reports",
                        icon = Icons.Default.Assessment,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.onCardClicked("report_type") }
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    DashboardCard(
                        label = if ((state.unsyncedCustomersCount ?: 0) > 0)
                            "Sync Customers\n(${state.unsyncedCustomersCount} pending)"
                        else "Sync Customers",
                        icon = Icons.Default.Sync,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.fetchUnsyncedCustomerToSync() }
                    )
                    DashboardCard(
                        label = if ((state.unsyncedOrdersCount ?: 0) > 0)
                            "Sync Orders\n(${state.unsyncedOrdersCount} pending)"
                        else "Sync Orders",
                        icon = Icons.Default.SyncAlt,
                        modifier = Modifier.weight(1f),
                        onClick = { viewModel.fetchUnsyncedOrdersToSync() }
                    )
                }
            }
        }
    }
}




