package com.gabrielaangebrandt.blockbuddy.model.processing

import android.provider.CallLog.Calls.*
import com.gabrielaangebrandt.blockbuddy.R

data class CallLogModel(
    val type: Int,
    val name: String,
    val number: String,
    val time: String
) {
    companion object {
        fun CallLogModel.getColor() =
            when (this.type) {
                INCOMING_TYPE,
                ANSWERED_EXTERNALLY_TYPE -> R.color.green
                MISSED_TYPE -> R.color.orange
                VOICEMAIL_TYPE -> R.color.yellow
                BLOCKED_TYPE, REJECTED_TYPE -> R.color.red
                else -> R.color.black
            }
    }
}