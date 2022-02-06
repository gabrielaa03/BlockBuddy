package com.gabrielaangebrandt.blockbuddy.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import android.util.Log
import com.android.internal.telephony.ITelephony
import com.gabrielaangebrandt.blockbuddy.TAG
import com.gabrielaangebrandt.blockbuddy.broadcastreceiver.CallListener
import com.gabrielaangebrandt.blockbuddy.broadcastreceiver.PHONE_STATE
import com.gabrielaangebrandt.blockbuddy.broadcastreceiver.PhoneStateReceiver
import com.gabrielaangebrandt.blockbuddy.model.notification.NotificationType
import com.gabrielaangebrandt.blockbuddy.utils.NotificationHelper
import com.gabrielaangebrandt.blockbuddy.utils.ProcessingManager
import com.gabrielaangebrandt.blockbuddy.utils.SharedPrefsHelper
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.lang.reflect.Method


private const val SERVICE_ID = 1
private const val CALL_DETAILS_ID = 2

class ProcessingService : Service(), CallListener, KoinComponent {

    private val phoneStateReceiver: PhoneStateReceiver by lazy {
        PhoneStateReceiver()
    }

    private val notificationHelper: NotificationHelper by inject()
    private val sharedPrefsHelper: SharedPrefsHelper by inject()
    private val processingManager: ProcessingManager by inject()

    override fun onCreate() {
        super.onCreate()
        startForeground(
            SERVICE_ID,
            notificationHelper.postNotification(NotificationType.FOREGROUND_SERVICE_NOTIFICATION)
        )

        phoneStateReceiver.setListener(this@ProcessingService)

        registerReceiver(
            phoneStateReceiver,
            IntentFilter(PHONE_STATE)
        )
    }

    override fun onDestroy() {
        unregisterReceiver(phoneStateReceiver)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCallFinished(number: String) {
        val isSuspicious = processingManager.isNumberSuspicious(number)
        if (isSuspicious) {
            notificationHelper.discardNotification()
        }
    }

    override fun onCallReceived(number: String) {
        Log.d(TAG, "Call received")

        val isScam = processingManager.isNumberPotentialScam(number)
        val isNumberBlockedLocally = processingManager.isNumberBlocked(number)
        val isSuspicious = processingManager.isNumberSuspicious(number)

        if (isScam || isNumberBlockedLocally) {
            endCall()
        } else if (isSuspicious) {
            showExtraDetails()
        } else {
            val isNumberAContact = processingManager.isNumberOnContactList(number)
            if (!isNumberAContact && sharedPrefsHelper.allowContactsOnlyCall) {
                endCall()
            }
        }
    }

    // permission already checked
    @SuppressLint("MissingPermission")
    private fun endCall() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val telecomManager = getSystemService(TELECOM_SERVICE) as TelecomManager
            telecomManager.endCall()
        } else {
            val telephony = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            try {
                val clazz = Class.forName(telephony.javaClass.name)
                val method: Method = clazz.getDeclaredMethod(TELEPHONY_METHOD).apply {
                    isAccessible = true
                }
                (method.invoke(telephony) as ITelephony).run {
                    endCall()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun showExtraDetails() {
        val notification =
            notificationHelper.postNotification(NotificationType.CALL_DETAILS_NOTIFICATION)
        notificationHelper.notify(CALL_DETAILS_ID, notification)
    }

    companion object {
        private const val TELEPHONY_METHOD = "getITelephony"
    }
}