package com.gabrielaangebrandt.blockbuddy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabrielaangebrandt.blockbuddy.model.processing.CallLogModel
import com.gabrielaangebrandt.blockbuddy.utils.ProcessingManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryFragmentViewModel(
    private val processingManager: ProcessingManager
) : ViewModel() {

    private var _callLogs = MutableLiveData<List<CallLogModel>>()
    val callLogs: LiveData<List<CallLogModel>>
        get() = _callLogs

    fun filterCallLogs() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _callLogs.postValue(processingManager.getCallLogs())
            }
        }
    }
}