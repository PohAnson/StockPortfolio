package com.example.owlio.ui.screen.pnl

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.owlio.model.PnlRowData
import com.example.owlio.ui.theme.OwlioAppTheme

@Composable
fun PnlTable(pnlRowDataList: List<PnlRowData>, modifier: Modifier = Modifier) {
    fun cellWidth(index: Int): Dp {
        return when (index) {
            0 -> 130.dp
            1 -> 75.dp
            2 -> 75.dp
            3 -> 75.dp
            else -> 0.dp
        }
    }

    val headerTitle: List<String> = listOf("Symbol", "Buy/Sell", "Dividend", "Total")

    Column(
        modifier
            .fillMaxSize()
            .padding(5.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            headerTitle.forEachIndexed { index, title ->
                Text(
                    title,
                    modifier = Modifier.width(cellWidth(index)),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        Divider(Modifier.padding(top = 8.dp))
        LazyColumn {
            items(pnlRowDataList, key = { it.stockCode }) {
                PnlRow(it, cellWidth = ::cellWidth)
                Divider()
            }
        }
    }
}

@Composable
fun PnlRow(pnlRowData: PnlRowData, cellWidth: (Int) -> Dp, modifier: Modifier = Modifier) {
    val cellTextStyle = MaterialTheme.typography.bodyLarge
    Row(modifier = modifier.padding(vertical = 10.dp)) {

        // Stock Name/Code
        Column(modifier = Modifier.width(cellWidth(0))) {
            val fontSz = if ((pnlRowData.stockName).length < 14) 16.sp else 12.sp

            Text(
                text = pnlRowData.stockName,
                style = cellTextStyle,
                fontWeight = FontWeight.SemiBold,
                fontSize = fontSz,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = pnlRowData.stockCode, style = cellTextStyle)
        }

        // Buy/Sell
        Text(
            "%.2f".format(pnlRowData.transaction),
            modifier = Modifier.width(cellWidth(1)),
            style = cellTextStyle
        )

        // Dividend
        Text(
            "%.2f".format(pnlRowData.dividend),
            modifier = Modifier.width(cellWidth(2)),
            style = cellTextStyle
        )

        // Total Cost
        Text(
            "%.2f".format(pnlRowData.totalEarnings),
            modifier = Modifier.width(cellWidth(3)),
            style = cellTextStyle
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PnlTablePreview() {
    OwlioAppTheme {
        PnlTable(listOf())
    }
}