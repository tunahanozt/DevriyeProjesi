package com.garoz.devriyemobil.domain

import com.garoz.devriyemobil.database.DevriyeRepository

class DevriyeKayitUseCase(
    private val repository: DevriyeRepository,
    private val timeProvider: SecureTimeProvider
) {
    // Sunucudan alınan son güvenli zaman ve o anki cihaz uptime'ı (Login olurken kaydedilecek)
    private var sonSenkronizeEdilenSunucuZamani: Long = 0L
    private var senkronizasyonAnindakiUptime: Long = 0L

    // NFC okunduğunda UI (ViewModel) tarafından çağrılacak ana fonksiyon
    fun nfcOkutuldu(uid: String, sicilNo: String) {
        val guvenliZamanDamgasi = hesaplaGuvenliZaman()

        repository.kayitEkle(
            nfcUid = uid,
            okumaZamani = guvenliZamanDamgasi.toString(),
            sicilNo = sicilNo
        )
    }

    private fun hesaplaGuvenliZaman(): Long {
        // Eğer daha önce sunucuyla senkronize olunmadıysa mecburen cihaz saatini kullan
        if (sonSenkronizeEdilenSunucuZamani == 0L) {
            return timeProvider.getCurrentDeviceTimeMillis()
        }

        // Hile Koruması: Sunucu saati + (Şu anki Uptime - Senkronizasyon anındaki Uptime)
        // Kullanıcı normal saati değiştirse bile Uptime değişmediği için gerçek zaman bulunur.
        val gecenGercekSure = timeProvider.getUptimeMillis() - senkronizasyonAnindakiUptime
        return sonSenkronizeEdilenSunucuZamani + gecenGercekSure
    }

    // Bu fonksiyon API'den Token/Login başarılı olduğunda çağrılacak
    fun sunucuZamaniniGuncelle(sunucuZamaniMillis: Long) {
        sonSenkronizeEdilenSunucuZamani = sunucuZamaniMillis
        senkronizasyonAnindakiUptime = timeProvider.getUptimeMillis()
    }
}