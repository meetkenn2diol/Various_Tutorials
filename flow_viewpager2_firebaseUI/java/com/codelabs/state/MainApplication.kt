package com.codelabs.state

import android.app.Application
import com.codelabs.state.BuildConfig
import timber.log.Timber

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}