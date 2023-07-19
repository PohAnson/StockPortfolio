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

    val headerTitle: List<String> = listOf("Symbol", "Avg Price", "Volume", "Cost")

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

        // Avg Price
        Text(
            "%.3f".format(portfolioRowData.avgPrice),
            modifier = Modifier.width(cellWidth(1)),
            style = cellTextStyle
        )

        // Volume
        Text(
            portfolioRowData.volume.toString(),
            modifier = Modifier.width(cellWidth(2)),
            style = cellTextStyle
        )

        // Total Cost
        Text(
            "%.2f".format(portfolioRowData.cost),
            modifier = Modifier.width(cellWidth(3)),
            style = cellTextStyle
        )


    }
}

@Composable
@Preview(showBackground = true)
fun PortfolioTablePreview() {
    PortfolioTable(
        listOf(
            PortfolioRowData("Mapletree PanAsia Com Tr", "N2IU", 100, 1.143f)
        )
    )
}