package com.garoz.devriyemobil.di

import com.garoz.devriyemobil.ui.LoginViewModel
import com.garoz.devriyemobil.database.DevriyeRepository
import com.garoz.devriyemobil.domain.DevriyeKayitUseCase
import com.garoz.devriyemobil.domain.SecureTimeProvider
import com.garoz.devriyemobil.network.ApiClient
import com.garoz.devriyemobil.network.AuthRepository
import org.koin.dsl.module

val appModule = module {
    // get() fonksiyonu, Koin'in ihtiyaç duyulan bağımlılığı (örneğin DatabaseDriverFactory)
    // otomatik bulup enjekte etmesini sağlar.
    single { DevriyeRepository(driverFactory = get()) }
    single { SecureTimeProvider() }
    single { DevriyeKayitUseCase(repository = get(), timeProvider = get()) }

    // Ktor HTTP istemcisini Koin grafiğine tanıtıyoruz ki AuthRepository enjekte edilebilsin
    single { ApiClient.httpClient }
    single { AuthRepository(httpClient = get()) }
    factory { LoginViewModel(repository = get()) }
}