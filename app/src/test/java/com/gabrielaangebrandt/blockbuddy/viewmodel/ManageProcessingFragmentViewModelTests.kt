package com.gabrielaangebrandt.blockbuddy.viewmodel

import androidx.lifecycle.Observer
import com.gabrielaangebrandt.blockbuddy.R
import com.gabrielaangebrandt.blockbuddy.model.processing.ProcessState
import com.gabrielaangebrandt.blockbuddy.utils.SharedPrefsHelper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ManageProcessingFragmentViewModelTests : BaseTests() {

    @Mock
    private lateinit var sharedPrefsHelper: SharedPrefsHelper

    private lateinit var viewModel: ManageProcessingFragmentViewModel

    @Mock
    private lateinit var processStateObserver: Observer<ProcessState>

    override fun setUpViewModel() {
        viewModel = ManageProcessingFragmentViewModel(sharedPrefsHelper)
    }

    @Test
    fun testChangeServiceState_setsSharedPrefsData_serviceRunning() {
        sharedPrefsHelper.serviceRunning = true
        viewModel.changeServiceState()
        assertFalse(sharedPrefsHelper.serviceRunning)
    }

    @Test
    fun testSetupUI_setsSharedPrefsData_serviceRunning() {
        `when`(sharedPrefsHelper.serviceRunning).thenReturn(true)

        val processState = ProcessState(
            image = R.drawable.background_stop_service,
            processingState = R.string.processing_calls,
            instructionsText = R.string.press_button_to_stop_processing
        )
        viewModel.setupUI()

        viewModel.processState.observeForever(processStateObserver)
        verify(processStateObserver).onChanged(processState)
        assertEquals(processState, viewModel.processState.value)
    }

    @Test
    fun testSetupUI_setsSharedPrefsData_serviceTerminated() {
        `when`(sharedPrefsHelper.serviceRunning).thenReturn(false)

        val processState = ProcessState(
            image = R.drawable.background_start_service,
            processingState = R.string.processing_turned_off,
            instructionsText = R.string.press_button_to_start_processing
        )
        viewModel.setupUI()

        viewModel.processState.observeForever(processStateObserver)
        verify(processStateObserver).onChanged(processState)
        assertEquals(processState, viewModel.processState.value)
    }
}