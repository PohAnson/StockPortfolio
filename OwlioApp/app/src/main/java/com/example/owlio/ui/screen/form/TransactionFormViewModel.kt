package com.example.owlio.ui.screen.form

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.owlio.data.StockInfoRepo
import com.example.owlio.data.TransactionRepo
import com.example.owlio.model.Broker
import com.example.owlio.model.StockInfo
import com.example.owlio.model.TradeType
import com.example.owlio.model.Transaction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "TransactionFormViewModel"

@HiltViewModel
class TransactionFormViewModel @Inject constructor(
    private val stockInfoRepo: StockInfoRepo, private val transactionRepo: TransactionRepo
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionUiFormDataState())
    val uiState: StateFlow<TransactionUiFormDataState> = _uiState.asStateFlow()

    fun updateTradeDate(tradeDate: String) {
        _uiState.update {
            it.copy(
                tradeDate = tradeDate
            )
        }
    }

    fun updateSelectedStock(stock: StockInfo?) {
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

    fun updateCurrentSelectedStockQuery(value: String) {
        _uiState.update { it.copy(currentSelectedStockQuery = value) }
    }

    fun fillFormFromId(transactionId: Int) {
        viewModelScope.launch {
            transactionRepo.getTransactionById(transactionId).let { transaction ->
                val selectedStock = stockInfoRepo.getStockInfoByCode(transaction.stockCode).first()
                _uiState.update {
                    it.copy(
                        tradeDate = Transaction.toTradeDateString(transaction.tradeDate),
                        selectedStock = selectedStock,
                        broker = transaction.broker,
                        tradeType = transaction.tradeType,
                        price = transaction.price.toString(),
                        volume = transaction.volume.toString(),
                        transactionId = transactionId.toString(),
                        currentSelectedStockQuery = selectedStock.tradingName
                    )
                }
            }
        }
    }

    suspend fun submitForm(isEdit: Boolean): SubmissionStatus {
        /**
         * Submit the form, return true if it is submitted         *
         * */
        _uiState.update { it.copy(submissionStatus = SubmissionStatus.Loading) }
        val submissionStatus = validateAllFields()
        _uiState.update { it.copy(submissionStatus = submissionStatus) }
        return if (submissionStatus is SubmissionStatus.Validated) {
            viewModelScope.launch(Dispatchers.IO) {
                if (isEdit) {
                    Log.d("update trans", uiState.value.toTransaction().toString())
                    transactionRepo.updateTransaction(uiState.value.toTransaction())
                        .let { Log.d("DB UPDATE", it.toString()) }
                } else {
                    transactionRepo.insertTransaction(uiState.value.toTransaction())
                }

            }.invokeOnCompletion {
                _uiState.update { it.copy(submissionStatus = SubmissionStatus.Success) }
            }
            SubmissionStatus.Success
        } else {
            submissionStatus
        }
    }

    private suspend fun validateAllFields(): SubmissionStatus {
        val validatedResults: Map<String, Boolean> = validateAllFormData()
        Log.d(TAG.plus(" validateAllFields"), validatedResults.toString())
        return if (validatedResults.all { it.value }) {
            // if all form data is valid
            SubmissionStatus.Validated
        } else {
            // form data is invalid
            SubmissionStatus.Error(
                "${
                    (validatedResults.filter { !it.value }.keys.joinToString())
                } is invalid"
            )

        }
    }


    private suspend fun validateAllFormData(): Map<String, Boolean> {
        val (tradeDate, selectedStock, broker, tradeType, price, volume) = _uiState.value
        val stockCode = selectedStock?.tradingCode ?: ""

        return mapOf(
            "Date" to Transaction.validateTradeDateString(tradeDate),
            "Stock" to Transaction.validateStockCodeString(stockCode, stockInfoRepo),
            "Broker" to (broker != null),
            "Trade Type" to (tradeType != null),
            "Price" to Transaction.validatePriceString(price),
            "Volume" to Transaction.validateVolumeString(volume),
        )
    }

    fun getAllStockInfo(): Flow<List<StockInfo>> {
        return stockInfoRepo.getAllStock()
    }

}

data class TransactionUiFormDataState(
    val tradeDate: String = "",
    val selectedStock: StockInfo? = null,
    val broker: Broker? = null,
    val tradeType: TradeType? = null,
    val price: String = "",
    val volume: String = "",
    val submissionStatus: SubmissionStatus = SubmissionStatus.NotSubmitted,
    val transactionId: String = "0",
    val currentSelectedStockQuery: String = "",
)


fun TransactionUiFormDataState.toTransaction(): Transaction {
    return Transaction(
        transactionId = this.transactionId.toInt(),
        tradeDate = Transaction.fromTradeDateString(this.tradeDate),
        stockCode = this.selectedStock?.tradingCode ?: "",
        broker = this.broker ?: Broker.INVALID,
        tradeType = this.tradeType ?: TradeType.INVALID,
        price = Transaction.priceStringToFloat(this.price),
        volume = Transaction.volumeStringToInt(this.volume)
    )
}

sealed interface SubmissionStatus {
    object NotSubmitted : SubmissionStatus
    object Loading : SubmissionStatus
    object Validated : SubmissionStatus
    data class Error(val errorMessage: String) : SubmissionStatus
    object Success : SubmissionStatus
}


