package com.joseph.salesorderapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.joseph.salesorderapp.presentation.UiEventManager
import com.joseph.salesorderapp.presentation.navigation.AppNavGraph
import com.joseph.salesorderapp.ui.theme.SalesOrderAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    lateinit var navController: NavHostController

    @Inject
    lateinit var uiEventManager: UiEventManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SalesOrderAppTheme {
                SetupAppNavGraph()
            }
        }
    }

    @Composable
    fun SetupAppNavGraph() {
        navController = rememberNavController() // This is a NavHostController
        AppNavGraph(
            navController = navController,
            uiEventManager = uiEventManager
        )
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SalesOrderAppTheme {

    }
}
