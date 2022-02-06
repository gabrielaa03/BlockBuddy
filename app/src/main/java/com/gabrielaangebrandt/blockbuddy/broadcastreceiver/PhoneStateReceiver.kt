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

    fun setListener(listener: CallListener) {
        this.listener = listener
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (sharedPrefsHelper.serviceRunning) {
            checkPhoneState(intent, context)
        } else {
            Log.d(TAG, "Processing has not been started.")
        }
    }

    private fun checkPhoneState(intent: Intent?, context: Context?) {
        if (intent?.action == PHONE_STATE) {
            val phoneState = intent.getStringExtra(TelephonyManager.EXTRA_STATE) ?: return

            when (phoneState) {
                TelephonyManager.EXTRA_STATE_RINGING,
                TelephonyManager.EXTRA_STATE_OFFHOOK ->
                    onPhoneRinging(intent)
                TelephonyManager.EXTRA_STATE_IDLE ->
                    onPhoneCallEnded(context)
            }
        } else {
            Log.d(TAG, "Action received: ${intent?.action}")
        }
    }

    private fun onPhoneRinging(intent: Intent) {
        val phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
        phoneNumber?.let {
            listener?.onCallReceived(it)
        }

        Log.d(TAG, "Phone ringing.")
    }

    private fun onPhoneCallEnded(context: Context?) {
        Toast.makeText(context, R.string.call_ended, Toast.LENGTH_LONG).show()
        Log.d(TAG, "Phone call ended.")
    }
}