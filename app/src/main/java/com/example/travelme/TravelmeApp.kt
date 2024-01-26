package com.example.travelme

import android.app.Application
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import java.io.File

@HiltAndroidApp
class TravelmeApp : Application(), Configuration.Provider {
    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()
}