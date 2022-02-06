package com.gabrielaangebrandt.blockbuddy.viewmodel

import android.provider.CallLog
import androidx.lifecycle.Observer
import com.gabrielaangebrandt.blockbuddy.model.processing.CallLogModel
import com.gabrielaangebrandt.blockbuddy.model.processing.CallState
import com.gabrielaangebrandt.blockbuddy.utils.ProcessingManager
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class HistoryFragmentViewModelTests : BaseTests() {

    private var processingManager = mock(ProcessingManager::class.java)
    private lateinit var viewModel: HistoryFragmentViewModel

    @Mock
    private lateinit var callLogsObserver: Observer<List<CallLogModel>>

    override fun setUpViewModel() {
        viewModel = HistoryFragmentViewModel(processingManager)
    }

    @Test
    fun testFilterCallLogs_setsLiveData() {
        val data = listOf(
            CallLogModel(
                CallLog.Calls.INCOMING_TYPE,
                "MOM",
                "123-123-123",
                "Today",
                CallState.UNDEFINED
            )
        )
        `when`(processingManager.getCallLogs()).thenReturn(data)

        viewModel.filterCallLogs()

        viewModel.callLogs.observeForever(callLogsObserver)
        verify(callLogsObserver).onChanged(data)
        assertEquals(data, viewModel.callLogs.value)
    }
}