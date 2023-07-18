package com.example.owlio.ui.screen.form.transactionFormField

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.owlio.model.StockInfo

fun StockInfo.containsStringQuery(query: String): Boolean {
    val currentQuery = query.trim()
    return (this.tradingName.contains(currentQuery, ignoreCase = true) or this.tradingCode.contains(
        currentQuery, ignoreCase = true
    ))

}

@Composable
fun StockSelectorField(
    currentQuery: String,
    updateCurrentQuery: (String) -> Unit,
    stockList: List<StockInfo>,
    selectedStock: StockInfo?,
    updateSelectedStock: (StockInfo?) -> Unit
) {


    val suggestions = stockList.filter {
        it.containsStringQuery(currentQuery)
    }

    val fieldIsNotEmpty = currentQuery.isNotEmpty()
    var isFieldFocused by remember {
        mutableStateOf(false)
    }
    val focusManager = LocalFocusManager.current

    GenericFieldRow(label = "Selected Stock: ${selectedStock?.tradingCode}") {
        Column {
            OutlinedTextField(value = currentQuery,
                onValueChange = {
                    updateCurrentQuery(it)
                },
                modifier = Modifier.onFocusChanged { isFieldFocused = it.isFocused },
                trailingIcon = {
                    if (fieldIsNotEmpty) {
                        IconButton(onClick = {
                            updateCurrentQuery("")
                            updateSelectedStock(null)
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Clear, contentDescription = "X"
                            )
                        }
                    }
                })

            // Show autocomplete list only when it is in focus
            if (isFieldFocused) {
                Card(
                    modifier = Modifier.padding(2.dp, 4.dp),
                    elevation = CardDefaults.elevatedCardElevation()
                ) {
                    LazyColumn(
                        modifier = Modifier.heightIn(max = TextFieldDefaults.MinHeight * 4)
                    ) {
                        items(suggestions, key = { it.tradingCode }) { stockInfo ->
                            AutocompleteItem(stockInfo = stockInfo, onSelectStock = {
                                updateSelectedStock(stockInfo)
                                updateCurrentQuery(stockInfo.tradingName)
                                focusManager.clearFocus()
                            })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AutocompleteItem(stockInfo: StockInfo, onSelectStock: () -> Unit) {
    Text(stockInfo.tradingName,
        modifier = Modifier
            .clickable { onSelectStock() }
            .fillMaxWidth(1f)
            .padding(horizontal = 6.dp, vertical = 12.dp))
    Divider()
}