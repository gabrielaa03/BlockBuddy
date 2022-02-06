package com.gabrielaangebrandt.blockbuddy.viewmodel

import androidx.lifecycle.Observer
import com.gabrielaangebrandt.blockbuddy.R
import com.gabrielaangebrandt.blockbuddy.model.viewrendering.SettingsFragmentData
import com.gabrielaangebrandt.blockbuddy.utils.ProcessingManager
import com.gabrielaangebrandt.blockbuddy.utils.SharedPrefsHelper
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SettingsFragmentViewModelTests : BaseTests() {

    private lateinit var viewModel: SettingsFragmentViewModel

    @Mock
    private lateinit var sharedPrefsHelper: SharedPrefsHelper

    @Mock
    private lateinit var processingManager: ProcessingManager

    @Mock
    private lateinit var viewDataObserver: Observer<SettingsFragmentData>

    @Mock
    private lateinit var blockedNumbersUpdatedObserver: Observer<List<String>>

    @Mock
    private lateinit var numberAlreadyBlockedObserver: Observer<Unit>

    override fun setUpViewModel() {
        viewModel = SettingsFragmentViewModel(sharedPrefsHelper, processingManager)
    }

    @Test
    fun testGetViewData() {
        val data = SettingsFragmentData(
            instructions = R.string.customize_your_settings,
            allowOnlyCallsText = R.string.allow_calls_from_contact_list_only,
            allowOnlySmsText = R.string.allow_sms_from_contact_list_only,
            allowContactCallsChecked = sharedPrefsHelper.allowContactsOnlyCall,
            blockedNumbers = sharedPrefsHelper.blockedNumbers
        )

        viewModel.getViewData()

        viewModel.viewData.observeForever(viewDataObserver)
        verify(viewDataObserver).onChanged(data)
        assertEquals(data, viewModel.viewData.value)
    }

    @Test
    fun testSaveSettings() {
        viewModel.saveSettings(true)
        verify(sharedPrefsHelper).allowContactsOnlyCall = true

        viewModel.saveSettings(false)
        verify(sharedPrefsHelper).allowContactsOnlyCall = false
    }

    @Test
    fun testIsNumberValid() {
        viewModel.isNumberValid("")
        verify(processingManager).isNumberValid("")
    }

    @Test
    fun testBlockNumber_NumberAlreadyBlocked() {
        val data = "123-123-123"
        `when`(sharedPrefsHelper.blockedNumbers).thenReturn(listOf(data))

        viewModel.blockNumber(data)

        viewModel.numberAlreadyBlocked.observeForever(numberAlreadyBlockedObserver)
        verify(numberAlreadyBlockedObserver).onChanged(Unit)
        assertEquals(Unit, viewModel.numberAlreadyBlocked.value)
    }

    @Test
    fun testBlockNumber_NumberBlockedSuccessfully() {
        val newNumber = "123-123-122"
        val data = listOf("123-123-123")
        `when`(sharedPrefsHelper.blockedNumbers).thenReturn(data)

        viewModel.blockNumber(newNumber)

        viewModel.blockedNumbersUpdated.observeForever(blockedNumbersUpdatedObserver)
        verify(blockedNumbersUpdatedObserver).onChanged(data)
        assertEquals(data, viewModel.blockedNumbersUpdated.value)
    }
}