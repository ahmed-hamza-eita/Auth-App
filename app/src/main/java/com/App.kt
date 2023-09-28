package com

import android.app.Application
import com.hamza.authapp.utils.MySharedPreferences
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        MySharedPreferences.init(this)

    }
}