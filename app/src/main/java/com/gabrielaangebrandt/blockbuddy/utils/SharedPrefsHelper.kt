package com.gabrielaangebrandt.blockbuddy.utils

import android.content.SharedPreferences

private const val SERVICE_RUNNING = "ServiceRunning"
private const val ALLOW_CONTACTS_ONLY_CALL = "AllowContactsOnlyCall"

class SharedPrefsHelper(
    private val preferences: SharedPreferences
) {

    var serviceRunning: Boolean = false
        get() = preferences.getBoolean(SERVICE_RUNNING, false)
        set(value) {
            preferences.edit()
                .putBoolean(SERVICE_RUNNING, value)
                .apply()
            field = value
        }

    var allowContactsOnlyCall: Boolean = false
        get() = preferences.getBoolean(ALLOW_CONTACTS_ONLY_CALL, false)
        set(value) {
            preferences.edit()
                .putBoolean(ALLOW_CONTACTS_ONLY_CALL, value)
                .apply()
            field = value
        }
}