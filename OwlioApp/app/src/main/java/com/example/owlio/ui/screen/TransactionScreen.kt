package com.example.owlio.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TransactionScreen(goToTransactionForm: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(1f)) {
        Text("Transaction")
        FloatingActionButton(
            onClick = { goToTransactionForm() }, modifier = Modifier
                .align(
                    Alignment.BottomEnd
                )
                .padding(8.dp)
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
        }
    }
}