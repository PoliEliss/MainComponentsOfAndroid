package com.rorono.maincomponentsofandroid.screens

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.rorono.maincomponentsofandroid.R
import com.rorono.maincomponentsofandroid.ServiceTimer
import com.rorono.maincomponentsofandroid.databinding.FragmentServiceTimerBinding
import com.rorono.maincomponentsofandroid.utils.BaseViewBindingFragment
import com.rorono.maincomponentsofandroid.viewmodel.MainViewModel
import com.rorono.maincomponentsofandroid.viewmodel.MainViewModelFactory


class ServiceTimerFragment : BaseViewBindingFragment<FragmentServiceTimerBinding>(
    FragmentServiceTimerBinding::inflate
) {
    private lateinit var viewModel: MainViewModel
    private var time = 0.0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModelFactory = MainViewModelFactory()
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[MainViewModel::class.java]

        getButtonRendering(viewModel.stateTimer.value as Boolean)

        viewModel.stateTimer.observe(viewLifecycleOwner) {
            getButtonRendering(it)
        }
        binding.buttonStartAndStopTimer.setOnClickListener {
            startAndStopTimer()
        }
        binding.buttonResetTimer.setOnClickListener {
            resetTimer()
            binding.buttonResetTimer.visibility = View.GONE
        }
        activity?.registerReceiver(updateTime, IntentFilter(ServiceTimer.TIMER_UPDATE))
        viewModel.time.observe(viewLifecycleOwner) {
            binding.tvTimer.text = it
        }
    }

    private val updateTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            time = intent?.getDoubleExtra(ServiceTimer.EXTRA_TIMER, 0.0) ?: 0.0
            viewModel.getTimerStringFromDouble(time)
        }
    }

    private fun resetTimer() {
        stopTimer()
        time = 0.0
        viewModel.getTimerStringFromDouble(time)
    }

    private fun startAndStopTimer() {
        if (viewModel.stateTimer.value as Boolean) {
            stopTimer()
        } else
            startTimer()
    }

    private fun startTimer() {
        activity?.startService(ServiceTimer.newIntentTimer(requireContext(), time))
        viewModel.getStateTimer(true)
    }

    private fun stopTimer() {
        activity?.stopService(ServiceTimer.newIntentTimer(requireContext(), time))
        viewModel.getStateTimer(false)
    }

    private fun getButtonRendering(timeStarted: Boolean) {
        when (timeStarted) {
            false -> {
                binding.buttonStartAndStopTimer.setImageResource(R.drawable.ic__play_arrow_24)
                binding.buttonStartAndStopTimer.setBackgroundResource(R.drawable.custom_button_start)
            }
            true -> {
                binding.buttonStartAndStopTimer.setImageResource(R.drawable.ic_pause_24)
                binding.buttonStartAndStopTimer.setBackgroundResource(R.drawable.custom_button_pause)
                binding.buttonResetTimer.visibility = View.VISIBLE
            }
        }
    }
}