package com.gabrielaangebrandt.blockbuddy.viewmodel

import android.util.Patterns
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
    init {
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
    fun saveSettings(state: Boolean) {
        sharedPrefsHelper.allowContactsOnlyCall = state
    }

    // check if entered number matches phone number regex
    fun isNumberValid(number: String) =
        Patterns.PHONE.matcher(number).matches()

    // For purpose of this project, blocked numbers will be saved on the app level. Ideally,
    // the app needs to access blocked number on the system level and use a cursor to add
    // more numbers. For that implementation we need to request additional changes for the
    // user, e.g. set the app to be a default dialer, so I went for this kind of solution.
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