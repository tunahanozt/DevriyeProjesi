package com.garoz.devriyemobil.android

import android.app.Application
import com.garoz.devriyemobil.android.di.androidModule
import com.garoz.devriyemobil.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class DevriyeApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@DevriyeApplication)
            // NOT: workManagerFactory() Aşama 3'te (offline senkronizasyon) eklenecek.
            // Şu an hiçbir Worker enqueue edilmediğinden WorkManager'ı varsayılan
            // başlatıcısına bırakıyoruz; Koin ile çift init "already initialized" crash veriyordu.
            modules(appModule, androidModule)
        }
    }
}