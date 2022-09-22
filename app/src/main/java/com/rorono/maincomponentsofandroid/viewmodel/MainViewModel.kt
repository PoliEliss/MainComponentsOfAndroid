package com.rorono.maincomponentsofandroid.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.roundToInt

class MainViewModel() : ViewModel() {

    val stateTimer = MutableLiveData<Boolean>(false)

    val time = MutableLiveData<String>()

    fun getStatFun(state: Boolean) {
        stateTimer.value = state
    }

    private fun makeTimeString(hour: Int, min: Int, sec: Int) {
        time.value = String.format("%02d:%02d:%02d", hour, min, sec)
    }
     fun getTimerStringFromDouble(time: Double) {
        val resultInt = time.roundToInt()
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60
     makeTimeString(hours, minutes, seconds)
    }

}