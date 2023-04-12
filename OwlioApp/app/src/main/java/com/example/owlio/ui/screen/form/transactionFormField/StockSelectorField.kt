package com.example.owlio.ui.screen.form.transactionFormField

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import com.example.owlio.model.StockInfo
import com.example.owlio.ui.screen.form.GenericFieldRow


@Composable
fun StockSelectorField(
    stockList: List<StockInfo>,
    selectedStock: StockInfo?,
    updateSelectedStock: (StockInfo) -> Unit
) {
    var currentQuery: String by remember { mutableStateOf("") }
    val suggestions = stockList.filter {
        it.tradingName.contains(
            currentQuery,
            ignoreCase = true
        ) or it.tradingCode.contains(currentQuery, ignoreCase = true)
    }

    var expanded: Boolean by remember {
        mutableStateOf(false)
    }
    GenericFieldRow(label = "Selected Stock: ${selectedStock?.tradingName}") {

        OutlinedTextField(value = currentQuery,
            onValueChange = { currentQuery = it },
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    if (expanded) {
                        Icon(imageVector = Icons.Filled.KeyboardArrowUp, contentDescription = "^")
                    } else {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "Expand"
                        )

                    }
                }

            })
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            suggestions.forEach {
                DropdownMenuItem(onClick = { updateSelectedStock(it) }) {
                    Text(it.tradingName)

                }
            }
        }
    }
}