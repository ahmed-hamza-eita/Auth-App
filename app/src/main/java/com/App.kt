package com

import LanguageManager
import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.hamza.authapp.utils.MySharedPreferences
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        MySharedPreferences.init(this)

    }

}