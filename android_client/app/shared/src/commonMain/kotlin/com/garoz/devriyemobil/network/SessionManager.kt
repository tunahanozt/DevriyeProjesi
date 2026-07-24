package com.garoz.devriyemobil.network

// Giriş sonrası alınan JWT token'ı tutan basit oturum yöneticisi.
// Koin'de tekil (single) tutulur; kimlik doğrulamalı istekler token'ı buradan okur.
class SessionManager {
    var token: String? = null
}
