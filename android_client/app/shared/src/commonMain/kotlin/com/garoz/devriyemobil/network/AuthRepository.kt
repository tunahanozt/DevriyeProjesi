package com.garoz.devriyemobil.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class AuthRepository(private val httpClient: HttpClient) {
    // USB kablo (adb reverse) VEYA emülatör için 127.0.0.1 kullanılır.
    // Kabloyla test: terminalde bir kez ->  adb reverse tcp:5212 tcp:5212
    //   (telefonun localhost'unu PC'nin localhost'una USB üzerinden tünelller)
    // WiFi ile fiziksel telefon kullanacaksan burayı PC'nin yerel IP'si yap: http://192.168.1.X:5212/api
    private val baseUrl = "http://127.0.0.1:5212/api"

    suspend fun login(sicilNo: String, sifre: String): Result<String> {
        return try {
            val response = httpClient.post("$baseUrl/Auth/login") { // C# tarafı: AuthController [HttpPost("login")] => /api/Auth/login
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(sicilNo = sicilNo, sifre = sifre))
            }

            if (response.status.isSuccess()) {
                val loginResponse: LoginResponse = response.body()
                Result.success(loginResponse.token)
            } else {
                // HTTP 401 Unauthorized durumunda buraya düşer
                Result.failure(Exception("Hatalı sicil numarası veya şifre."))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Sunucuya bağlanılamadı. Lütfen internetinizi kontrol edin."))
        }
    }
}