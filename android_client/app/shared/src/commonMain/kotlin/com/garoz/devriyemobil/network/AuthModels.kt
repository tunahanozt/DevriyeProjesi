package com.garoz.devriyemobil.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// C# tarafındaki LoginRequest sınıfına karşılık gelir
@Serializable
data class LoginRequest(
    @SerialName("SicilNo") val sicilNo: String,
    @SerialName("Sifre") val sifre: String
)

// C# tarafındaki Ok(new { token = token }) yanıtına karşılık gelir
@Serializable
data class LoginResponse(
    val token: String
)