package com.example.owlio.ui.screen.portfolio

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.owlio.model.PortfolioRowData

@Composable
fun PortfolioTable(portfolioRowDataList: List<PortfolioRowData>, modifier: Modifier=Modifier) {
    fun cellWidth(index: Int): Dp {
        return when (index) {
            0 -> 150.dp
            1 -> 70.dp
            2 -> 70.dp
            3 -> 70.dp
            else -> 0.dp
        }
    }

    val headerTitle: List<String> = listOf("Symbol", "Avg Price", "Volume", "Cost")

    LazyColumn(
        modifier.padding(4.dp), horizontalAlignment = Alignment.CenterHorizontally
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
            Divider(Modifier.padding(vertical = 6.dp))
        }
        items(portfolioRowDataList, key = { it.stockCode }) {
            PortfolioRow(it, cellWidth = ::cellWidth)
            Divider()
        }
    }
}

@Composable
fun PortfolioRow(portfolioRowData: PortfolioRowData, cellWidth: (Int) -> Dp) {
    val cellTextStyle = TextStyle(fontSize = 14.sp)
    Row(Modifier.padding(vertical = 4.dp)) {
        // Stock Name/Code
        Column(modifier = Modifier.width(cellWidth(0))) {
            Text(
                text = portfolioRowData.stockName,
                style = cellTextStyle,
                fontWeight = FontWeight.SemiBold
            )
            Text(text = portfolioRowData.stockCode, style = cellTextStyle)
        }

        // Avg Price
        Text("%.3f".format(portfolioRowData.avgPrice), modifier = Modifier.width(cellWidth(1)))

        // Volume
        Text(portfolioRowData.volume.toString(), modifier = Modifier.width(cellWidth(2)))

        // Total Cost
        Text("%.2f".format(portfolioRowData.cost), modifier = Modifier.width(cellWidth(3)))


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