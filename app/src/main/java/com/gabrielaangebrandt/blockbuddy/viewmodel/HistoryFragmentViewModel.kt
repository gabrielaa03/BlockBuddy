package com.gabrielaangebrandt.blockbuddy.viewmodel

import android.database.Cursor
import android.provider.CallLog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gabrielaangebrandt.blockbuddy.model.processing.CallLogModel
import com.gabrielaangebrandt.blockbuddy.utils.convertToDate

class HistoryFragmentViewModel : ViewModel() {

    val projection = arrayOf(
        CallLog.Calls.CACHED_NAME,
        CallLog.Calls.NUMBER,
        CallLog.Calls.TYPE,
        CallLog.Calls.DATE
    )

    private var _callLogs = MutableLiveData<List<CallLogModel>>()
    val callLogs: LiveData<List<CallLogModel>>
        get() = _callLogs

    fun filterCallLogs(cursor: Cursor) {
        _callLogs.postValue(
            generateSequence {
                if (cursor.moveToNext()) {
                    cursor
                } else {
                    null
                }
            }.map { getStringFromCursor(it) }
                .filter { it.type != CallLog.Calls.OUTGOING_TYPE }
                .toList()
        )
    }

    private fun getStringFromCursor(cursor: Cursor): CallLogModel {
        val number: String = cursor.getString(projection.indexOf(CallLog.Calls.NUMBER))
        val name: String = cursor.getString(projection.indexOf(CallLog.Calls.CACHED_NAME))
        val time: String = cursor.getString(projection.indexOf(CallLog.Calls.DATE))
        val type: Int = cursor.getInt(projection.indexOf(CallLog.Calls.TYPE))
        return CallLogModel(
            type = type,
            number = number,
            name = name,
            time = time.toLong().convertToDate()
        )
    }
}