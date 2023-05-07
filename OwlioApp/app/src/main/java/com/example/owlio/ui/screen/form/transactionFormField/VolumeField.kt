package com.example.owlio.ui.screen.form.transactionFormField

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType


@Composable
fun VolumeField(volume: String, updateVolume: (String) -> Unit) {
    val focusManager = LocalFocusManager.current
    GenericFieldRow(label = "Volume") {
        OutlinedTextField(value = volume,
            onValueChange = { updateVolume(it) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions { focusManager.clearFocus() })
    }
}
