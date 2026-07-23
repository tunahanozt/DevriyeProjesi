package com.garoz.devriyemobil.network // Buraya kendi paket adını yazmayı unutma

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

object ApiClient {
    val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true // API'den beklemediğimiz bir alan gelirse uygulamanın çökmesini engeller
            })
        }
    }
}