package com.gabrielaangebrandt.blockbuddy.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.gabrielaangebrandt.blockbuddy.R
import com.gabrielaangebrandt.blockbuddy.model.notification.NotificationData
import com.gabrielaangebrandt.blockbuddy.model.notification.NotificationType
import com.gabrielaangebrandt.blockbuddy.view.activity.MainActivity

class NotificationHelper(private val context: Context) {

    private val notificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    fun postNotification(notificationType: NotificationType): Notification {
        val notificationData = getNotificationData(notificationType)
        val notificationBuilder = createNotificationBuilder(notificationData)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = createChannel(notificationData.channelId)
            notificationManager.createNotificationChannel(channel)
        }
        return notificationBuilder.build()
    }

    private fun createNotificationBuilder(notificationData: NotificationData) =
        NotificationCompat.Builder(context, notificationData.channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(context.getString(notificationData.title))
            .setContentText(context.getString(notificationData.title))
            .setSound(null)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(openMainActivity())

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(id: String) =
        NotificationChannel(
            id,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            setSound(null, null)
        }

    private fun getNotificationData(notificationType: NotificationType) =
        when (notificationType) {
            NotificationType.CALL_DETAILS_NOTIFICATION -> {
                NotificationData(
                    channelId = CALL_DETAILS_CHANNEL_ID,
                    title = R.string.suspicious_call,
                    description = R.string.be_careful_this_number_is_not_on_your_contact_list,
                    icon = R.drawable.ic_warning
                )
            }
            NotificationType.FOREGROUND_SERVICE_NOTIFICATION -> {
                NotificationData(
                    channelId = APP_CHANNEL_ID,
                    title = R.string.app_notification_title,
                    description = R.string.app_notification_description,
                    icon = R.drawable.app_icon
                )
            }
        }

    private fun openMainActivity(): PendingIntent =
        PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java),
            FLAG_UPDATE_CURRENT
        )

    companion object {
        private const val APP_CHANNEL_ID = "BlockBuddyChannelID"
        private const val CALL_DETAILS_CHANNEL_ID = "BlockBuddyCallDetailsChannelID"
        private const val CHANNEL_NAME = "BlockBuddyChannel"
    }
}