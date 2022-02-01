package com.gabrielaangebrandt.blockbuddy.di

import android.content.Context
import com.gabrielaangebrandt.blockbuddy.utils.NotificationHelper
import com.gabrielaangebrandt.blockbuddy.utils.PermissionsManager
import com.gabrielaangebrandt.blockbuddy.utils.SharedPrefsHelper
import com.google.gson.GsonBuilder
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
    single { SharedPrefsHelper(get(), get()) }
    single { NotificationHelper(get()) }
    single { PermissionsManager(get()) }
    single { GsonBuilder().create() }
}