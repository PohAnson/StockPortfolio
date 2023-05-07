package com.example.owlio.ui.screen.transaction

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.owlio.model.Broker
import com.example.owlio.model.StockInfo
import com.example.owlio.model.TradeType
import com.example.owlio.model.Transaction
import com.example.owlio.utils.toDate
import com.example.owlio.utils.toLocalDate
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun TransactionTable(transactionList: List<Transaction>, stockInfoMapping: Map<String, StockInfo>) {
    val headerTitle: List<String> = listOf("Date", "Type/\nBroker", "Symbol", "Qty/\nPrice")

    fun cellWidth(index: Int): Dp {
        return when (index) {
            0 -> 70.dp
            1 -> 70.dp
            2 -> 140.dp
            3 -> 50.dp
            else -> 0.dp
        }
    }
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(5.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Row {
                headerTitle.forEachIndexed { index, title ->
                    Text(
                        title,
                        modifier = Modifier.width(cellWidth(index)),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Divider(Modifier.padding(top = 8.dp))
        }
        items(transactionList, key = { it.transactionId }) {
            TransactionRow(
                transaction = it, stockInfoMapping[it.stockCode], cellWidth = ::cellWidth
            )
            Divider()
        }
    }
}


@Composable
fun TransactionRow(transaction: Transaction, stockInfo: StockInfo?, cellWidth: (Int) -> Dp) {
    val cellTextStyle = TextStyle(fontSize = 15.sp)
    Row(Modifier.padding(vertical = 10.dp)) {
        //Date
        Text(
            transaction.tradeDate.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yy")),
            modifier = Modifier.width(cellWidth(0)),
            style = cellTextStyle
        )

        // Type/Broker
        Column(modifier = Modifier.width(cellWidth(1))) {
            Text(
                transaction.tradeType.name,
                style = cellTextStyle,
                color = when (transaction.tradeType) {
                    TradeType.Buy -> Color.Green
                    TradeType.Sell -> Color.Red
                    TradeType.INVALID -> Color.LightGray
                }

            )
            Text(text = transaction.broker.name, style = cellTextStyle, color = Color.Gray)

        }

        // Symbol
        Column(
            modifier = Modifier.width(cellWidth(2))
        ) {
            val fontSz = if ((stockInfo?.tradingName ?: "").length < 14) 15.sp else 12.sp

            Text(
                text = stockInfo?.tradingName ?: "NIL",
                style = cellTextStyle,
                fontSize = fontSz,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(transaction.stockCode, style = cellTextStyle)
        }

        // Qty/Price
        Column(
            modifier = Modifier.width(cellWidth(3)),
        ) {
            Text(
                transaction.volume.toString(), style = cellTextStyle
            )
            Text(
                "%.3f".format(transaction.price), style = cellTextStyle
            )
        }
    }
}


@Composable
@Preview(showBackground = true)
fun TransactionTablePreview() {
    TransactionTable(
        transactionList = listOf(
            Transaction(
                1, LocalDate.now().toDate(), "N2IU", Broker.Poems, TradeType.Buy, 1f, 100
            ), Transaction(
                2, LocalDate.now().toDate(), "N2IU", Broker.Poems, TradeType.Sell, 1f, 100
            )
        ), stockInfoMapping = mapOf(
            "N2IU" to StockInfo(
                "Mapletree Pan Asia Commercial Trust",
                "N2IU",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
            )
        )
    )

}
