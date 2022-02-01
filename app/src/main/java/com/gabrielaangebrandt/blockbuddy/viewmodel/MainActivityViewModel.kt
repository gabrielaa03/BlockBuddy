package com.gabrielaangebrandt.blockbuddy.viewmodel

import androidx.lifecycle.ViewModel
import com.gabrielaangebrandt.blockbuddy.model.processing.CallState
import com.gabrielaangebrandt.blockbuddy.model.notification.NotificationType
import com.gabrielaangebrandt.blockbuddy.utils.NotificationHelper

private const val SUSPICIOUS_CALL_MOCK = "425-950-1212"
private const val SCAM_CALL_MOCK = "253-950-1212"

class MainActivityViewModel(
    private val notificationManager: NotificationHelper
) : ViewModel() {

    fun processCall(number: String) {
        when (getCallState(number)) {
            CallState.NORMAL -> return
            CallState.SUSPICIOUS ->
                notificationManager.postNotification(NotificationType.CALL_DETAILS_NOTIFICATION)
            CallState.SCAM -> blockCall()
        }
    }

    private fun getCallState(number: String): CallState =
        when (number) {
            SUSPICIOUS_CALL_MOCK -> CallState.SUSPICIOUS
            SCAM_CALL_MOCK -> CallState.SCAM
            else -> CallState.NORMAL
        }

    private fun blockCall() {

    }
}