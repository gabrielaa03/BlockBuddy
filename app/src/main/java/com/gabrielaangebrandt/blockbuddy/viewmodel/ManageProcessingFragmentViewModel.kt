package com.gabrielaangebrandt.blockbuddy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gabrielaangebrandt.blockbuddy.R
import com.gabrielaangebrandt.blockbuddy.model.processing.ProcessState
import com.gabrielaangebrandt.blockbuddy.utils.SharedPrefsHelper

class ManageProcessingFragmentViewModel(
    private val sharedPrefsHelper: SharedPrefsHelper
) : ViewModel() {

    private var _processState = MutableLiveData<ProcessState>()
    val processState: LiveData<ProcessState>
        get() = _processState

    val isServiceRunning: Boolean
        get() = sharedPrefsHelper.serviceRunning

    fun changeServiceState() {
        sharedPrefsHelper.serviceRunning = !sharedPrefsHelper.serviceRunning
        setupUI()
    }

    fun setupUI() {
        val imageRes: Int
        val processingStateTextRes: Int
        val instructionsTextRes: Int

        if (sharedPrefsHelper.serviceRunning) {
            imageRes = R.drawable.background_stop_service
            processingStateTextRes = R.string.processing_calls
            instructionsTextRes = R.string.press_button_to_stop_processing
        } else {
            imageRes = R.drawable.background_start_service
            processingStateTextRes = R.string.processing_turned_off
            instructionsTextRes = R.string.press_button_to_start_processing
        }

        _processState.postValue(
            ProcessState(
                imageRes,
                processingStateTextRes,
                instructionsTextRes
            )
        )
    }
}