package com.garoz.devriyemobil

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.garoz.devriyemobil.network.SessionManager
import com.garoz.devriyemobil.ui.DevriyeScreen
import com.garoz.devriyemobil.ui.DevriyeViewModel
import com.garoz.devriyemobil.ui.LoginScreen
import com.garoz.devriyemobil.ui.LoginViewModel
import org.koin.compose.koinInject

@Composable
fun App() {
    MaterialTheme {
        val session = koinInject<SessionManager>()
        var token by remember { mutableStateOf<String?>(null) }

        if (token == null) {
            // Koin üzerinden ViewModel'imizi güvenle çağırıyoruz
            val loginViewModel = koinInject<LoginViewModel>()

            LoginScreen(
                viewModel = loginViewModel,
                onLoginSuccess = { alinanToken ->
                    session.token = alinanToken   // Kimlik doğrulamalı istekler token'ı buradan okur
                    token = alinanToken           // Devriye ekranına geçişi tetikler
                }
            )
        } else {
            val devriyeViewModel = koinInject<DevriyeViewModel>()

            DevriyeScreen(
                viewModel = devriyeViewModel,
                onLogout = {
                    session.token = null
                    token = null
                }
            )
        }
    }
}
