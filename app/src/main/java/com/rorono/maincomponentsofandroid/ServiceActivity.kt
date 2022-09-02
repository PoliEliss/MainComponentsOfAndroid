package com.rorono.maincomponentsofandroid

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.rorono.maincomponentsofandroid.databinding.ActivityServiceBinding

class ServiceActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityServiceBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.simpleService.setOnClickListener {
            stopService(ForegroundService.newIntent(this))
            startService(ServiceTimer.newIntent(this, 10))
        }
        binding.foregroundService.setOnClickListener {
            showNotification()
            ContextCompat.startForegroundService(this, ForegroundService.newIntent(this))
        }
        binding.intentService.setOnClickListener {
          ContextCompat.startForegroundService(this,
          IntentServiceExample.newIntent(this))
        }
    }

    private fun showNotification() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Уведомление о работе таймере")
            .setContentText("Text")
            .setSmallIcon(R.drawable.service_icon)
            .build()

        notificationManager.notify(1, notification)
    }

    companion object {
        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "channel_name"
    }
}
