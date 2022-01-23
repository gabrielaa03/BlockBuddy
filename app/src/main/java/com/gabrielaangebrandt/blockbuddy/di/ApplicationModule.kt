package com.gabrielaangebrandt.blockbuddy.di

import com.gabrielaangebrandt.blockbuddy.utils.NotificationHelper
import org.koin.dsl.module

val applicationModule = module {
    single { NotificationHelper(get()) }
}