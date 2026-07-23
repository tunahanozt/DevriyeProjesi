package com.garoz.devriyemobil.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class AuthRepository(private val httpClient: HttpClient) {
    // Android emülatöründen bilgisayardaki C# (localhost) projesine erişmek için 10.0.2.2 kullanılır.
    // Fiziksel telefonda test ederken buraya bilgisayarının yerel IP adresini (örn: 192.168.1.X) yazmalısın.
    private val baseUrl = "http://10.0.2.2:5212/api"

    suspend fun login(sicilNo: String, sifre: String): Result<String> {
        return try {
            val response = httpClient.post("$baseUrl/login") { // C# tarafındaki [HttpPost("login")]
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