package com.example.owlio.ui.screen.pnl

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun PnlScreen() {
    val vm: PnlViewModel = hiltViewModel()
    val pnlRowDataList by vm.pnlRow.collectAsState()
    if (vm.isLoading) {
        Text("Loading....")
    } else {
        PnlTable(pnlRowDataList)
    }
}