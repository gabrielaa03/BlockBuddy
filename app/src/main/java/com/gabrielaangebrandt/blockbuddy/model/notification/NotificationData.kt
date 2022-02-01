package com.gabrielaangebrandt.blockbuddy.model.notification

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class NotificationData(
    @StringRes var title: Int,
    @StringRes var description: Int,
    @DrawableRes var icon: Int,
    var channelId: String
)