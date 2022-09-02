package com.rorono.maincomponentsofandroid

import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*


class JobServiceExample : JobService() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }


    override fun onStartJob(p0: JobParameters?): Boolean {
        log("onStartCommand")
        coroutineScope.launch {
            for (i in 0..12) {
                delay(100)
                log("timer $i")
            }
            jobFinished(p0, false)
        }
        return true
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        log("onStopJob")
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
        coroutineScope.cancel()
    }

    private fun log(message: String) {
        Log.d("SERVICE", "Service: $message")
    }


}