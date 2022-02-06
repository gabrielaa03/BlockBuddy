package com.gabrielaangebrandt.blockbuddy.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import com.gabrielaangebrandt.blockbuddy.R
import com.gabrielaangebrandt.blockbuddy.TAG
import com.gabrielaangebrandt.blockbuddy.utils.SharedPrefsHelper
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

const val PHONE_STATE = "android.intent.action.PHONE_STATE"

class PhoneStateReceiver : BroadcastReceiver(), KoinComponent {

    private val sharedPrefsHelper: SharedPrefsHelper by inject()
    private var listener: CallListener? = null
    private var context: Context? = null

    fun setListener(listener: CallListener) {
        this.listener = listener
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        this.context = context
        if (sharedPrefsHelper.serviceRunning) {
            checkPhoneState(intent)
        } else {
            Log.d(TAG, "Processing has not been started.")
        }
    }

    private fun checkPhoneState(intent: Intent?) {
        if (intent?.action == PHONE_STATE) {
            val phoneState: String = intent.getStringExtra(TelephonyManager.EXTRA_STATE) ?: return
            val phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            when (phoneState) {
                TelephonyManager.EXTRA_STATE_RINGING ->
                    onPhoneRinging(phoneNumber)
                TelephonyManager.EXTRA_STATE_OFFHOOK ->
                    onPhoneAnswered(phoneNumber)
                TelephonyManager.EXTRA_STATE_IDLE ->
                    onPhoneCallEnded(phoneNumber)
            }
        } else {
            Log.d(TAG, "Action received: ${intent?.action}")
        }
    }

    private fun onPhoneRinging(phoneNumber: String?) {
        phoneNumber?.let {
            listener?.onCallReceived(it)
        }

        Log.d(TAG, "Phone ringing.")
    }

    private fun onPhoneAnswered(phoneNumber: String?) {
        Toast.makeText(context, R.string.call_ended, Toast.LENGTH_SHORT).show()
        phoneNumber?.let {
            listener?.onCallFinished(it)
        }
        Log.d(TAG, "Call answered.")
    }

    private fun onPhoneCallEnded(phoneNumber: String?) {
        Toast.makeText(context, R.string.call_ended, Toast.LENGTH_SHORT).show()
        phoneNumber?.let {
            listener?.onCallFinished(it)
        }
        Log.d(TAG, "Phone call ended.")
    }
}