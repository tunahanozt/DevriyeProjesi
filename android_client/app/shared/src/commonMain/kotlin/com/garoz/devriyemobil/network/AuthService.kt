package com.garoz.devriyemobil.network

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class AuthService {
    // Sınıfın içerisinde, fonksiyonların hemen üstünde tanımlı olmalı:
    private val BASE_URL = "http://127.0.0.1:5212/api"

    suspend fun login(sicilNo: String, sifre: String): Result<String> {
        return try {
            val response = ApiClient.httpClient.post("$BASE_URL/Auth/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(sicilNo, sifre))
            }

            if (response.status.isSuccess()) {
                val loginResponse: LoginResponse = response.body()
                Result.success(loginResponse.token)
            } else {
                Result.failure(Exception("Giriş başarısız. Hata Kodu: ${response.status.value}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}