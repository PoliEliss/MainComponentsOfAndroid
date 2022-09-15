package com.rorono.maincomponentsofandroid.screens

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import com.rorono.maincomponentsofandroid.BroadcastReceiver
import com.rorono.maincomponentsofandroid.databinding.FragmentBateryBinding
import com.rorono.maincomponentsofandroid.utils.BaseViewBindingFragment

class BatteryFragment :
    BaseViewBindingFragment<FragmentBateryBinding>(FragmentBateryBinding::inflate) {
    private val broadcastReceiver = BroadcastReceiver()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.registerReceiver(broadcastReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        binding.buttonGetBatteryLevel.setOnClickListener {
            binding.batteryStatus.text = broadcastReceiver.checkPhoneChargeLevel().toString() + "%"
        }
    }
}