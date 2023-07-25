package com.example.owlio.ui.screen.pnl

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.owlio.ui.LoadingAnimation

@Composable
fun PnlScreen(modifier: Modifier = Modifier) {
    val vm: PnlViewModel = hiltViewModel()
    val pnlRowDataList by vm.pnlRow.collectAsState()
    if (vm.isLoading) {
        Box(Modifier.fillMaxSize()) {
            LoadingAnimation(modifier = Modifier.align(Alignment.Center))
        }
    } else {
        PnlTable(pnlRowDataList, modifier = modifier)
    }
}