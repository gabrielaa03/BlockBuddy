package com.gabrielaangebrandt.blockbuddy.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gabrielaangebrandt.blockbuddy.R
import com.gabrielaangebrandt.blockbuddy.model.viewrendering.SettingsFragmentData
import com.gabrielaangebrandt.blockbuddy.utils.ProcessingManager
import com.gabrielaangebrandt.blockbuddy.utils.SharedPrefsHelper

class SettingsFragmentViewModel(
    private val sharedPrefsHelper: SharedPrefsHelper,
    private val processingManager: ProcessingManager
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
            blockedNumbers = sharedPrefsHelper.blockedNumbers
        )
        viewData.postValue(data)
    }

    // save switch state
    fun saveSettings(allowContactsOnlyCall: Boolean) {
        sharedPrefsHelper.allowContactsOnlyCall = allowContactsOnlyCall
    }

    // check is number valid
    fun isNumberValid(number: String) =
        processingManager.isNumberValid(number)

    // block number on the app level
    fun blockNumber(number: String) {
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