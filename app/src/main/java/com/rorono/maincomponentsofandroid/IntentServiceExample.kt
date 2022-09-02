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


class IntentServiceExample : IntentService(SERVICE_NAME) {
    override fun onCreate() {
        super.onCreate()
        log("onCreate")
        notificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())
    }

    override fun onHandleIntent(p0: Intent?) {
        log("onHandleIntent")
        for (i in 0..100) {
            Thread.sleep(1000)
            log("timer $i")
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
    }

    private fun log(message: String) {
        Log.d("SERVICE", "ForegroundService: $message")
    }


    private fun notificationChannel() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun createNotification() = NotificationCompat.Builder(this, CHANNEL_ID)
        .setContentTitle("Title2")
        .setContentText("text2")
        .setSmallIcon(R.drawable.service_icon)
        .build()


    companion object {

        private const val CHANNEL_ID = "channel_id_foreground"
        private const val NOTIFICATION_ID = 2
        private const val CHANNEL_NAME = "channel name2"
        private const val SERVICE_NAME = "Intent_Service"
        fun newIntent(context: Context): Intent {
            return Intent(context, IntentServiceExample::class.java).apply {
            }
        }
    }

}
