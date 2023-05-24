package com.example.owlio

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.AlertDialog
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.owlio.ui.SnackbarDelegate
import com.example.owlio.ui.SnackbarState
import com.example.owlio.ui.getValue
import com.example.owlio.ui.screen.PortfolioScreen
import com.example.owlio.ui.screen.Screens
import com.example.owlio.ui.screen.form.EditTransactionFormScreen
import com.example.owlio.ui.screen.form.NewTransactionFormScreen
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
                TransactionScreen(goToNewTransactionForm = { navController.navigate(route = Screens.NEWTRANSACTION.route) },
                    modifier = Modifier.padding(innerpadding),
                    goToEditTransactionForm = { transactionId -> navController.navigate(Screens.EDITTRANSACTION.route + transactionId) })
            }
            composable(Screens.PNL.route) {
                topAppBarTitle = Screens.PNL.label
                PnlScreen()
            }
            composable(Screens.NEWTRANSACTION.route) {
                topAppBarTitle = Screens.NEWTRANSACTION.label
                NewTransactionFormScreen(
                    modifier = Modifier.padding(innerpadding),
                    snackbarDelegate = snackbarDelegate,
                    navigateBack = { navController.popBackStack() },
                )
            }
            composable(
                Screens.EDITTRANSACTION.route + "{transactionId}",
                arguments = listOf(navArgument("transactionId") { type = NavType.StringType })
            ) { backStackEntry ->

                topAppBarTitle = Screens.EDITTRANSACTION.label
                val transactionId = backStackEntry.arguments?.getString("transactionId")
                if (transactionId != null) {
                    EditTransactionFormScreen(transactionId = transactionId,
                        snackbarDelegate = snackbarDelegate,
                        navigateBack = { navController.popBackStack() })
                } else {
                    snackbarDelegate.showSnackbar(SnackbarState.ERROR, "Error Editing transaction")
                    navController.popBackStack(route = Screens.TRANSACTION.route, inclusive = false)
                }
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
        var openConfirmLogoutDialog by remember { mutableStateOf(false) }

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
                        text = screen.label, fontWeight = FontWeight.SemiBold, fontSize = 10.sp
                    )
                },
            )
        }
        BottomNavigationItem(selected = false,
            onClick = { openConfirmLogoutDialog = true },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.ExitToApp, contentDescription = null
                )
            },
            label = { Text(text = "Logout", fontSize = 10.sp) })
        if (openConfirmLogoutDialog) AlertDialog(onDismissRequest = {
            openConfirmLogoutDialog = false
        },
            title = { Text(text = "Confirm Logout?") },
            confirmButton = { Button(onClick = { onLogout() }) { Text("Logout") } },
            dismissButton = {
                Button(onClick = {
                    openConfirmLogoutDialog = false
                }) { Text("Cancel") }
            })


    }
}

