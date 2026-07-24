package com.garoz.devriyemobil

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.garoz.devriyemobil.ui.HomeScreen
import com.garoz.devriyemobil.ui.LoginScreen
import com.garoz.devriyemobil.ui.LoginViewModel
import org.koin.compose.koinInject

@Composable
fun App() {
    MaterialTheme {
        // Basit oturum durumu: token null ise Login ekranı, doluysa ana ekran gösterilir.
        var token by remember { mutableStateOf<String?>(null) }

        if (token == null) {
            // Koin üzerinden ViewModel'imizi güvenle çağırıyoruz
            val loginViewModel = koinInject<LoginViewModel>()

            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = { alinanToken ->
                    println("Giriş Başarılı! Alınan Token: $alinanToken")
                    token = alinanToken // Ana ekrana geçişi tetikler
                }
            )
        } else {
            HomeScreen(
                onLogout = { token = null } // Çıkış yapınca tekrar giriş ekranına dön
            )
        }
    }
}
