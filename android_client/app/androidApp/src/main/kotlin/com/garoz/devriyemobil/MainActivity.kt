package com.garoz.devriyemobil.android

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.garoz.devriyemobil.android.nfc.NfcManager

class MainActivity : ComponentActivity() {

    private lateinit var nfcManager: NfcManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val workConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED) // Sadece cihaz internete bağlıyken çalışır
            .build()

        val syncRequest = PeriodicWorkRequestBuilder<DevriyeSyncWorker>(15, TimeUnit.MINUTES)
            .setConstraints(workConstraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "OfflineDevriyeSync",
            ExistingPeriodicWorkPolicy.KEEP, // Mevcut bir görev varsa onu bozma
            syncRequest
        )

        // Modülü başlat ve okunan UID'yi dinle
        nfcManager = NfcManager(this) { nfcUid ->
            // Modüler mimari: İleride bu UID doğrudan ViewModel'e veya DevriyeRepository'ye aktarılacak
            Toast.makeText(this, "NFC Okundu UID: $nfcUid", Toast.LENGTH_SHORT).show()
        }

        if (!nfcManager.isNfcAvailable()) {
            Toast.makeText(this, "Bu cihazda NFC özelliği bulunmuyor!", Toast.LENGTH_LONG).show()
        }

        setContent {
            // Compose UI
        }
    }

    // Sadece delegasyon (yönlendirme) yapıyoruz
    override fun onResume() {
        super.onResume()
        nfcManager.enableForegroundDispatch()
    }

    override fun onPause() {
        super.onPause()
        nfcManager.disableForegroundDispatch()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        nfcManager.processIntent(intent)
    }
}