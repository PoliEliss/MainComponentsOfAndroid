package com.rorono.maincomponentsofandroid

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*


class IntentServiceExample2 : IntentService(SERVICE_NAME) {
    override fun onCreate() {
        super.onCreate()
        log("onCreate")
        setIntentRedelivery(true)
    }

    override fun onHandleIntent(p0: Intent?) {
        log("onHandleIntent")
        val page = p0?.getIntExtra(PAGE, 0) ?: 0
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


        private const val SERVICE_NAME = "Intent_Service"
        private const val PAGE = "page"

        fun newIntent(context: Context, page: Int): Intent {
            return Intent(context, IntentServiceExample2::class.java).apply {
                putExtra(PAGE, page)
            }
        }
    }

}
