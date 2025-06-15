package com.joseph.salesorderapp.presentation.navigation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import com.joseph.salesorderapp.presentation.login.LoginScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.joseph.salesorderapp.presentation.UiEventManager
import com.joseph.salesorderapp.presentation.customer.CustomerScreen
import com.joseph.salesorderapp.presentation.dashboard.DashBoardScreen
import com.joseph.salesorderapp.presentation.sale.OrderScreen
import com.joseph.salesorderapp.presentation.splash.SplashScreen
import com.joseph.salesorderapp.util.UiEvent

@Composable
fun AppNavGraph(
    navController: NavHostController,
    uiEventManager: UiEventManager
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    var showLoader by remember { mutableStateOf(false) }
    var loaderMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        uiEventManager.eventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.actionLabel
                    )
                }

                is UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                is UiEvent.Navigate -> {
                    navController.navigate(event.route) {
                        event.popUpToRoute?.let { route ->
                            popUpTo(route) {
                                inclusive = event.popUpToInclusive
                            }
                        }
                    }
                }

                is UiEvent.NavigateUp -> {
                    navController.navigateUp()
                }

                is UiEvent.CircleLoader -> {
                    showLoader = event.isVisible
                    loaderMessage = event.message
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.Splash.route) { SplashScreen() }
            composable(Routes.Login.route) { LoginScreen() }
            composable(Routes.Dashboard.route) { DashBoardScreen() }
            composable(Routes.CustomerList.route) { CustomerScreen() }
            composable(Routes.SaleOrder.route) { OrderScreen() }
        }
    }

    if (showLoader) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
