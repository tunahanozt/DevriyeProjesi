package com.garoz.devriyemobil.database

class DevriyeRepository(driverFactory: DatabaseDriverFactory) {

    // SQLDelight'ın bizim için otomatik ürettiği veritabanı sınıfını başlatıyoruz
    private val database = DevriyeDatabase(driverFactory.createDriver())
    private val queries = database.devriyeLogQueries

    // 1. NFC okunduğunda veriyi lokale kaydetme fonksiyonu
    fun kayitEkle(nfcUid: String, okumaZamani: String, sicilNo: String) {
        queries.kayitEkle(
            nfcUid = nfcUid,
            okumaZamani = okumaZamani,
            sicilNo = sicilNo
        )
    }

    // 2. İnternet geldiğinde sunucuya gönderilecek bekleyen kayıtları getirme
    fun senkronizeEdilmeyenleriGetir(): List<DevriyeLog> {
        return dbQuery.tumKayitlariGetir().executeAsList()
    }

    // 3. Sunucuya başarıyla iletilen kaydın durumunu güncelleme
    fun senkronizeEdildiIsaretle(id: Long) {
        queries.durumGuncelle(id)
    }
}