package com.gabrielaangebrandt.blockbuddy.utils

import android.content.SharedPreferences

private const val SERVICE_RUNNING = "ServiceRunning"

class SharedPrefsHelper(private val preferences: SharedPreferences) {

    var serviceRunning: Boolean = false
        get() = preferences.getBoolean(SERVICE_RUNNING, false)
        set(value) {
            preferences.edit().putBoolean(SERVICE_RUNNING, value).apply()
            field = value
        }
}