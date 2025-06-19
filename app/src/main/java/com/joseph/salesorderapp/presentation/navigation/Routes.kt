package com.joseph.salesorderapp.presentation.navigation

sealed class Routes(val route: String) {
    object Splash : Routes("splash")
    object Login : Routes("login")
    object Dashboard : Routes("dashboard")
    object CustomerList : Routes("customers")
    object AddCustomer : Routes("add_customer")
    object SaleOrder : Routes("sale_order")
    object Report : Routes("report")
    object ReportDetails : Routes("report_details/{orderId}") {
        fun createRoute(orderId: Int): String = "report_details/$orderId"
    }

}