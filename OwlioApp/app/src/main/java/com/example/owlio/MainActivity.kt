package com.example.owlio

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.example.owlio.service.syncDatabaseService.syncDatabaseWorkRequest
import com.example.owlio.ui.theme.OwlioAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            OwlioAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    OwlioApp(onLogout = {
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.putExtra("isLogout", true)
                        startActivity(intent)
                        finish()
                    }, syncToServer = {
                        WorkManager.getInstance(this)
                            .enqueueUniqueWork(
                                "syncDatabaseWorker",
                                ExistingWorkPolicy.REPLACE,
                                syncDatabaseWorkRequest
                            )
                    })
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    OwlioAppTheme {}
}