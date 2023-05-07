package com.example.owlio.ui.screen.form.transactionFormField

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GenericFieldRow(
    label: String, spaceBelow: Boolean = true, inputField: @Composable (() -> Unit)
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label, modifier = Modifier
                .fillMaxWidth(0.3f)
                .padding(end = 4.dp)
        )
        inputField()
    }
    if (spaceBelow) Spacer(modifier = Modifier.height(8.dp))
}
