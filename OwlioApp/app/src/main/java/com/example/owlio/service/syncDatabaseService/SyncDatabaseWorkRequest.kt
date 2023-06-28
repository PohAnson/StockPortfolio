package com.example.owlio.service.syncDatabaseService

import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import java.util.concurrent.TimeUnit

//val syncDatabaseWorkRequest: WorkRequest =
//    OneTimeWorkRequestBuilder<SyncDatabaseWorker>().setConstraints(
//        Constraints(requiredNetworkType = NetworkType.UNMETERED)
//    ).build()

val syncDatabaseWorkRequest =
    OneTimeWorkRequestBuilder<SyncDatabaseWorker>().setConstraints(
        Constraints(
            requiredNetworkType = NetworkType.UNMETERED
        )
    ).setBackoffCriteria(BackoffPolicy.LINEAR, 1, TimeUnit.MINUTES).build()
