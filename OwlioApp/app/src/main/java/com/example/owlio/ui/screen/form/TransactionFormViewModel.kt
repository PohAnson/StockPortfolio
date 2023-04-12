package com.example.owlio.ui.screen.form

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.owlio.model.Broker
import com.example.owlio.model.StockInfo
import com.example.owlio.model.TradeType
import com.example.owlio.model.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

private val TAG = "TransactionFormViewModel"

class TransactionFormViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TransactionUiFormDataState())
    val uiState: StateFlow<TransactionUiFormDataState> = _uiState.asStateFlow()


    fun updateTradeDate(tradeDate: String) {
        _uiState.update {
            it.copy(
                tradeDate = tradeDate
            )
        }
    }

    fun updateSelectedStock(stock: StockInfo) {
        _uiState.update { it.copy(selectedStock = stock) }
    }

    fun updateBroker(broker: Broker) {
        _uiState.update { it.copy(broker = broker) }
    }

    fun updateTradeType(tradeType: TradeType) {
        _uiState.update { it.copy(tradeType = tradeType) }
    }

    fun updatePrice(price: String) {
        _uiState.update { it.copy(price = price) }
    }

    fun updateVolume(volume: String) {
        _uiState.update { it.copy(volume = volume) }
    }

    fun updateErrorMessage(msg: String?) {
        _uiState.update { it.copy(errorMessage = msg) }
    }

    fun validateAllFields(): Boolean {
        val validatedResults = _uiState.value.validateAllFormData()
        if (!validatedResults.values.any()) {
            // if form data is valid
            updateErrorMessage(null)
            return true
        }
        // form data is invalid
        updateErrorMessage(
            "${
                (validatedResults.filter { !it.value }.keys.joinToString())
            } is invalid"
        )
        Log.d(TAG.plus(" validateAllFields"), validatedResults.toString())
        return false

    }

    fun getAllStockInfo(): List<StockInfo> {
        return listOf() // TODO: get data from db
    }
}

data class TransactionUiFormDataState(
    val tradeDate: String = "",
    val selectedStock: StockInfo? = null,
    val broker: Broker? = null,
    val tradeType: TradeType? = null,
    val price: String = "",
    val volume: String = "",
    val errorMessage: String? = "",
) {

    fun validateAllFormData(): Map<String, Boolean> {
        val stockCode = selectedStock?.tradingCode ?: ""
        return mapOf(
            "Date" to Transaction.validateTradeDateString(tradeDate),
            "Stock" to Transaction.validateStockCodeString(stockCode),
            "Broker" to (broker != null),
            "Trade Type" to (tradeType != null),
            "Price" to Transaction.validatePriceString(price),
            "Volume" to Transaction.validateVolumeString(volume),

            )
    }
}