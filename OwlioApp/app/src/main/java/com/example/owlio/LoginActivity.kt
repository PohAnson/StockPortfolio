package com.example.owlio

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Surface
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.owlio.ui.SnackbarDelegate
import com.example.owlio.ui.getValue
import com.example.owlio.ui.screen.LoginScreen
import com.example.owlio.ui.screen.LoginViewModel
import com.example.owlio.ui.theme.OwlioAppTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val snackbarDelegate by remember { SnackbarDelegate() }
            val scaffoldState = rememberScaffoldState()
            snackbarDelegate.apply {
                snackbarHostState = scaffoldState.snackbarHostState
                coroutineScope = rememberCoroutineScope()
            }
            val vm: LoginViewModel = hiltViewModel()
            val isClearCredential = intent.getBooleanExtra("isClearCredential", false)
            if (isClearCredential) vm.clearCredential()


            OwlioAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {

                    Scaffold(scaffoldState = scaffoldState, snackbarHost = {
                        SnackbarHost(it) { snackbarData ->
                            Snackbar(
                                snackbarData,
                                backgroundColor = snackbarDelegate.msnackbarState.backgroundColor
                            )
                        }
                    }) { innerpadding ->
                        LoginScreen(
                            isCredentialPresent = vm.isCredentialPresent(),
                            checkCredential = { username, password ->
                                vm.checkCredential(
                                    username,
                                    password
                                )
                            },
                            saveCredential = { username, password ->
                                vm.checkCredential(
                                    username,
                                    password
                                )
                            }, modifier = Modifier.padding(innerpadding),
                            snackbarDelegate,
                            onSuccess = {
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            },
                            login = { username, password -> vm.saveCredential(username, password) })
                    }
                }
            }
        }
    }


}


@Preview(showBackground = true)
@Composable
fun LoginPagePreview() {
    OwlioAppTheme { LoginActivity() }
}
