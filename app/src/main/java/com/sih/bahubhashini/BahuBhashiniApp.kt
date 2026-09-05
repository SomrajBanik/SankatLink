package com.sih.bahubhashini

import android.app.Application
import android.util.Log

class BahuBhashiniApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.i("BahuBhashini", "BahuBhashini Offline Emergency Communication Engine Initialized.")
    }
}

