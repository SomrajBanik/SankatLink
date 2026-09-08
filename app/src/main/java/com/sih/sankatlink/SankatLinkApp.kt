package com.sih.sankatlink

import android.app.Application
import android.util.Log

class SankatLinkApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.i("SankatLink", "SankatLink Offline Emergency Communication Engine Initialized.")
    }
}

