package com.example.owlio

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.owlio.ui.screen.PnlScreen
import com.example.owlio.ui.screen.PortfolioScreen
import com.example.owlio.ui.screen.Screens
import com.example.owlio.ui.screen.TransactionScreen
import com.example.owlio.ui.screen.form.TransactionFormScreen


@Composable
fun OwlioApp() {
    val navController = rememberNavController()

    Scaffold(bottomBar = { OwlioBottonNavBar(navController = navController) }) { innerpadding ->
        NavHost(navController = navController, startDestination = Screens.PORTFOLIO.route) {
            composable(Screens.PORTFOLIO.route) {
                PortfolioScreen()
            }
            composable(Screens.TRANSACTION.route) {
                TransactionScreen(goToTransactionForm = { navController.navigate(route = Screens.TRANSACTIONFORM.route) }, modifier=Modifier.padding(innerpadding))
            }
            composable(Screens.PNL.route) {
                PnlScreen()
            }
            composable(Screens.TRANSACTIONFORM.route) {
                TransactionFormScreen(modifier = Modifier.padding(innerpadding))
            }
        }
    }
}

@Composable
fun OwlioBottonNavBar(navController: NavController) {
    BottomNavigation {
        val screens = listOf(Screens.PORTFOLIO, Screens.TRANSACTION, Screens.PNL)
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        screens.forEach { screen ->
            BottomNavigationItem(
                selected = currentRoute == screen.route,
                onClick = {
                    navController.popBackStack()
                    navController.navigate(route = screen.route)

                },
                icon = {
                    screen.icon?.let { painterResource(id = it) }?.let {
                        Icon(
                            painter = it, contentDescription = null, modifier = Modifier.size(28.dp)
                        )
                    }
                },
                label = {
                    Text(
                        text = screen.label, fontWeight = FontWeight.SemiBold, fontSize = 14.sp
                    )
                },
                alwaysShowLabel = true,
            )
        }
    }
}

