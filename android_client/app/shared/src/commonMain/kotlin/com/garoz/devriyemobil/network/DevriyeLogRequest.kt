package com.garoz.devriyemobil.network

import kotlinx.serialization.Serializable

@Serializable
data class DevriyeLogRequest(
    val nfcUid: String,
    val okumaZamani: String,
    val sicilNo: String
)