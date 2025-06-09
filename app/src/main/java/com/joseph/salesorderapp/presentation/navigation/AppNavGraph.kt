package com.joseph.salesorderapp.presentation.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import com.joseph.salesorderapp.presentation.login.LoginScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.joseph.salesorderapp.presentation.UiEventManager
import com.joseph.salesorderapp.presentation.dashboard.DashBoardScreen
import com.joseph.salesorderapp.presentation.splash.SplashScreen
import com.joseph.salesorderapp.util.UiEvent

//@Composable
//fun AppNavGraph(navController: NavHostController) {
//    NavHost(
//        navController = navController,
//        startDestination = Routes.Splash.route
//    ) {
//        composable(Routes.Splash.route) { SplashScreen(navController) }
//        composable(Routes.Login.route) { LoginScreen(navController) }
//        composable(Routes.Dashboard.route) { DashBoardScreen(navController) }
//        composable(Routes.CustomerList.route) { CustomerScreen(navController) }
////        composable(Routes.AddCustomer.route) { AddCustomerScreen(navController) }
////        composable(Routes.ProductList.route) { ProductListScreen(navController) }
////        composable(Routes.Report.route) { ReportScreen(navController) }
//    }
//}
@Composable
fun AppNavGraph(
    navController: NavHostController,
    uiEventManager: UiEventManager
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

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
        }
    }
}
