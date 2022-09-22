package com.rorono.maincomponentsofandroid

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager

class BroadcastReceiver : BroadcastReceiver() {
    private var batteryLevel: Int = 0

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.action
        val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = intent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1)

        if (level != null && scale != null) {
            batteryLevel = level * 100 / scale
        }
    }

    fun checkPhoneChargeLevel(): Int {
        return batteryLevel
    }
}