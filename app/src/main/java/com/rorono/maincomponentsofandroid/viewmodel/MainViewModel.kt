package com.rorono.maincomponentsofandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rorono.maincomponentsofandroid.models.Contact
import kotlin.math.roundToInt

class MainViewModel : ViewModel() {

    private val _stateTimer = MutableLiveData(false)
    val stateTimer: LiveData<Boolean>
        get() = _stateTimer

    private val _time = MutableLiveData<String>()
    val time: LiveData<String>
        get() = _time

    fun getStateTimer(state: Boolean) {
        _stateTimer.value = state
    }

    private val _listContacts = MutableLiveData<List<Contact>>()
    val listContact: LiveData<List<Contact>>
        get() = _listContacts


    private fun makeTimeString(hour: Int, min: Int, sec: Int) {
        _time.value = String.format("%02d:%02d:%02d", hour, min, sec)
    }

    fun getTimerStringFromDouble(time: Double) {
        val resultInt = time.roundToInt()
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60
        makeTimeString(hours, minutes, seconds)
    }

    fun getContacts(listContacts: MutableList<Contact>) {
        _listContacts.value = listContacts
    }
}