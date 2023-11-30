package com.example.owlio.ui.screen.portfolio

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.owlio.model.PortfolioRowData

@Composable
fun PortfolioTable(portfolioRowDataList: List<PortfolioRowData>, modifier: Modifier = Modifier) {
    fun cellWidth(index: Int): Dp {
        return when (index) {
            0 -> 140.dp
            1 -> 70.dp
            2 -> 70.dp
            3 -> 80.dp
            else -> 0.dp
        }
    }

    val headerTitle: List<String> =
        listOf("Symbol", "Last /\nAvg", "MV /\nQty", "Unrealised P/L")

    Column(
        modifier
            .fillMaxSize()
            .padding(5.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.padding(
                0.dp
            )
        ) {
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
            items(portfolioRowDataList, key = { it.stockCode }) {
                PortfolioRow(it, cellWidth = ::cellWidth)
                Divider()
            }
        }
    }
}

@Composable
fun PortfolioRow(portfolioRowData: PortfolioRowData, cellWidth: (Int) -> Dp) {
    val cellTextStyle = MaterialTheme.typography.bodyLarge
    Row(Modifier.padding(vertical = 10.dp)) {
        // Stock Name/Code
        Column(modifier = Modifier.width(cellWidth(0))) {
            val fontSz =
                if ((portfolioRowData.stockName).length < 14) 16.sp else 12.sp

            Text(
                text = portfolioRowData.stockName,
                style = cellTextStyle,
                fontWeight = FontWeight.SemiBold,
                fontSize = fontSz,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = portfolioRowData.stockCode, style = cellTextStyle)
        }

        // Last/Avg Price
        Column(
            modifier = Modifier.width(cellWidth(1)),
        ) {
            // Last
            if (portfolioRowData.lastPrice < 0)
                Text("--", color = Color.Gray)
            else
                Text(
                    "%.3f".format(portfolioRowData.lastPrice),
                    style = cellTextStyle
                )
            // Avg
            Text(
                "%.3f".format(portfolioRowData.avgPrice),
                style = cellTextStyle
            )
        }

        // MV/Volume
        Column(
            modifier = Modifier.width(cellWidth(1)),
        ) {
            // Market Value
            if (portfolioRowData.lastPrice < 0)
                Text("--", color = Color.Gray)
            else
                Text(
                    "%.2f".format(portfolioRowData.marketValue),
                    style = cellTextStyle
                )
            // Volume
            Text(
                portfolioRowData.volume.toString(),
                style = cellTextStyle
            )
        }

        // Unrealised P/L
        if (portfolioRowData.lastPrice < 0)
            Text("--", color = Color.Gray)
        else
            Text(
                "%.2f".format(portfolioRowData.unrealisedPnl),
                modifier = Modifier.width(cellWidth(3)),
                style = cellTextStyle,
                color = if (portfolioRowData.unrealisedPnl < 0) Color.Red else Color.Green
            )


    }
}

@Composable
@Preview(showBackground = true)
fun PortfolioTablePreview() {
    PortfolioTable(
        listOf(
            PortfolioRowData("Mapletree PanAsia Com Tr", "N2IU", 100, 1.143f, 1.55f)
        )
    )
}