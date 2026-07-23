package com.garoz.devriyemobil.android

import android.app.Application
import com.garoz.devriyemobil.android.di.androidModule
import com.garoz.devriyemobil.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class DevriyeApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@DevriyeApplication)
            workManagerFactory() // WorkManager'ın Koin ile çalışması için şart
            modules(appModule, androidModule)
        }
    }
}