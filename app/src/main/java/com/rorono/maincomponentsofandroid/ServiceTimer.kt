package com.rorono.maincomponentsofandroid

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import java.util.*


class ServiceTimer : Service() {
    private val timer = Timer()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val time = intent?.getDoubleExtra(EXTRA_TIMER, 0.0) ?: 0.0
        timer.scheduleAtFixedRate(TimeTask(time), 0, 1000)
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    private inner class TimeTask(private var time: Double) : TimerTask() {
        override fun run() {
            val intent = Intent(TIMER_UPDATE)
            time++
            intent.putExtra(EXTRA_TIMER,time)
            sendBroadcast(intent)
        }
    }

    companion object {
        const val TIMER_UPDATE = "timerUpdate"
         const val EXTRA_TIMER = "startTimer"

        fun newIntentTimer(context: Context,startTimer:Double):Intent{
            return Intent(context,ServiceTimer::class.java).apply {
                putExtra(EXTRA_TIMER,startTimer)
            }
        }
    }
}