package com.garoz.devriyemobil

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform