package com.rorono.maincomponentsofandroid.screens

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rorono.maincomponentsofandroid.R
import com.rorono.maincomponentsofandroid.ServiceTimer
import com.rorono.maincomponentsofandroid.databinding.FragmentServiceTimerBinding
import kotlin.math.roundToInt


class ServiceTimerFragment : Fragment() {

    private var time = 0.0
    private var timeStarted = false

    private var _binding: FragmentServiceTimerBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentServiceTimerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonStartAndStopTimer.setOnClickListener {
            startAndStopTimer()
        }
        binding.buttonResetTimer.setOnClickListener {
            resetTimer()
            binding.buttonResetTimer.visibility = View.GONE
        }

        activity?.registerReceiver(updateTime, IntentFilter(ServiceTimer.TIMER_UPDATE))
    }

    private val updateTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            time = intent?.getDoubleExtra(ServiceTimer.EXTRA_TIMER, 0.0) ?: 0.0
            binding.tvTimer.text = getTimerStringFromDouble(time)
        }

    }

    private fun resetTimer() {
        stopTimer()
        time = 0.0
        binding.tvTimer.text = getTimerStringFromDouble(time)
    }


    private fun startAndStopTimer() {
        if (timeStarted) {
            stopTimer()
        } else
            startTimer()
    }

    private fun getTimerStringFromDouble(time: Double): String {
        val resultInt = time.roundToInt()
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60
        Log.d("TEST", "hours $hours")
        Log.d("TEST", "minutes $minutes")
        Log.d("TEST", "seconds $seconds")
        return makeTimeString(hours, minutes, seconds)

    }


    private fun makeTimeString(hour: Int, min: Int, sec: Int): String {
        return String.format("%02d:%02d:%02d", hour, min, sec)
    }

    private fun startTimer() {
        activity?.startService(ServiceTimer.newIntentTimer(requireContext(), time))
        binding.buttonStartAndStopTimer.setImageResource(R.drawable.ic_pause_24)
        binding.buttonStartAndStopTimer.setBackgroundResource(R.drawable.custom_button_pause)
        binding.buttonResetTimer.visibility = View.VISIBLE
        timeStarted = true
    }

    private fun stopTimer() {
        activity?.stopService(ServiceTimer.newIntentTimer(requireContext(), time))
        binding.buttonStartAndStopTimer.setImageResource(R.drawable.ic__play_arrow_24)
        binding.buttonStartAndStopTimer.setBackgroundResource(R.drawable.custom_button_start)
        timeStarted = false
    }

}