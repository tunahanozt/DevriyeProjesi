package com.garoz.devriyemobil.android.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.garoz.devriyemobil.database.DevriyeRepository
import com.garoz.devriyemobil.network.DevriyeLogRequest
// Ktor ve diğer importların projedeki ApiClient yapına göre eklenebilir
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

// KoinComponent arayüzü sayesinde "by inject()" ile bağımlılıkları doğrudan çekebiliyoruz
class DevriyeSyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams), KoinComponent {

    // Koin'den veritabanı deposunu otomatik al
    private val repository: DevriyeRepository by inject()

    // C# sunucunun adresi (Gerçek projede ApiClient gibi merkezi bir yerden gelmeli)
    private val BASE_URL = "http://10.0.2.2:5212/api" // Emülatör için localhost, fiziksel cihaz için IP girilmeli

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            // 1. Yerel veritabanından sadece senkronize EDİLMEMİŞ (0) olanları getir
            val bekleyenKayitlar = repository.senkronizeEdilmeyenleriGetir()

            if (bekleyenKayitlar.isEmpty()) {
                Log.d("DevriyeSyncWorker", "Gönderilecek yeni kayıt yok.")
                return@withContext Result.success() // İşlem başarılı, çalışmayı bitir
            }

            Log.d("DevriyeSyncWorker", "${bekleyenKayitlar.size} adet kayıt sunucuya gönderiliyor...")

            // 2. Her bir kaydı C# API'ye gönder
            // Not: İstersen API tarafında toplu (List) kabul eden bir endpoint yazıp tek seferde de atabilirsin
            for (kayit in bekleyenKayitlar) {
                val requestDto = DevriyeLogRequest(
                    nfcUid = kayit.toString(), // Geçici olarak kontrol amaçlı
                    okumaZamani = "",
                    sicilNo = ""
                )

                // Ktor HTTP Client ile POST isteği atıyoruz
                // ApiClient.httpClient'i daha önce AuthService'de kurduysan onu kullanabilirsin
                /*
                val response = ApiClient.httpClient.post("$BASE_URL/Devriye/log-ekle") {
                    contentType(ContentType.Application.Json)
                    setBody(requestDto)
                }

                // 3. Eğer C# API başarıyla aldıysa (200 OK), lokaldeki kaydın durumunu 1 (Senkronize Edildi) yap
                if (response.status.isSuccess()) {
                    repository.senkronizeEdildiIsaretle(kayit.id)
                    Log.d("DevriyeSyncWorker", "Kayıt ${kayit.id} başarıyla senkronize edildi.")
                } else {
                    // Sunucu hata verdiyse döngüyü kır, sonraki denemede baştan başlasın
                    Log.e("DevriyeSyncWorker", "Sunucu hatası: ${response.status}")
                    return@withContext Result.retry()
                }
                */
            }

            Result.success()

        } catch (e: Exception) {
            // İnternet var gibi görünüp C# sunucusuna ulaşılamazsa (Timeout vs.) buraya düşer
            Log.e("DevriyeSyncWorker", "Bağlantı hatası: ${e.message}")
            // İşletim sistemine "Bu işi daha sonra tekrar dene" komutu verir
            Result.retry()
        }
    }
}