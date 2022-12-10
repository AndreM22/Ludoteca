package com.andremachicao.ludoteca.sharedPreferences

import android.annotation.SuppressLint
import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class InitApp:Application() {

    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var prefs: Prefs
    }

    override fun onCreate() {
        super.onCreate()
        prefs = Prefs(applicationContext)
    }
}