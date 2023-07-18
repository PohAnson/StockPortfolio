package com.example.owlio.ui.screen.form.transactionFormField

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType


@Composable
fun PriceField(price: String, updatePrice: (String) -> Unit) {
    val focusManager = LocalFocusManager.current
    GenericFieldRow(label = "Price") {
        OutlinedTextField(value = price,
            onValueChange = { updatePrice(it) },
            modifier = Modifier.onFocusChanged { focusState ->
                // round to 3 d.p. when loses focus on the field
                if (!focusState.isFocused) {
                    price.toFloatOrNull()?.let { value ->
                        updatePrice("%.3f".format(value))
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions { focusManager.moveFocus(FocusDirection.Next) })
    }
}
