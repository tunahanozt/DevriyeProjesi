package com.garoz.devriyemobil

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.garoz.devriyemobil.ui.LoginScreen
import com.garoz.devriyemobil.ui.LoginViewModel
import org.koin.compose.koinInject

@Composable
fun App() {
    MaterialTheme {
        // Koin üzerinden ViewModel'imizi güvenle çağırıyoruz
        val loginViewModel = koinInject<LoginViewModel>()

        // Modern Login Ekranımız
        LoginScreen(
            viewModel = loginViewModel,
            onLoginSuccess = { token ->
                // Başarılı giriş sonrası token alındı
                println("Giriş Başarılı! Alınan Token: $token")
                // İleride buraya ana ekrana geçiş (Navigation) kodunu ekleyeceğiz
            }
        )
    }
}