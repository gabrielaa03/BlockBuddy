package com.gabrielaangebrandt.blockbuddy.di

import android.content.Context
import com.gabrielaangebrandt.blockbuddy.utils.NotificationHelper
import com.gabrielaangebrandt.blockbuddy.utils.SharedPrefsHelper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val PREF_NAME = "SharedPreferencesBlockBuddy"

val applicationModule = module {
    single {
        androidContext().getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )
    }
    single { SharedPrefsHelper(get()) }
    single { NotificationHelper(get()) }
}