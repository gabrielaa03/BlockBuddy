package com.gabrielaangebrandt.blockbuddy.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.gabrielaangebrandt.blockbuddy.TAG
import com.gabrielaangebrandt.blockbuddy.service.ProcessingService
import com.gabrielaangebrandt.blockbuddy.utils.SharedPrefsHelper
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PhoneBootReceiver : BroadcastReceiver(), KoinComponent {

    private val sharedPrefsHelper: SharedPrefsHelper by inject()

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if (Intent.ACTION_BOOT_COMPLETED == it.action
                && sharedPrefsHelper.serviceRunning
            ) {
                startService(context)
            } else {
                Log.d(TAG, "Action received: ${it.action}")
            }
        } ?: Log.e(TAG, "Intent is not initialized.")
    }

    private fun startService(context: Context?) {
        context?.run {
            val intent = Intent(this, ProcessingService::class.java)
            startForegroundService(intent)
        } ?: Log.e(TAG, "Context is null")
    }
}