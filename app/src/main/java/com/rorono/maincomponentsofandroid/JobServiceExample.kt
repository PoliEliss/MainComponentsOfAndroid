package com.rorono.maincomponentsofandroid

import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.PersistableBundle
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            coroutineScope.launch {
                var workItem = p0?.dequeueWork()
                while (workItem != null) {
                    val page = workItem.intent.getIntExtra(PAGE, 0)
                    for (i in 0..5) {
                        delay(100)
                        log("timer $i $page")
                    }
                    p0?.completeWork(workItem)
                    workItem = p0?.dequeueWork()
                }
                jobFinished(p0, false)
            }
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
        Log.d("SERVICE", "JobService: $message")
    }

    companion object {
        private const val PAGE = "page"
        fun newIntent(context: Context, page: Int): Intent {
            return Intent().apply {
                putExtra(PAGE, page)
            }

        }
    }


}