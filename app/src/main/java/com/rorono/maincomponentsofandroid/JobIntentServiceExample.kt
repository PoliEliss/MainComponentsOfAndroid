package com.rorono.maincomponentsofandroid

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import kotlinx.coroutines.*


class JobIntentServiceExample : JobIntentService() {
    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }

    override fun onHandleWork(intent: Intent) {
        log("onHandleIntent")
        val page = intent.getIntExtra(PAGE, 0)
        for (i in 0..100) {
            Thread.sleep(1000)
            log("timer $i $page")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
    }


    private fun log(message: String) {
        Log.d("SERVICE", "ForegroundService: $message")
    }


    companion object {
        private const val SERVICE_NAME = "JobIntentService"
        private const val PAGE = "page"
        private const val JOB_ID = 2

        fun enqueue(context: Context, page: Int) {
            enqueueWork(
                context,
                JobIntentServiceExample::class.java,
                JOB_ID,
                newIntent(context, page)
            )
        }

        private fun newIntent(context: Context, page: Int): Intent {
            return Intent(context, JobIntentServiceExample::class.java).apply {
                putExtra(PAGE, page)
            }
        }
    }
}
