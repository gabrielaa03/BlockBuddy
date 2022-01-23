package com.gabrielaangebrandt.blockbuddy.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gabrielaangebrandt.blockbuddy.R
import com.gabrielaangebrandt.blockbuddy.model.ProcessState

class MainFragmentViewModel : ViewModel() {

    var processState = MutableLiveData<ProcessState>()
    var isProcessing = false

    fun changeState() {
        isProcessing = !isProcessing
        setupUI()
    }

    fun setupUI() {
        val imageRes: Int
        val textRes: Int

        if (isProcessing) {
            imageRes = R.drawable.ic_stop
            textRes = R.string.press_button_to_stop_processing
        } else {
            imageRes = R.drawable.ic_start
            textRes = R.string.press_button_to_start_processing
        }

        processState.postValue(ProcessState(imageRes, textRes))
    }
}