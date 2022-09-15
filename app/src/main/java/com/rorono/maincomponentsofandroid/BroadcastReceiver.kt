package com.rorono.maincomponentsofandroid

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.BatteryManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.rorono.maincomponentsofandroid.screens.ContentProviderFragment

class BroadcastReceiver : BroadcastReceiver() {
    var batteryLevel: Float = 0.0f

    override fun onReceive(context: Context?, intent: Intent?) {
        val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = intent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1)

        if (level != null && scale != null) {
            batteryLevel = level * 100 / scale.toFloat()
        }
    }

    fun checkPhoneChargeLevel(): Float {
        return batteryLevel
    }
}