package com.example.owlio.ui

import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty


enum class SnackbarState(val backgroundColor: Color) {
    DEFAULT(Color.Gray), ERROR(Color.Red), SUCCESS(Color.Green),
}

class SnackbarDelegate(
    var snackbarHostState: SnackbarHostState? = null, var coroutineScope: CoroutineScope? = null
) {
    var msnackbarState: SnackbarState = SnackbarState.DEFAULT
    fun showSnackbar(
        snackbarState: SnackbarState,
        message: String,
        actionLabel: String? = null,
        duration: SnackbarDuration = SnackbarDuration.Short,
        onAction: () -> Unit = {}
    ) {
        msnackbarState = snackbarState
        coroutineScope?.launch {
            snackbarHostState?.showSnackbar(message, actionLabel, duration).let { snackbarResult ->
                if (snackbarResult == SnackbarResult.ActionPerformed) {
                    onAction()
                }
            }
        }
    }
}

operator fun SnackbarDelegate.getValue(thisRef: Any?, property: KProperty<*>) = this
