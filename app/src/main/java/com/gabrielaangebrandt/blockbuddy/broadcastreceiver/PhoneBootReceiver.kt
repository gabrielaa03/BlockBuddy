package com.gabrielaangebrandt.blockbuddy.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.gabrielaangebrandt.blockbuddy.TAG
import com.gabrielaangebrandt.blockbuddy.service.BackgroundProcessingService
import org.koin.core.component.KoinComponent

class PhoneBootReceiver : BroadcastReceiver(), KoinComponent {

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if (Intent.ACTION_BOOT_COMPLETED == it.action) {
                val service = Intent(
                    context,
                    BackgroundProcessingService::class.java
                )
                context?.startService(service) ?: Log.e(TAG, "Context is not initialized.")
            } else {
                Log.d(TAG, "Action received: ${it.action}")
            }
        } ?: Log.e(TAG, "Intent is not initialized.")
    }
}