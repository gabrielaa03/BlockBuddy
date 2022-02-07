package com.gabrielaangebrandt.blockbuddy.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.gabrielaangebrandt.blockbuddy.R
import com.gabrielaangebrandt.blockbuddy.model.notification.NotificationData
import com.gabrielaangebrandt.blockbuddy.model.notification.NotificationType
import com.gabrielaangebrandt.blockbuddy.view.activity.MainActivity

class NotificationHelper(private val context: Context) {

    private var notificationManager: NotificationManager? = null

    fun postNotification(notificationType: NotificationType): Notification {
        val notification = createNotification(notificationType)

        val channel = NotificationChannel(
            notificationType.channelId,
            context.getString(R.string.channel_name),
            getImportance(notificationType)
        )
        notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager?.createNotificationChannel(channel)

        return notification
    }

    fun notify(id: Int, notification: Notification) {
        notificationManager?.notify(id, notification)
    }

    private fun createNotification(notificationType: NotificationType): Notification {
        val data = getNotificationData(notificationType)

        return NotificationCompat.Builder(context, notificationType.channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(context.getString(data.title))
            .setContentText(context.getString(data.description))
            .setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setContentIntent(openMainActivity())
            .setCategory(Notification.CATEGORY_CALL)
            .setOngoing(isOnGoing(notificationType))
            .setPriority(getPriority(notificationType))
            .build()
    }

    fun discardNotification() {
        notificationManager?.deleteNotificationChannel(
            NotificationType.CALL_DETAILS_NOTIFICATION.channelId
        )
    }

    private fun getNotificationData(notificationType: NotificationType) =
        when (notificationType) {
            NotificationType.CALL_DETAILS_NOTIFICATION -> {
                NotificationData(
                    title = R.string.suspicious_call,
                    description = R.string.be_careful_this_number_is_not_on_your_contact_list,
                    icon = R.drawable.ic_warning
                )
            }
            NotificationType.FOREGROUND_SERVICE_NOTIFICATION -> {
                NotificationData(
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
            FLAG_IMMUTABLE
        )

    private fun isOnGoing(notificationType: NotificationType) =
        notificationType == NotificationType.CALL_DETAILS_NOTIFICATION


    private fun getImportance(notificationType: NotificationType) =
        if (notificationType == NotificationType.FOREGROUND_SERVICE_NOTIFICATION) {
            NotificationManager.IMPORTANCE_DEFAULT
        } else {
            NotificationManager.IMPORTANCE_HIGH
        }

    private fun getPriority(notificationType: NotificationType) =
        if (notificationType == NotificationType.FOREGROUND_SERVICE_NOTIFICATION) {
            NotificationCompat.PRIORITY_DEFAULT
        } else {
            NotificationCompat.PRIORITY_HIGH
        }
}