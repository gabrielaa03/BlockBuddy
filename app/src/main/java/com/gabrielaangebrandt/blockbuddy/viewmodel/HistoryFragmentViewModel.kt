package com.gabrielaangebrandt.blockbuddy.viewmodel

import androidx.lifecycle.ViewModel
import com.gabrielaangebrandt.blockbuddy.model.CallLog
import com.gabrielaangebrandt.blockbuddy.model.CallState

class HistoryFragmentViewModel : ViewModel() {

    fun filterCallLogs(): List<CallLog> {
        return listOf(
            CallLog(
                CallState.NORMAL,
                "MOM",
                "233-2333-1233",
                "Yesteday"
            ),
            CallLog(
                CallState.SUSPICIOUS,
                "DAD",
                "233-2333-1233",
                "Today"
            ),
            CallLog(
                CallState.SCAM,
                "LUKA",
                "233-2333-1233",
                "Today"
            )
        )
    }
}