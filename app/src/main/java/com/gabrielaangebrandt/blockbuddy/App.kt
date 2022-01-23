package com.gabrielaangebrandt.blockbuddy

import android.app.*
import androidx.lifecycle.*
import com.gabrielaangebrandt.blockbuddy.di.applicationModule
import com.gabrielaangebrandt.blockbuddy.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

val TAG: String = App::class.java.name

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@App)
            modules(
                listOf(
                    applicationModule,
                    presentationModule
                )
            )
        }
    }

    companion object {
        lateinit var appContext: App
            private set
    }
}