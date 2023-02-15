package com.example.textfield

import android.app.Application
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class Application : Application()  {

    override fun onCreate() {
        super.onCreate()
       Timber.plant(Timber.DebugTree())
    }
}