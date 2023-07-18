package com.example.owlio.ui.screen.form.transactionFormField

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.owlio.model.Broker


@Composable
fun BrokerField(selectedBroker: Broker?, updateBroker: (Broker) -> Unit) {
    GenericFieldRow(label = "Broker") {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val selectedButtonColors = ButtonDefaults.outlinedButtonColors(
//                containerColor = MaterialTheme.colorScheme.primary,
//                contentColor = MaterialTheme.colorScheme.onPrimary
            )
            OutlinedButton(
                onClick = { updateBroker(Broker.Poems) },
                modifier = Modifier.padding(8.dp),
                colors = if (selectedBroker?.name == Broker.Poems.name) selectedButtonColors else ButtonDefaults.outlinedButtonColors(),
            ) {
                Text("Poems")
            }
            OutlinedButton(
                onClick = { updateBroker(Broker.Moomoo) },
                colors = if (selectedBroker?.name == Broker.Moomoo.name) selectedButtonColors else ButtonDefaults.outlinedButtonColors(),
            ) {
                Text("Moomoo")


            }

        }
    }
}