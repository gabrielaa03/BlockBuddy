package com.gabrielaangebrandt.blockbuddy.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.gabrielaangebrandt.blockbuddy.utils.SharedPrefsHelper

class SettingsFragmentViewModel(
    private val sharedPrefsHelper: SharedPrefsHelper
) : ViewModel() {

    fun saveSettings(option: Setting, state: Boolean) {
        when (option) {
            Setting.ALLOW_CONTACTS_ONLY_CALL -> {
                sharedPrefsHelper.allowContactsOnlyCall = state
            }
            Setting.ALLOW_CONTACTS_ONLY_SMS -> {
                sharedPrefsHelper.allowContactsOnlySms = state
            }
        }
    }

    fun isNumberValid(number: String) =
        Patterns.PHONE.matcher(number).matches()

    fun blockNumber(number: String) {
        // TODO: Save to DB
    }
}

enum class Setting {
    ALLOW_CONTACTS_ONLY_CALL,
    ALLOW_CONTACTS_ONLY_SMS
}