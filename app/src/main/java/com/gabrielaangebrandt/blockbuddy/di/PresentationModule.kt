package com.gabrielaangebrandt.blockbuddy.di

import com.gabrielaangebrandt.blockbuddy.viewmodel.MainFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { MainFragmentViewModel() }
}