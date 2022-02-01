package com.gabrielaangebrandt.blockbuddy.viewmodel

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gabrielaangebrandt.blockbuddy.R
import com.gabrielaangebrandt.blockbuddy.model.viewrendering.SettingsFragmentData
import com.gabrielaangebrandt.blockbuddy.utils.SharedPrefsHelper

class SettingsFragmentViewModel(
    private val sharedPrefsHelper: SharedPrefsHelper
) : ViewModel() {

    val viewData = MutableLiveData<SettingsFragmentData>()
    val blockedNumbersUpdated = MutableLiveData<List<String>>()
    val numberAlreadyBlocked = MutableLiveData<Unit>()

    // initial view rendering
    fun getViewData() {
        val data = SettingsFragmentData(
            instructions = R.string.customize_your_settings,
            allowOnlyCallsText = R.string.allow_calls_from_contact_list_only,
            allowOnlySmsText = R.string.allow_sms_from_contact_list_only,
            allowContactCallsChecked = sharedPrefsHelper.allowContactsOnlyCall,
            allowContactSmsChecked = sharedPrefsHelper.allowContactsOnlySms,
            blockedNumbers = sharedPrefsHelper.blockedNumbers.toList()
        )
        viewData.postValue(data)
    }

    // save switch state
    fun saveSettings(option: Setting, state: Boolean) {
        when (option) {
            Setting.ALLOW_CONTACTS_ONLY_CALL ->
                sharedPrefsHelper.allowContactsOnlyCall = state

            Setting.ALLOW_CONTACTS_ONLY_SMS ->
                sharedPrefsHelper.allowContactsOnlySms = state
        }
    }

    // check if entered number matches phone number regex
    fun isNumberValid(number: String) =
        Patterns.PHONE.matcher(number).matches()

    fun blockNumber(number: String) {
        // Since this is a String, this time I'll make an exception and
        // I'll store it in Shared Preferences just to spare time on integrating database.
        // Ideally, this would be saved into Room DB, no matter how complex it is. Shared
        // Preferences should be used for simple values (Boolean, Int, String...).
        val oldBlockedNumbers = sharedPrefsHelper.blockedNumbers
        if (oldBlockedNumbers.contains(number)) {
            numberAlreadyBlocked.postValue(Unit)
        } else {
            sharedPrefsHelper.blockedNumbers =
                oldBlockedNumbers.toMutableList().apply {
                    add(number)
                }
            blockedNumbersUpdated.postValue(sharedPrefsHelper.blockedNumbers.toList())
        }
    }
}

enum class Setting {
    ALLOW_CONTACTS_ONLY_CALL,
    ALLOW_CONTACTS_ONLY_SMS
}