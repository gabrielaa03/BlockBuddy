package com.gabrielaangebrandt.blockbuddy.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

class BackgroundProcessingService : Service() {
    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}