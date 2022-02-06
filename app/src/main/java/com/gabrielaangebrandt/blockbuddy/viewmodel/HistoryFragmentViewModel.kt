package com.gabrielaangebrandt.blockbuddy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gabrielaangebrandt.blockbuddy.model.processing.CallLogModel
import com.gabrielaangebrandt.blockbuddy.utils.ProcessingManager

class HistoryFragmentViewModel(
    private val processingManager: ProcessingManager
) : ViewModel() {

    private var _callLogs = MutableLiveData<List<CallLogModel>>()
    val callLogs: LiveData<List<CallLogModel>>
        get() = _callLogs

    fun filterCallLogs() {
        _callLogs.postValue(processingManager.getCallLogs())
    }
}