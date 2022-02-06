package com.gabrielaangebrandt.blockbuddy.utils

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private const val SERVICE_RUNNING = "ServiceRunning"
private const val ALLOW_CONTACTS_ONLY_CALL = "AllowContactsOnlyCall"
private const val BLOCKED_NUMBERS = "BlockedNumbers"

class SharedPrefsHelper(
    private val preferences: SharedPreferences,
    private val gson: Gson
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

    var blockedNumbers: List<String> = emptyList()
        get() {
            val json = preferences.getString(BLOCKED_NUMBERS, "")
            return if (json.isNullOrEmpty()) {
                emptyList()
            } else {
                gson.fromJson(json, object : TypeToken<List<String?>?>() {}.type)
            }
        }
        set(value) {
            val json: String = gson.toJson(value)
            preferences.edit()
                .putString(BLOCKED_NUMBERS, json)
                .apply()
            field = value
        }
}