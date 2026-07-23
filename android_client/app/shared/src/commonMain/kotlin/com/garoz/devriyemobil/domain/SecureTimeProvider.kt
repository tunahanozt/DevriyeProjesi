package com.garoz.devriyemobil.domain

// İşletim sisteminden manipüle edilemez çekirdek süresini alacağımız sınıf
expect class SecureTimeProvider() {
    // Cihazın son yeniden başlatılmasından bu yana geçen milisaniye (Değiştirilemez)
    fun getUptimeMillis(): Long

    // Cihazın şu anki normal saati (Kullanıcı tarafından değiştirilebilir)
    fun getCurrentDeviceTimeMillis(): Long
}