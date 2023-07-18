package com.example.owlio.ui.screen.form.transactionFormField

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import java.util.Calendar


@Composable
fun TradeDateField(tradeDate: String, setTradeDate: (String) -> Unit) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val calendar = Calendar.getInstance()


    val datePicker = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            setTradeDate("${"%02d".format(dayOfMonth)}/${"%02d".format(month + 1)}/$year")
        },
        calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH],
    )
    GenericFieldRow(
        label = "Date"
    ) {
        OutlinedTextField(
            value = tradeDate,
            onValueChange = {
                setTradeDate(it)
            },
            modifier = Modifier.onFocusChanged {
                if (!it.isFocused) {
                    var td = tradeDate
                    //when lose focus
                    if (td.length == 6) {
                        // assume that they meant 20 as the century year
                        td = td.replaceRange(4, 4, "20")
                    }
                    if (td.length == 8) {
                        setTradeDate(
                            td.replaceRange(2, 2, "/").replaceRange(5, 5, "/")
                        )

                    }
                }
            },

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions { focusManager.moveFocus(FocusDirection.Next) },
            label = {
                Text("Date")
            },
            placeholder = { Text("dd/mm/yyyy") },
            trailingIcon = {
                IconButton(onClick = { datePicker.show() }) {
                    Icon(
                        imageVector = Icons.Filled.DateRange, contentDescription = "DatePicker"
                    )
                }
            },
        )
    }

}
