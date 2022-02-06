package com.gabrielaangebrandt.blockbuddy.viewmodel

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gabrielaangebrandt.blockbuddy.R
import com.gabrielaangebrandt.blockbuddy.events.AlreadyBlockedEvent
import com.gabrielaangebrandt.blockbuddy.events.NumberBlockedSuccessfullyEvent
import com.gabrielaangebrandt.blockbuddy.model.viewrendering.SettingsFragmentData
import com.gabrielaangebrandt.blockbuddy.utils.EventBus
import com.gabrielaangebrandt.blockbuddy.utils.ProcessingManager
import com.gabrielaangebrandt.blockbuddy.utils.SharedPrefsHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable


class SettingsFragmentViewModel(
    private val sharedPrefsHelper: SharedPrefsHelper,
    private val processingManager: ProcessingManager
) : ViewModel() {

    val compDisposable = CompositeDisposable()
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
            allowContactSmsChecked = sharedPrefsHelper.allowContactsOnlySms,
            blockedNumbers = processingManager.getBlockedNumbers()
        )
        viewData.postValue(data)
    }

    // register observers for Processing Manager Events
    fun addToCompositeDisposables() {
        compDisposable.run {
            add(
                EventBus.subscribe<AlreadyBlockedEvent>()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        numberAlreadyBlocked.value = Unit
                    })
            add(
                EventBus.subscribe<NumberBlockedSuccessfullyEvent>()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        blockedNumbersUpdated.value = it.blockedNumbers
                    })
        }
    }

    // remove observers
    fun cleanUpDisposables() {
        if (!compDisposable.isDisposed) {
            compDisposable.clear()
        }
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
        processingManager.addBlockedNumber(number)
    }
}

enum class Setting {
    ALLOW_CONTACTS_ONLY_CALL,
    ALLOW_CONTACTS_ONLY_SMS
}