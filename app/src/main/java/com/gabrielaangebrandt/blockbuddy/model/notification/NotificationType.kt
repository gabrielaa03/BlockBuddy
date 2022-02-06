package com.gabrielaangebrandt.blockbuddy.model.notification

enum class NotificationType(val channelId: String) {
    FOREGROUND_SERVICE_NOTIFICATION("BlockBuddyChannelId"),
    CALL_DETAILS_NOTIFICATION("BlockBuddyCallDetailsChannelId")
}