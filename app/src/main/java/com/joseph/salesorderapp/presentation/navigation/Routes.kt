package com.joseph.salesorderapp.presentation.navigation

sealed class Routes(val route: String) {
    object Splash : Routes("splash")
    object Login : Routes("login")
    object Dashboard : Routes("dashboard")
    object CustomerList : Routes("customers")
    object AddCustomer : Routes("add_customer")
    object ProductList : Routes("product_list")
    object Report : Routes("report")
}