package com.example.owlio

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.example.owlio.ui.SnackbarDelegate
import com.example.owlio.ui.getValue
import com.example.owlio.ui.screen.PortfolioScreen
import com.example.owlio.ui.screen.Screens
import com.example.owlio.ui.screen.form.TransactionFormScreen
import com.example.owlio.ui.screen.pnl.PnlScreen
import com.example.owlio.ui.screen.transaction.TransactionScreen

@Composable
fun OwlioApp(onLogout: () -> Unit) {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val snackbarDelegate by remember { SnackbarDelegate() }
    snackbarDelegate.apply {
        snackbarHostState = scaffoldState.snackbarHostState
        coroutineScope = rememberCoroutineScope()
    }
    var topAppBarTitle by remember { mutableStateOf("Owlio") }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            OwlioTopAppBar(title = topAppBarTitle)
        },
        bottomBar = { OwlioBottonNavBar(navController = navController, onLogout) },
        snackbarHost = {
            SnackbarHost(it) { snackbarData ->
                Snackbar(
                    snackbarData, backgroundColor = snackbarDelegate.msnackbarState.backgroundColor
                )
            }
        },
    ) { innerpadding ->
        NavHost(navController = navController, startDestination = Screens.PORTFOLIO.route) {
            composable(Screens.PORTFOLIO.route) {
                topAppBarTitle = Screens.PORTFOLIO.label
                PortfolioScreen(modifier = Modifier.padding(innerpadding))
            }
            composable(Screens.TRANSACTION.route) {
                topAppBarTitle = Screens.TRANSACTION.label
                TransactionScreen(
                    goToTransactionForm = { navController.navigate(route = Screens.TRANSACTIONFORM.route) },
                    modifier = Modifier.padding(innerpadding)
                )
            }
            composable(Screens.PNL.route) {
                topAppBarTitle = Screens.PNL.label
                PnlScreen()
            }
            composable(Screens.TRANSACTIONFORM.route) {
                topAppBarTitle = Screens.TRANSACTIONFORM.label
                TransactionFormScreen(
                    modifier = Modifier.padding(innerpadding),
                    snackbarDelegate = snackbarDelegate,
                    navigateBack = { navController.popBackStack() },
                )
            }
        }
    }
}

@Composable
fun OwlioTopAppBar(
    title: String, modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(title) }, modifier = modifier
    )
}


@Composable
fun OwlioBottonNavBar(navController: NavController, onLogout: () -> Unit) {
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
        BottomNavigationItem(selected = false, onClick = { onLogout() }, icon = {
            Icon(
                imageVector = Icons.Outlined.AccountBox, contentDescription = null
            )
        }, label = { Text(text = "Logout") })


    }
}

