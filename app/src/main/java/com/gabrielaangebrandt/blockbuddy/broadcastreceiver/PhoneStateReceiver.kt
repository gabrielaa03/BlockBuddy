package com.gabrielaangebrandt.blockbuddy.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.widget.Toast

class PhoneStateReceiver : BroadcastReceiver() {

    private lateinit var listener: CallListener

    fun setListener(listener: CallListener) {
        this.listener = listener
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val state = intent?.getStringExtra(TelephonyManager.EXTRA_STATE)
        try {
            println("Receiver start")
            Toast.makeText(context, " Receiver start ", Toast.LENGTH_SHORT).show()
            state?.let { it -> listener.onCallReceived(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}