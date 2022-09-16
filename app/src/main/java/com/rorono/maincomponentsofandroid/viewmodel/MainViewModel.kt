package com.rorono.maincomponentsofandroid.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel() : ViewModel() {

    val stateTimer = MutableLiveData<Boolean>(false)

    fun getStatFun(state: Boolean) {
        stateTimer.value = state
    }

}