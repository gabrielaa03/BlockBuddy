package com.gabrielaangebrandt.blockbuddy.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gabrielaangebrandt.blockbuddy.R
import com.gabrielaangebrandt.blockbuddy.model.ProcessState
import com.gabrielaangebrandt.blockbuddy.utils.SharedPrefsHelper

class ManageProcessingFragmentViewModel(
    private val sharedPrefsHelper: SharedPrefsHelper
) : ViewModel() {

    var processState = MutableLiveData<ProcessState>()
    val isServiceRunning: Boolean
        get() = sharedPrefsHelper.serviceRunning

    fun changeState() {
        sharedPrefsHelper.serviceRunning = !sharedPrefsHelper.serviceRunning
        setupUI()
    }

    fun setupUI() {
        val imageRes: Int
        val textRes: Int

        if (sharedPrefsHelper.serviceRunning) {
            imageRes = R.drawable.ic_stop
            textRes = R.string.press_button_to_stop_processing
        } else {
            imageRes = R.drawable.ic_start
            textRes = R.string.press_button_to_start_processing
        }

        processState.postValue(ProcessState(imageRes, textRes))
    }
}