package com.example.owlio

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
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
fun OwlioApp(onLogout: () -> Unit, syncToServer: () -> Unit) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarDelegate by remember { SnackbarDelegate(snackbarHostState = snackbarHostState) }
    snackbarDelegate.apply {
        coroutineScope = rememberCoroutineScope()
    }
    var topAppBarTitle by remember { mutableStateOf("Owlio") }

    Scaffold(
        topBar = {
            OwlioTopAppBar(title = topAppBarTitle, syncToServer = syncToServer)
        },
        bottomBar = { OwlioBottonNavBar(navController = navController, onLogout) },
        snackbarHost = {
            SnackbarHost(snackbarHostState) { snackbarData ->
                Snackbar(
                    snackbarData, containerColor = snackbarDelegate.msnackbarState.backgroundColor
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
                PnlScreen(modifier = Modifier.padding(innerpadding))
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
                        modifier = Modifier.padding(innerpadding),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwlioTopAppBar(
    title: String, modifier: Modifier = Modifier, syncToServer: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                title,
                style = MaterialTheme.typography.headlineSmall,
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = modifier,
        actions = {
            IconButton(onClick = syncToServer) {
                Icon(Icons.Outlined.Refresh, contentDescription = "Sync to Database")
            }
        },
    )
}


@Composable
fun OwlioBottonNavBar(navController: NavController, onLogout: () -> Unit) {
    NavigationBar(tonalElevation = 8.dp) {
        val screens = listOf(Screens.PORTFOLIO, Screens.TRANSACTION, Screens.PNL)
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        var openConfirmLogoutDialog by remember { mutableStateOf(false) }
        val navigationItemLabelStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = 12.sp)

        screens.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    navController.popBackStack()
                    navController.navigate(route = screen.route)

                },
                icon = {
                    screen.icon?.let { painterResource(id = it) }?.let {
                        Icon(
                            painter = it, contentDescription = null, modifier = Modifier.size(24.dp)
                        )
                    }
                },
                label = {
                    Text(
                        text = screen.label, style = navigationItemLabelStyle
                    )
                },
            )
        }
        NavigationBarItem(selected = false,
            onClick = { openConfirmLogoutDialog = true },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.ExitToApp,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                )
            },
            label = { Text(text = "Logout", style = navigationItemLabelStyle) })
        if (openConfirmLogoutDialog) AlertDialog(onDismissRequest = {
            openConfirmLogoutDialog = false
        },
            title = {
                Text(
                    text = "Confirm Logout?",
                    style = MaterialTheme.typography.titleMedium
                )
            },
            confirmButton = { Button(onClick = { onLogout() }) { Text("Logout") } },
            dismissButton = {
                Button(onClick = {
                    openConfirmLogoutDialog = false
                }) { Text("Cancel") }
            })


    }
}

