package com.rorono.maincomponentsofandroid.screens

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import com.rorono.maincomponentsofandroid.BroadcastReceiver
import com.rorono.maincomponentsofandroid.R
import com.rorono.maincomponentsofandroid.databinding.FragmentBateryBinding
import com.rorono.maincomponentsofandroid.utils.BaseViewBindingFragment

class BatteryFragment :
    BaseViewBindingFragment<FragmentBateryBinding>(FragmentBateryBinding::inflate) {
    private val broadcastReceiver = BroadcastReceiver()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.registerReceiver(broadcastReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        binding.buttonGetBatteryLevel.setOnClickListener {
            val resultCheckPhoneChargeLevel =
                checkPhoneChargeLevel(broadcastReceiver = broadcastReceiver)
            binding.batteryStatus.text = convertDataToString(resultCheckPhoneChargeLevel)
            displayingImage(resultCheckPhoneChargeLevel)
        }
    }

    private fun checkPhoneChargeLevel(broadcastReceiver: BroadcastReceiver): Int {
        return broadcastReceiver.checkPhoneChargeLevel()
    }

    private fun <T> convertDataToString(data: T): String {
        return data.toString() + getString(R.string.percent)
    }

    private fun displayingImage(percent: Int) {
        binding.imageViewBatteryLevel.visibility = View.VISIBLE
        when (percent) {
            in 50..100 -> binding.imageViewBatteryLevel.setImageResource(R.drawable.ic_full_baterry)
            in 50 downTo 25 -> binding.imageViewBatteryLevel.setImageResource(R.drawable.ic_average_battery_4_bar_24)
            in 25 downTo 0 -> binding.imageViewBatteryLevel.setImageResource(R.drawable.ic_low_battery_2_bar_24)
        }
    }
}

