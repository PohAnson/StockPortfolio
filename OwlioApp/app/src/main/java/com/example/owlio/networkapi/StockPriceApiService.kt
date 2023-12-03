package com.example.owlio.networkapi

import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface StockPriceApiService {
    @GET("{stockCode}?includePrePost=false&interval=1d&range=1d")
    suspend fun getCurrentPrice(@Path("stockCode") stockCode: String): Response<StockPriceApiResponse>
}

@Serializable
data class StockPriceApiResponse(val chart: StockPriceApiChart?)

@Serializable
data class StockPriceApiChart(val result: List<StockPriceApiMeta>)


@Serializable
data class StockPriceApiMeta(val meta: StockPriceApiPrice?)

@Serializable
data class StockPriceApiPrice(val regularMarketPrice: Float?)
