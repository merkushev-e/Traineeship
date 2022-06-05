package com.testtask.traineeship

import android.app.Application
import com.testtask.traineeship.DI.application
import com.testtask.traineeship.DI.mainScreen
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(listOf(application, mainScreen))
        }
    }
}