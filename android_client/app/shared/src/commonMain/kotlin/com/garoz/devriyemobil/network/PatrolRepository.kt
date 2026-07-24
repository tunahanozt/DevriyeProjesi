package com.garoz.devriyemobil.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess

// Devriye uçları ile konuşan repository. Her istek JWT token'ı Authorization başlığında gönderir.
class PatrolRepository(
    private val httpClient: HttpClient,
    private val session: SessionManager
) {
    // USB (adb reverse) / emülatör için; WiFi'de PC yerel IP'si yazılır.
    private val baseUrl = "http://127.0.0.1:5212/api"

    suspend fun getMyPatrols(): Result<List<PatrolDto>> {
        return try {
            val response = httpClient.get("$baseUrl/Patrols/mine") {
                header(HttpHeaders.Authorization, "Bearer ${session.token}")
            }
            if (response.status.isSuccess()) {
                val patrols: List<PatrolDto> = response.body()
                Result.success(patrols)
            } else {
                Result.failure(Exception("Devriyeler alınamadı. (Kod: ${response.status.value})"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Sunucuya bağlanılamadı."))
        }
    }

    suspend fun startPatrol(id: Int): Result<PatrolDto> = post("$baseUrl/Patrols/$id/start", "Devriye başlatılamadı.")

    suspend fun completePatrol(id: Int): Result<PatrolDto> = post("$baseUrl/Patrols/$id/complete", "Devriye tamamlanamadı.")

    private suspend fun post(url: String, hataMesaji: String): Result<PatrolDto> {
        return try {
            val response = httpClient.post(url) {
                header(HttpHeaders.Authorization, "Bearer ${session.token}")
            }
            if (response.status.isSuccess()) {
                val patrol: PatrolDto = response.body()
                Result.success(patrol)
            } else {
                Result.failure(Exception("$hataMesaji (Kod: ${response.status.value})"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Sunucuya bağlanılamadı."))
        }
    }
}
