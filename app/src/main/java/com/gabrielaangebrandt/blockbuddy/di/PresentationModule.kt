package com.gabrielaangebrandt.blockbuddy.di

import com.gabrielaangebrandt.blockbuddy.viewmodel.HistoryFragmentViewModel
import com.gabrielaangebrandt.blockbuddy.viewmodel.ManageProcessingFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { ManageProcessingFragmentViewModel(get()) }
    viewModel { HistoryFragmentViewModel() }
}