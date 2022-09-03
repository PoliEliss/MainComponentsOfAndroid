package com.rorono.maincomponentsofandroid

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.app.job.JobWorkItem
import android.content.ComponentName
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.rorono.maincomponentsofandroid.databinding.ActivityServiceBinding

class ServiceActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityServiceBinding.inflate(layoutInflater)
    }
    private var page = 0

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
            ContextCompat.startForegroundService(
                this,
                IntentServiceExample.newIntent(this)
            )
        }
        binding.jobScheduler.setOnClickListener {
            val componentName = ComponentName(this, JobServiceExample::class.java)
            val jobInfo = JobInfo.Builder(JOB_ID, componentName)
                .setRequiresCharging(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .build()

            val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val intent = JobServiceExample.newIntent(this, page++)
                jobScheduler.enqueue(jobInfo, JobWorkItem(intent))
            } else {
                stopService(IntentServiceExample2.newIntent(this, page++))
            }
        }
        binding.jobIntentService.setOnClickListener {
            JobIntentServiceExample.enqueue(this, page++)
        }

        binding.workManager.setOnClickListener {
            val workManager = WorkManager.getInstance(applicationContext)
            workManager.enqueueUniqueWork(
                WorkManagerExample.NAME_WORK_MANAGER,
                ExistingWorkPolicy.APPEND,
                WorkManagerExample.makeRequest(page = page)

            )
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
        private const val JOB_ID = 4
    }
}
