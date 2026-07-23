package com.garoz.devriyemobil.domain

import android.os.SystemClock

actual class SecureTimeProvider actual constructor() {
    actual fun getUptimeMillis(): Long {
        // Android'in değiştirilemeyen donanım sayacı
        return SystemClock.elapsedRealtime()
    }

    actual fun getCurrentDeviceTimeMillis(): Long {
        return System.currentTimeMillis()
    }
}