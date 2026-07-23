package com.garoz.devriyemobil.android.di

import com.garoz.devriyemobil.database.DatabaseDriverFactory
import com.garoz.devriyemobil.android.worker.DevriyeSyncWorker
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val androidModule = module {
    // Android Context'ini vererek SQLDelight fabrikasını kuruyoruz
    single { DatabaseDriverFactory(androidContext()) }

    // Arka plan işçimizi Koin'e tanıtıyoruz
    worker { DevriyeSyncWorker(appContext = get(), workerParams = get()) }
}