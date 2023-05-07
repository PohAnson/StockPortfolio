package com.example.owlio.data

import com.example.owlio.model.DividendDateRow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject


class DividendInfoRepo @Inject constructor() {
    suspend fun getDividendDateRowList(stockCode: String): List<DividendDateRow> {
        return parseTableRow(getDividendTable(stockCode))
    }

    private suspend fun getDividendTable(stockCode: String): List<Element> {
        val doc = runBlocking {
            withContext(Dispatchers.Default) {
                Jsoup.connect("https://www.dividends.sg/view/$stockCode").get()
            }
        }
        return doc.select("tr").toList().drop(1) // drop the header
    }

    private fun parseTableRow(rows: List<Element>): List<DividendDateRow> {
        val dividendPayouts: MutableList<DividendDateRow> = mutableListOf()
        rows.forEach { row ->
            val rowList = row.select("td").toList().reversed()

            val rowExDate = rowList[2].text()
            val rowRate = rowList[3].text()
            val exDate = LocalDate.parse(rowExDate, DateTimeFormatter.ofPattern("y-M-d"))
            if (exDate >= LocalDate.of(2015, 1, 1) && rowRate.trim() != "-") {
                dividendPayouts.add(
                    DividendDateRow(
                        rowRate.trim('S', 'G', 'D', ' ').toFloat(),
                        exDate
                    )
                )
            }
        }
        return dividendPayouts
    }

}