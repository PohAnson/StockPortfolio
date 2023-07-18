package com.example.owlio.ui.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.owlio.ui.screen.portfolio.PortfolioTable
import com.example.owlio.ui.screen.portfolio.PortfolioViewModel

@Composable
fun PortfolioScreen(modifier: Modifier = Modifier) {
    val vm: PortfolioViewModel = hiltViewModel()
    val portfolioRowDataList = vm.portfolioRowDataState.collectAsState().value
    if (vm.isLoading) {
        Text(text = "Loading ......")
    } else {
        PortfolioTable(portfolioRowDataList = portfolioRowDataList, modifier = modifier)
    }
}