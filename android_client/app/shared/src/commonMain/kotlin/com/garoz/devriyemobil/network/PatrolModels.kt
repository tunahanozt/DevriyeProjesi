package com.garoz.devriyemobil.network

import kotlinx.serialization.Serializable

// C# tarafındaki PatrolDto'ya karşılık gelir (ASP.NET Core JSON'u camelCase üretir).
@Serializable
data class PatrolDto(
    val id: Int,
    val userId: Int,
    val personelAdi: String = "",
    val patrolRouteId: Int,
    val rotaAdi: String = "",
    val status: String = "",
    val assignedAt: String? = null,
    val startedAt: String? = null,
    val completedAt: String? = null
)
