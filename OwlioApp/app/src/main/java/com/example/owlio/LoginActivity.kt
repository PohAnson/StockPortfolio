package com.example.owlio

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
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

            val snackbarHostState = remember { SnackbarHostState() }
            val snackbarDelegate by remember {
                SnackbarDelegate(
                    snackbarHostState = snackbarHostState,
                )
            }
            snackbarDelegate.apply {
                coroutineScope = rememberCoroutineScope()
            }
            val vm: LoginViewModel = hiltViewModel()
            val isLogout = intent.getBooleanExtra("isLogout", false)
            if (isLogout) vm.logout()


            OwlioAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Scaffold(snackbarHost = {
                        SnackbarHost(snackbarDelegate.snackbarHostState) { snackbarData ->
                            Snackbar(
                                snackbarData,
                                containerColor = snackbarDelegate.msnackbarState.backgroundColor
                            )
                        }
                    }) { innerpadding ->
                        LoginScreen(isCredentialPresent = vm.isCredentialPresent(),
                            vm = vm,
                            modifier = Modifier.padding(innerpadding),
                            snackbarDelegate = snackbarDelegate,
                            onSuccess = {
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            })
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
