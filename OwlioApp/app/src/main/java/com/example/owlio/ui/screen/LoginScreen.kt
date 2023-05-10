package com.example.owlio.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.owlio.R
import com.example.owlio.ui.SnackbarDelegate
import com.example.owlio.ui.SnackbarState


@Composable
fun LoginScreen(
    isCredentialPresent: Boolean,
    checkCredential: (String, String) -> Boolean,
    saveCredential: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    snackbarDelegate: SnackbarDelegate,
    onSuccess: () -> Unit,
    login: (String, String) -> Unit,
) {
    var isLoginForm by remember {
        mutableStateOf(true)
    }
    if (isCredentialPresent) {
        // Auto login
        onSuccess()
    } else {
        // Login Form
        if (isLoginForm) UserAuthForm(login = true,
            onSubmit = { username, password -> login(username, password) },
            toggleSignUpSignInForm = { isLoginForm = false })
        else
        // Signup Form
            UserAuthForm(login = false, modifier = modifier, onSubmit = { username, password ->
                if (username.isEmpty() && password.isEmpty()) {
                    snackbarDelegate.showSnackbar(
                        SnackbarState.ERROR, "Username/Password is empty!"
                    )
                } else {
                    saveCredential(username, password)
                    onSuccess()
                }
            }, toggleSignUpSignInForm = { isLoginForm = true })
    }
}

@Composable
private fun UserAuthForm(
    login: Boolean,
    modifier: Modifier = Modifier,
    onSubmit: (String, String) -> Unit,
    toggleSignUpSignInForm: () -> Unit = {}
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val formNameType = if (login) "Login" else "Sign Up"
    val focusManager = LocalFocusManager.current
    Column(
        modifier = modifier.fillMaxSize(1f), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // wave picture
        Image(
            painter = painterResource(R.drawable.wave),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxHeight(0.25f)
                .fillMaxWidth(1f),
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(formNameType, style = MaterialTheme.typography.h3)

        Spacer(modifier = Modifier.height(35.dp))


        // Username Field
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            placeholder = { Text("Username") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions { focusManager.moveFocus(FocusDirection.Next) },
        )
        Spacer(modifier = Modifier.height(18.dp))

        // Password Field
        OutlinedTextField(value = password,
            onValueChange = { password = it },
            placeholder = { Text("Password") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions {
                focusManager.clearFocus()
                onSubmit(username, password)
            })
        Spacer(modifier = Modifier.height(24.dp))


        // Submit Button
        Button(onClick = {
            focusManager.clearFocus()
            onSubmit(username, password)
        }) {
            Text(formNameType)
        }
        Spacer(modifier = Modifier.height(48.dp))


        TextButton(onClick = { toggleSignUpSignInForm() }) {
            if (login) Text("Sign up here")
            else Text("Sign in here")
        }

    }
}